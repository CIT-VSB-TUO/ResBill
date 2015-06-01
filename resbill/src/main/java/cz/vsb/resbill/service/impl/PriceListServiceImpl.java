package cz.vsb.resbill.service.impl;

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
import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.exception.PriceListServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.InvoicePriceList;
import cz.vsb.resbill.model.PriceList;
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
	public PriceList savePriceList(PriceList priceList) throws PriceListServiceException, ResBillException {
		try {
			if (priceList.getId() == null) {
				// zjisteni dosud platneho ceniku
				PriceList lastValid = priceListDAO.findLastValidPriceList(priceList.getTariff().getId());
				if (lastValid != null) {
					// zjisteni minimalniho mozneho datumu pocatku platnosti ceniku
					StringBuilder jpql = new StringBuilder();
					jpql.append("SELECT MAX(inv.period.endDate) FROM InvoicePriceList AS ipl");
					jpql.append(" JOIN ipl.invoice AS inv");
					jpql.append(" WHERE ipl.priceList.id = :priceListId");

					Query query = em.createQuery(jpql.toString());
					query.setParameter("priceListId", lastValid.getId());

					Date maxDate = (Date) query.getSingleResult();

					// kontrola: pocatek musi byt az po fakturaci
					if (maxDate != null && priceList.getPeriod().getBeginDate().compareTo(maxDate) <= 0) {
						throw new PriceListServiceException(Reason.INVOICE_DATE_CLASH);
					}

					// ukonceni platnosti dosud platneho ceniku
					Date newEndDate = DateUtils.addDays(priceList.getPeriod().getBeginDate(), -1);
					// kontrola: nevznikl by spatny interval?
					if (lastValid.getPeriod().getBeginDate().compareTo(newEndDate) > 0) {
						throw new PriceListServiceException(Reason.INVALID_PERIOD);
					}
					lastValid.getPeriod().setEndDate(newEndDate);
					priceListDAO.savePriceList(lastValid);
				}
			} else {
				// kontrola zda jiz bylo podle ceniku fakturovano
				InvoicePriceListCriteria iplCriteria = new InvoicePriceListCriteria();
				iplCriteria.setPriceListId(priceList.getId());
				List<InvoicePriceList> ipls = invoicePriceListDAO.findInvoicePriceLists(iplCriteria, null, null);
				if (!ipls.isEmpty()) {
					throw new PriceListServiceException(Reason.INVOICE_PRICE_LIST);
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

	@Override
	public PriceList deletePriceList(Integer priceListId) throws PriceListServiceException, ResBillException {
		try {
			PriceList priceList = priceListDAO.findPriceList(priceListId);
			// kontrola zavislosti
			// vytvorena faktura
			InvoicePriceListCriteria iplCriteria = new InvoicePriceListCriteria();
			iplCriteria.setPriceListId(priceListId);
			List<InvoicePriceList> ipls = invoicePriceListDAO.findInvoicePriceLists(iplCriteria, null, null);
			if (!ipls.isEmpty()) {
				throw new PriceListServiceException(Reason.INVOICE_PRICE_LIST);
			}

			return priceListDAO.deletePriceList(priceList);
		} catch (PriceListServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting PriceList with id=" + priceListId, e);
			throw new ResBillException(e);
		}
	}
}
