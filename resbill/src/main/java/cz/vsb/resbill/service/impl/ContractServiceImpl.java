/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.criteria.TransactionCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.TransactionDAO;
import cz.vsb.resbill.dto.ContractAgendaDTO;
import cz.vsb.resbill.dto.ContractDTO;
import cz.vsb.resbill.exception.ContractServiceException;
import cz.vsb.resbill.exception.ContractServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.Transaction;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.service.ResBillService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@ResBillService
public class ContractServiceImpl implements ContractService {

	private static final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ContractDAO contractDAO;

	@Inject
	private TransactionDAO transactionDAO;

	@Override
	public Contract findContract(Integer contractId) throws ResBillException {
		try {
			return contractDAO.findContract(contractId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding Contract by id=" + contractId, e);
			throw new ResBillException(e);
		}
	}

	/**
   * 
   */
	@Override
	public List<Contract> findContracts(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return contractDAO.findContracts(criteria, offset, limit);
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding Contracts.", exc);
			throw new ResBillException(exc);
		}
	}

	/**
   * 
   */
	@Override
	public List<ContractDTO> findContractDTOs(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			List<Contract> contracts = findContracts(criteria, offset, limit);
			List<ContractDTO> dtos = new ArrayList<ContractDTO>();

			for (Contract contract : contracts) {
				ContractDTO dto = new ContractDTO();
				dto.fill(contract);
				dtos.add(dto);
			}

			return dtos;
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding ContractDTOs.", exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * Nalezne kontrakty pro pouziti na Dashboardu (Agenda).
	 * 
	 * Tj. nalezne vsechny kontrakty, ktere splnuji alespon jednu pozadovanou vlastnost (Feature) - tim se lisi od standardni metody findContracts.
	 * 
	 * @param criteria
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Override
	public List<ContractAgendaDTO> findContractAgendaDTOs(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			ContractCriteria crit = null;
			List<Contract> contracts = null;
			Set<Integer> contractIds = new HashSet<Integer>();

			// Kontrakty odebírající zdroje po skončení platnosti
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ContractCriteria.Feature.DAILY_USAGE_AFTER_CONTRACT_END));
			contracts = findContracts(crit, null, null);
			Set<Integer> afterEndContractIds = new HashSet<Integer>();
			for (Contract contract : contracts) {
				afterEndContractIds.add(contract.getId());
			}
			contractIds.addAll(afterEndContractIds);

