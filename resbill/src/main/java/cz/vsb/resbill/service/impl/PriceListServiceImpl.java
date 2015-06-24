package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.InvoicePriceListCriteria;
import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.dao.InvoicePriceListDAO;
import cz.vsb.resbill.dao.PriceListDAO;
import cz.vsb.resbill.dao.TariffDAO;
import cz.vsb.resbill.dto.tariff.PriceListDTO;
import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.exception.PriceListServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.InvoicePriceList;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.PriceListService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link PriceListService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class PriceListServiceImpl implements PriceListService {

	private static final Logger log = LoggerFactory.getLogger(PriceListServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Inject
	private PriceListDAO priceListDAO;

	@Inject
	private InvoicePriceListDAO invoicePriceListDAO;

	@Inject
	private TariffDAO tariffDAO;

	@Override
	public PriceList findPriceList(Integer priceListId) throws ResBillException {
		try {
			return priceListDAO.findPriceList(priceListId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding PriceList by id=" + priceListId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<PriceList> findPriceLists(PriceListCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return priceListDAO.findPriceLists(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for PriceList entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<PriceListDTO> findPriceListDTOs(PriceListCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			List<PriceList> priceLists = findPriceLists(criteria, offset, limit);
			List<PriceListDTO> dtos = new ArrayList<PriceListDTO>(priceLists.size());
			for (PriceList pl : priceLists) {
				dtos.add(new PriceListDTO(pl));
			}
			return dtos;
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for PriceListDTOs by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public PriceList savePriceList(PriceList priceList) throws PriceListServiceException, ResBillException {
		try {
			// kontrola: je ukladany cenik poslednim platnym
			if (priceList.getPeriod().getEndDate() != null) {
				throw new PriceListServiceException(Reason.NOT_LAST_PRICE_LIST);
			}

			PriceList previous = null;
			if (priceList.getId() == null) { // novy cenik - pridani
				// zjisteni dosud platneho ceniku pro ukonceni jeho platnosti
				previous = priceListDAO.findLastPriceList(priceList.getTariff().getId());
				if (previous == null) { // prvni cenik u tarifu - netreba provadet dalsi kontroly
					priceList.setPrevious(null);
				} else { // nasledny cenik u tarifu - nutne kontroly
					priceList.setPrevious(previous);
				}
			} else { // existujici cenik - editace
				// kontrola zmeny tarifu
				Tariff origTariff = tariffDAO.findTariff(priceList.getTariff().getId());
				if (!origTariff.equals(priceList.getTariff())) {
					throw new PriceListServiceException(Reason.TARIFF_MODIFICATION);
				}

				// kontrola fakturace podle ceniku
				checkInvoiceExistence(priceList);

				if (priceList.getPrevious() == null) { // prvni cenik u tarifu
					// kontrola pokryti kontraktu
					checkContractPeriodCoverage(priceList);
				} else {
					previous = priceListDAO.findPriceList(priceList.getPrevious().getId());
				}
			}
			if (previous != null) {
				// kontrola pripadne kolize dat s fakturovanym predchozim cenikem
				checkInvoiceDateCollision(priceList);

				// ukonceni platnosti predchoziho ceniku, je-li treba
				Date terminationDate = DateUtils.addDays(priceList.getPeriod().getBeginDate(), -1);
				if (!terminationDate.equals(previous.getPeriod().getEndDate())) {
					terminatePriceList(previous, terminationDate);
				}
			}
			return priceListDAO.savePriceList(priceList);
		} catch (PriceListServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving PriceList entity: " + priceList, e);
			throw new ResBillException(e);
		}
	}

	/**
	 * kontrola pokryti obdobi prirazeni ke kontraktu - zjisteni prvniho data prirazeni -> maximalni mozne datum pocatku platnosti ceniku
	 * 
	 * @param priceList
	 * @throws PriceListServiceException
	 *           nepokryva-li cenik obdobi prirazeni tarifu ke kontraktu
	 */
	private void checkContractPeriodCoverage(PriceList priceList) throws PriceListServiceException {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT MIN(ct.period.beginDate) FROM ContractTariff AS ct");
		jpql.append(" WHERE ct.tariff.id = :tariffId");

		Query query = em.createQuery(jpql.toString());
		query.setParameter("tariffId", priceList.getTariff().getId());

		Date minDate = (Date) query.getSingleResult();

		if (minDate != null && !Period.isDateInPeriod(minDate, priceList.getPeriod())) {
			throw new PriceListServiceException(Reason.CONTRACT_PERIOD_UNCOVERED);
		}
	}

	/**
	 * kontrola existence faktury k danemu ceniku
	 * 
	 * @param priceList
	 * @throws PriceListServiceException
	 *           existuje-li faktura podle tohoto ceniku
	 */
	private void checkInvoiceExistence(PriceList priceList) throws PriceListServiceException {
		InvoicePriceListCriteria iplCriteria = new InvoicePriceListCriteria();
		iplCriteria.setPriceListId(priceList.getId());
		List<InvoicePriceList> ipls = invoicePriceListDAO.findInvoicePriceLists(iplCriteria, null, null);
		if (!ipls.isEmpty()) {
			throw new PriceListServiceException(Reason.INVOICE_EXISTENCE);
		}
	}

	/**
	 * kontrola kolize s fakturaci - urceni posledniho data fakturace podle predchoziho ceniku -> minimalni mozne datum (+ 1 den) pocatku platnosti noveho ceniku
	 * 
	 * @param priceList
	 * @throws PriceListServiceException
	 *           existuje-li faktura podle predchoziho ceniku a datum pocatku platnosti aktualniho ceniku zasahuje do jejiho obdobi
	 */
	private void checkInvoiceDateCollision(PriceList priceList) throws PriceListServiceException {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT MAX(inv.period.endDate) FROM InvoicePriceList AS ipl");
		jpql.append(" JOIN ipl.invoice AS inv");
		jpql.append(" WHERE ipl.priceList.id = :priceListId");

		Query query = em.createQuery(jpql.toString());
		query.setParameter("priceListId", priceList.getPrevious().getId());

		Date maxDate = (Date) query.getSingleResult();

		if (maxDate != null && Period.isDateInPeriod(maxDate, priceList.getPeriod())) {
			throw new PriceListServiceException(Reason.INVOICE_DATE_COLLISION);
		}
	}

	/**
	 * ukonci platnost ceniku k danemu dni - provede kontrolu na spravnost vznikleho obdobi platnosti
	 * 
	 * @param priceList
	 *          ukoncovany cenik
	 * @throws PriceListServiceException
	 *           pokud by platnost cenika nebyla spravnym obdobim (konec by predchazel pocatku)
	 */
	private void terminatePriceList(PriceList priceList, Date terminationDate) throws PriceListServiceException {
		// kontrola: nevznikl by ukoncenim platnosti predchoziho ceniku spatny interval?
		if (!Period.isValid(priceList.getPeriod().getBeginDate(), terminationDate)) {
			throw new PriceListServiceException(Reason.INVALID_PERIOD);
		}
		priceList.getPeriod().setEndDate(terminationDate);

		// ulozeni predchoziho ceniku
		priceListDAO.savePriceList(priceList);
	}

	/**
	 * zrusi ukonceni platnosti predchoziho ceniku - znovu zplatni cenik
	 * 
	 * @param priceList
	 */
	private void unterminatePriceList(PriceList priceList) {
		priceList.getPeriod().setEndDate(null);
		// ulozeni predchoziho ceniku
		priceListDAO.savePriceList(priceList);
	}

	@Override
	public PriceList deletePriceList(Integer priceListId) throws PriceListServiceException, ResBillException {
		try {
			PriceList priceList = priceListDAO.findPriceList(priceListId);

			// nelze smazat neposledni cenik
			if (priceList.getPeriod().getEndDate() != null) {
				throw new PriceListServiceException(Reason.NOT_LAST_PRICE_LIST);
			}
			// nelze smazat prvni cenik
			if (priceList.getPrevious() == null) {
				throw new PriceListServiceException(Reason.FIRST_PRICE_LIST);
			}
			// nelze smazat fakturovany cenik
			checkInvoiceExistence(priceList);

			// zplatneni predchoziho ceniku
			unterminatePriceList(priceList.getPrevious());

			return priceListDAO.deletePriceList(priceList);
		} catch (PriceListServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting PriceList with id=" + priceListId, e);
			throw new ResBillException(e);
		}
	}
}
