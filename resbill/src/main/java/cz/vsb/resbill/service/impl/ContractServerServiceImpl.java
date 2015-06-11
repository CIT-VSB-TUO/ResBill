package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.ContractServerDAO;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.exception.ContractServerServiceException;
import cz.vsb.resbill.exception.ContractServerServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.ContractTariff;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.service.ContractServerService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link ContractServerService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class ContractServerServiceImpl implements ContractServerService {

	private static final Logger log = LoggerFactory.getLogger(ContractServerServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ContractServerDAO contractServerDAO;

	@Inject
	private ContractDAO contractDAO;

	@Inject
	private ContractTariffDAO contractTariffDAO;

	@Override
	public ContractServer findContractServer(Integer contractServerId) throws ResBillException {
		try {
			return contractServerDAO.findContractServer(contractServerId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ContractServer by id=" + contractServerId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<ContractServer> findContractServers(ContractServerCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return contractServerDAO.findContractServers(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for ContractServer entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractServer saveContractServer(ContractServer contractServer) throws ContractServerServiceException, ResBillException {
		try {
			// kontrola vylucnosti prirazeni serveru v danem obdobi
			ContractServerCriteria csCriteria = new ContractServerCriteria();
			csCriteria.setServerId(contractServer.getServer().getId());
			csCriteria.setAssociatedFrom(contractServer.getPeriod().getBeginDate());
			csCriteria.setAssociatedFrom(contractServer.getPeriod().getEndDate());
			List<ContractServer> css = contractServerDAO.findContractServers(csCriteria, null, null);
			if (!css.isEmpty() && (contractServer.getId() == null || css.size() > 1 || !contractServer.getId().equals(css.get(0).getId()))) {
				throw new ContractServerServiceException(Reason.SERVER_ASSOCIATION_PERIOD_COLLISION);
			}
			// kontrola, zda spada do doby trvani kontraktu
			Contract contract = contractDAO.findContract(contractServer.getContract().getId());
			if (!Period.isPeriodInPeriod(contractServer.getPeriod(), contract.getPeriod())) {
				throw new ContractServerServiceException(Reason.OUT_OF_CONTRACT_DURATION);
			}

			if (contractServer.getId() == null) {
				// kontrola, zda ma kontrakt prirazen tarif
				ContractTariffCriteria ctCriteria = new ContractTariffCriteria();
				ctCriteria.setContractId(contractServer.getContract().getId());
				List<ContractTariff> cts = contractTariffDAO.findContractTariffs(ctCriteria, null, null);
				if (cts.isEmpty()) {
					throw new ContractServerServiceException(Reason.CONTRACT_WITHOUT_TARIFF);
				}
			} else {
				// kontrola pokusu o zmenu prirazeni
				ContractServer origCS = contractServerDAO.findContractServer(contractServer.getId());
				if (!origCS.getContract().equals(contractServer.getContract()) || !origCS.getServer().equals(contractServer.getServer())) {
					throw new ContractServerServiceException(Reason.CONTRACT_SERVER_MODIFICATION);
				}

				// kontrola, zda obdobi stale pokryva jiz fakturovane importy
				// puvodni sada dennich vyuziti
				List<Integer> origDUIds = getInvoicedDailyUsageIds(origCS);
				// sada po editaci
				List<Integer> newDUIds = getInvoicedDailyUsageIds(contractServer);

				if (!CollectionUtils.isEqualCollection(origDUIds, newDUIds)) {
					throw new ContractServerServiceException(Reason.INVOICE_DAILY_USAGE_UNCOVERED);
				}
			}

			return contractServerDAO.saveContractServer(contractServer);
		} catch (ContractServerServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving ContractServer entity: " + contractServer, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractServer deleteContractServer(Integer contractServerId) throws ContractServerServiceException, ResBillException {
		try {
			ContractServer cs = contractServerDAO.findContractServer(contractServerId);
			// kontrola, zda nebylo jiz fakturovano
			List<Integer> duIds = getInvoicedDailyUsageIds(cs);
			if (!duIds.isEmpty()) {
				throw new ContractServerServiceException(Reason.DAILY_USAGE_INVOICED);
			}

			return contractServerDAO.deleteContractServer(cs);
		} catch (ContractServerServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting ContractServer with id=" + contractServerId, e);
			throw new ResBillException(e);
		}
	}

	/* Vraci seznam ID DailyUsage patrici serveru prirazovanemu kontraktu. */
	private List<Integer> getInvoicedDailyUsageIds(ContractServer cs) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT DISTINCT dailyUsage.id");
		jpql.append(" FROM InvoiceDailyUsage AS idu");
		jpql.append(" JOIN idu.dailyUsage AS dailyUsage");
		jpql.append(" JOIN dailyUsage.dailyImport AS dailyImport");
		jpql.append(" WHERE dailyUsage.server.id = :serverId");
		jpql.append(" AND dailyImport.date >= :from");
		if (cs.getPeriod().getEndDate() != null) {
			jpql.append(" AND dailyImport.date <= :to");
		}
		TypedQuery<Integer> query = em.createQuery(jpql.toString(), Integer.class);
		query.setParameter("serverId", cs.getServer().getId());
		query.setParameter("from", cs.getPeriod().getBeginDate());
		if (cs.getPeriod().getEndDate() != null) {
			query.setParameter("to", cs.getPeriod().getEndDate());
		}
		return query.getResultList();
	}

}