			// Kontrakty odebírající zdroje mimo období tarifu
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ContractCriteria.Feature.DAILY_USAGE_OUT_OF_TARIFF));
			contracts = findContracts(crit, null, null);
			Set<Integer> outOfTariffContractIds = new HashSet<Integer>();
			for (Contract contract : contracts) {
				outOfTariffContractIds.add(contract.getId());
			}
			contractIds.addAll(outOfTariffContractIds);

			// Kontrakty bez typu účtování
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ContractCriteria.Feature.NO_INVOICE_TYPE));
			contracts = findContracts(crit, null, null);
			Set<Integer> noInvoiceTypeContractIds = new HashSet<Integer>();
			for (Contract contract : contracts) {
				noInvoiceTypeContractIds.add(contract.getId());
			}
			contractIds.addAll(noInvoiceTypeContractIds);

			// Contract bez serveru
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ContractCriteria.Feature.NO_SERVER));
			contracts = findContracts(crit, null, null);
			Set<Integer> noServerContractIds = new HashSet<Integer>();
			for (Contract contract : contracts) {
				noServerContractIds.add(contract.getId());
			}
			contractIds.addAll(noServerContractIds);

			// Contract bez tarifu
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ContractCriteria.Feature.NO_TARIFF));
			contracts = findContracts(crit, null, null);
			Set<Integer> noTariffContractIds = new HashSet<Integer>();
			for (Contract contract : contracts) {
				noTariffContractIds.add(contract.getId());
			}
			contractIds.addAll(noTariffContractIds);

			// Kontrakty se zapornym zustatkem
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ContractCriteria.Feature.NEGATIVE_BALANCE));
			contracts = findContracts(crit, null, null);
			Set<Integer> negativeBalanceContractIds = new HashSet<Integer>();
			for (Contract contract : contracts) {
				negativeBalanceContractIds.add(contract.getId());
			}
			contractIds.addAll(negativeBalanceContractIds);

			// Kontrakty s kladnym zustatkem
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ContractCriteria.Feature.POSITIVE_BALANCE));
			contracts = findContracts(crit, null, null);
			Set<Integer> positiveBalanceContractIds = new HashSet<Integer>();
			for (Contract contract : contracts) {
				positiveBalanceContractIds.add(contract.getId());
			}
			contractIds.addAll(positiveBalanceContractIds);

			// Pokud neexistuje zadny "zajimavy" kontrakt, pak vratim prazdny seznam
			if (contractIds.isEmpty()) {
				return new ArrayList<ContractAgendaDTO>();
			}

			// Ziskam vsechny "zajimave" kontrakty setridene dle puvodniho pozadavku
			crit = new ContractCriteria();
			crit.setContractIds(contractIds);
			crit.setOrderBy(criteria.getOrderBy());

			contracts = findContracts(crit, offset, limit);
			List<ContractAgendaDTO> dtos = new ArrayList<ContractAgendaDTO>();

			for (Contract contract : contracts) {
				ContractAgendaDTO dto = new ContractAgendaDTO();
				dto.fill(contract);
				dtos.add(dto);

				if (afterEndContractIds.contains(contract.getId())) {
					dto.setDailyUsagesAfterContractEnd(true);
				}
				if (outOfTariffContractIds.contains(contract.getId())) {
					dto.setDailyUsagesOutOfTariff(true);
				}
				if (noInvoiceTypeContractIds.contains(contract.getId())) {
					dto.setNoInvoiceType(true);
				}
				if (noServerContractIds.contains(contract.getId())) {
					dto.setNoServer(true);
				}
				if (noTariffContractIds.contains(contract.getId())) {
					dto.setNoTariff(true);
				}
				if (negativeBalanceContractIds.contains(contract.getId())) {
					dto.setNegativeBalance(true);
				}
				if (positiveBalanceContractIds.contains(contract.getId())) {
					dto.setPositiveBalance(true);
				}
			}

			return dtos;
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding ContractAgendaDTOs.", exc);
			throw new ResBillException(exc);
		}
	}

	@Override
	public Contract saveContract(Contract contract) throws ContractServiceException, ResBillException {
		try {
			if (contract.getId() != null) {
				// kontrola pokusu o zmenu zakaznika
				Contract origContract = contractDAO.findContract(contract.getId());
				if (!origContract.getCustomer().getId().equals(contract.getCustomer().getId())) {
					throw new ContractServiceException(Reason.CUSTOMER_MODIFICATION);
				}

				// kontrola, zda neni narusena platnost prirazeni serveru
				StringBuilder jpql = new StringBuilder();
				jpql.append("SELECT MIN(cs.period.beginDate), CASE WHEN COUNT(*) = COUNT(cs.period.endDate) THEN MAX(cs.period.endDate) END");
				jpql.append(" FROM ContractServer AS cs");
				jpql.append(" WHERE cs.contract.id = :contractId");
				Query query = em.createQuery(jpql.toString());
				query.setParameter("contractId", contract.getId());

				Object[] result = (Object[]) query.getSingleResult();
				Period minMaxPeriod = new Period();
				minMaxPeriod.setBeginDate((Date) result[0]);
				minMaxPeriod.setEndDate((Date) result[1]);

				if (minMaxPeriod.getBeginDate() != null && !Period.isPeriodInPeriod(minMaxPeriod, contract.getPeriod())) {
					throw new ContractServiceException(Reason.SERVER_ASSOCIATION_EXCLUSION);
				}
			}
			return contractDAO.saveContract(contract);
		} catch (ContractServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving Contract entity: " + contract, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Contract deleteContract(Integer contractId) throws ContractServiceException, ResBillException {
		try {
			Contract contract = contractDAO.findContract(contractId);
			// kontrola, zda existuje jiz transakce
			TransactionCriteria txCriteria = new TransactionCriteria();
			txCriteria.setContractId(contractId);
			List<Transaction> transactions = transactionDAO.findTransactions(txCriteria, null, null);
			if (!transactions.isEmpty()) {
				throw new ContractServiceException(Reason.TRANSACTION_EXISTENCE);
			}

			return contractDAO.deleteContract(contract);
		} catch (ContractServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting Contract with id=" + contractId, e);
			throw new ResBillException(e);
		}
	}
}
