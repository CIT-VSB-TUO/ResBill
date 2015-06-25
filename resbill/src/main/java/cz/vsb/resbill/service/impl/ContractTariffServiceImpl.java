package cz.vsb.resbill.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.ContractServerDAO;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.dao.PriceListDAO;
import cz.vsb.resbill.exception.ContractTariffServiceException;
import cz.vsb.resbill.exception.ContractTariffServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.ContractTariff;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.service.ContractTariffService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link ContractTariffService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class ContractTariffServiceImpl implements ContractTariffService {

	private static final Logger log = LoggerFactory.getLogger(ContractTariffServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ContractTariffDAO contractTariffDAO;

	@Inject
	private ContractDAO contractDAO;

	@Inject
	private PriceListDAO priceListDAO;

	@Inject
	private ContractServerDAO contractServerDAO;

	@Override
	public ContractTariff findContractTariff(Integer contractTariffId) throws ResBillException {
		try {
			return contractTariffDAO.findContractTariff(contractTariffId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ContractTariff by id=" + contractTariffId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<ContractTariff> findContractTariffs(ContractTariffCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return contractTariffDAO.findContractTariffs(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for ContractTariff entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractTariff saveContractTariff(ContractTariff contractTariff) throws ContractTariffServiceException, ResBillException {
		try {
			// editovat lze pouze posledni prirazeni
			if (contractTariff.getPeriod().getEndDate() != null) {
				throw new ContractTariffServiceException(Reason.NOT_LAST_CONTRACT_TARIFF);
			}
			// zjisteni kontraktu
			Contract contract = contractDAO.findContract(contractTariff.getContract().getId());

			ContractTariff previous = null;
			if (contractTariff.getId() == null) { // nove prirazeni
				// zjisteni posledniho (aktualniho) prirazeni
				previous = contractTariffDAO.findLastContractTariff(contract.getId());
				if (previous == null) {
					contractTariff.setPrevious(null);
					// prvni prirazeni ma platnost od pocatku kontraktu
					contractTariff.getPeriod().setBeginDate(contract.getPeriod().getBeginDate());
				} else {
					contractTariff.setPrevious(previous);
				}
			} else { // editace existujiciho prirazeni
				// kontrola modifikace prirazeni
				ContractTariff origCT = contractTariffDAO.findContractTariff(contractTariff.getId());
				if (!origCT.getContract().equals(contract) || !origCT.getTariff().equals(contractTariff.getTariff())) {
					throw new ContractTariffServiceException(Reason.CONTRACT_TARIFF_MODIFICATION);
				}
				// kontrola, zda obdobi stale pokryva stejne jiz fakturovane importy
				// puvodni sada dennich vyuziti
				List<Integer> origDUIds = getInvoicedDailyUsageIds(origCT);
				// sada po editaci
				List<Integer> newDUIds = getInvoicedDailyUsageIds(contractTariff);
				if (!CollectionUtils.isEqualCollection(origDUIds, newDUIds)) {
					throw new ContractTariffServiceException(Reason.INVOICE_DAILY_USAGE_UNCOVERED);
				}

				if (contractTariff.getPrevious() == null) {
					// kontrola zmeny pocatecniho data
					if (!contractTariff.getPeriod().getBeginDate().equals(contract.getPeriod().getBeginDate())) {
						throw new ContractTariffServiceException(Reason.FIRST_CONTRACT_TARIFF_BEGIN_DATE_MODIFICATION);
					}
				} else {
					previous = contractTariffDAO.findContractTariff(contractTariff.getPrevious().getId());
				}
			}
			// kontrola ze ceniky prirazovaneho tarifu pokryvaji dane obdobi
			PriceList firstPL = priceListDAO.findFirstPriceList(contractTariff.getTariff().getId());
			if (firstPL.getPeriod().getBeginDate().after(contractTariff.getPeriod().getBeginDate())) {
				throw new ContractTariffServiceException(Reason.NOT_COVERED_BY_PRICE_LISTS);
			}
			if (previous != null) {
				// kontrola zda pocatek spada do platnosti kontraktu
				checkContractBelonging(contractTariff, contract);

				// ukonceni platnosti predchoziho prirazeni, je-li treba
				Date terminationDate = DateUtils.addDays(contractTariff.getPeriod().getBeginDate(), -1);
				if (!terminationDate.equals(previous.getPeriod().getEndDate())) {
					terminateContractTariff(previous, terminationDate);
				}
			}

			return contractTariffDAO.saveContractTariff(contractTariff);
		} catch (ContractTariffServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving ContractTariff entity: " + contractTariff, e);
			throw new ResBillException(e);
		}
	}

	private void checkContractBelonging(ContractTariff ct, Contract contract) throws ContractTariffServiceException {
		if (!Period.isDateInPeriod(ct.getPeriod().getBeginDate(), contract.getPeriod())) {
			throw new ContractTariffServiceException(Reason.OUT_OF_CONTRACT_DURATION);
		}
	}

	private void terminateContractTariff(ContractTariff ct, Date terminationDate) throws ContractTariffServiceException {
		if (!Period.isValid(ct.getPeriod().getBeginDate(), terminationDate)) {
			throw new ContractTariffServiceException(Reason.INVALID_PERIOD);
		}
		// kontrola, zda obdobi stale pokryva stejne jiz fakturovane importy
		// puvodni sada dennich vyuziti
		List<Integer> origDUIds = getInvoicedDailyUsageIds(ct);
		// provedeni ukonceni
		ct.getPeriod().setEndDate(terminationDate);
		// sada po editaci
		List<Integer> newDUIds = getInvoicedDailyUsageIds(ct);

		if (!CollectionUtils.isEqualCollection(origDUIds, newDUIds)) {
			throw new ContractTariffServiceException(Reason.INVOICE_DAILY_USAGE_UNCOVERED);
		}
		// ulozeni zmeny
		contractTariffDAO.saveContractTariff(ct);
	}

	private List<Integer> getInvoicedDailyUsageIds(ContractTariff ct) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT DISTINCT dailyUsage.id");
		jpql.append(" FROM InvoiceDailyUsage AS idu");
		jpql.append(" JOIN idu.dailyUsage AS dailyUsage");
		jpql.append(" JOIN dailyUsage.dailyImport AS dailyImport");
		jpql.append(" JOIN dailyUsage.server AS server");
		jpql.append(" JOIN server.contractServers AS cs");
		jpql.append(" WHERE cs.contract.id = :contractId");
		jpql.append(" AND dailyImport.date >= cs.period.beginDate");
		jpql.append(" AND (cs.period.endDate IS NULL OR dailyImport.date <= cs.period.endDate)");
		jpql.append(" AND dailyImport.date >= :from");
		if (ct.getPeriod().getEndDate() != null) {
			jpql.append(" AND dailyImport.date <= :to");
		}
		TypedQuery<Integer> query = em.createQuery(jpql.toString(), Integer.class);
		query.setParameter("contractId", ct.getContract().getId());
		query.setParameter("from", ct.getPeriod().getBeginDate());
		if (ct.getPeriod().getEndDate() != null) {
			query.setParameter("to", ct.getPeriod().getEndDate());
		}
		return query.getResultList();
	}

	@Override
	public ContractTariff deleteContractTariff(Integer contractTariffId) throws ContractTariffServiceException, ResBillException {
		try {
			ContractTariff contractTariff = contractTariffDAO.findContractTariff(contractTariffId);

			// nelze smazat neposledni prirazeni
			if (contractTariff.getPeriod().getEndDate() != null) {
				throw new ContractTariffServiceException(Reason.NOT_LAST_CONTRACT_TARIFF);
			}

			if (contractTariff.getPrevious() == null) {
				// kontrola zda jiz neni kontraktu prirazen server
				ContractServerCriteria csCriteria = new ContractServerCriteria();
				csCriteria.setContractId(contractTariff.getContract().getId());
				List<ContractServer> css = contractServerDAO.findContractServers(csCriteria, null, null);
				if (!css.isEmpty()) {
					throw new ContractTariffServiceException(Reason.CONTRACT_SERVER_ASSOCIATED);
				}
			} else {
				// kontrola, zda jiz nebylo fakturovano
				List<Integer> duIds = getInvoicedDailyUsageIds(contractTariff);
				if (!duIds.isEmpty()) {
					throw new ContractTariffServiceException(Reason.DAILY_USAGE_INVOICED);
				}
				// zplatneni predchoziho prirazeni
				unterminateContractTariff(contractTariff.getPrevious());
			}

			return contractTariffDAO.deleteContractTariff(contractTariff);
		} catch (ContractTariffServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting ContractTariff with id=" + contractTariffId, e);
			throw new ResBillException(e);
		}
	}

	private void unterminateContractTariff(ContractTariff ct) throws ContractTariffServiceException {
		// kontrola, zda obdobi stale pokryva stejne jiz fakturovane importy
		// puvodni sada dennich vyuziti
		List<Integer> origDUIds = getInvoicedDailyUsageIds(ct);
		// zplatneni
		ct.getPeriod().setEndDate(null);
		// sada po editaci
		List<Integer> newDUIds = getInvoicedDailyUsageIds(ct);

		if (!CollectionUtils.isEqualCollection(origDUIds, newDUIds)) {
			throw new ContractTariffServiceException(Reason.INVOICE_DAILY_USAGE_UNCOVERED);
		}

		contractTariffDAO.saveContractTariff(ct);
	}
}
