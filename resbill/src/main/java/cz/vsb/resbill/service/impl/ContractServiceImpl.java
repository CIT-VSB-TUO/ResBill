/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.criteria.TransactionCriteria;
import cz.vsb.resbill.criteria.statistics.StatisticContractCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.ContractInvoiceTypeDAO;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.dao.TransactionDAO;
import cz.vsb.resbill.dto.ContractDTO;
import cz.vsb.resbill.dto.agenda.ContractAgendaDTO;
import cz.vsb.resbill.dto.statistics.StatisticContractDTO;
import cz.vsb.resbill.dto.statistics.StatisticReportDTO;
import cz.vsb.resbill.dto.statistics.StatisticUsageDTO;
import cz.vsb.resbill.exception.ContractServiceException;
import cz.vsb.resbill.exception.ContractServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractInvoiceType;
import cz.vsb.resbill.model.ContractTariff;
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

  private static final Logger    log = LoggerFactory.getLogger(ContractServiceImpl.class);

  @PersistenceContext
  private EntityManager          em;

  @Inject
  private ContractDAO            contractDAO;

  @Inject
  private TransactionDAO         transactionDAO;

  @Inject
  private ContractInvoiceTypeDAO contractInvoiceTypeDAO;

  @Inject
  private ContractTariffDAO      contractTariffDAO;

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

  /**
   * 
   * @param criteria
   * @return
   * @throws ResBillException
   */
  public StatisticReportDTO<StatisticContractDTO> findContractStatisticReport(StatisticContractCriteria criteria) throws ResBillException {
    try {
      StatisticReportDTO<StatisticContractDTO> statisticReportDTO = new StatisticReportDTO<StatisticContractDTO>();
      statisticReportDTO.setBeginDate(criteria.getBeginDate());
      statisticReportDTO.setEndDate(criteria.getEndDate());

      Integer overallServer = 0;
      Integer overallCpu = 0;
      BigDecimal overallMemoryGB = BigDecimal.ZERO;
      BigDecimal overallProvisionedSpaceGB = BigDecimal.ZERO;
      BigDecimal overallUsedSpaceGB = BigDecimal.ZERO;
      BigDecimal overallBackupGB = BigDecimal.ZERO;

      List<StatisticContractDTO> componentDTOs = new ArrayList<StatisticContractDTO>();
      statisticReportDTO.setComponentDTOs(componentDTOs);

      // Zjistit a poznbamenat si dilci hodnoty
      List<Object[]> usages = contractDAO.findContractStatistics(criteria);
      for (Object[] usage : usages) {
        Contract contract = (Contract) usage[0];
        Integer server = ((Number) usage[1]).intValue();
        Integer cpu = ((Number) usage[2]).intValue();
        BigDecimal memoryGB = (BigDecimal) usage[3];
        BigDecimal provisionedSpaceGB = (BigDecimal) usage[4];
        BigDecimal usedSpaceGB = (BigDecimal) usage[5];
        BigDecimal backupGB = (BigDecimal) usage[6];

        StatisticUsageDTO usageDTO = new StatisticUsageDTO();
        usageDTO.setServer(server);
        usageDTO.setCpu(cpu);
        usageDTO.setMemoryGB(memoryGB);
        usageDTO.setProvisionedSpaceGB(provisionedSpaceGB);
        usageDTO.setUsedSpaceGB(usedSpaceGB);
        usageDTO.setBackupGB(backupGB);

        StatisticContractDTO componentDTO = new StatisticContractDTO();
        componentDTOs.add(componentDTO);
        componentDTO.setContractDTO(new ContractDTO(contract));
        componentDTO.setUsageDTO(usageDTO);

        overallServer = overallServer + server;
        overallCpu = overallCpu + cpu;
        overallMemoryGB = overallMemoryGB.add(memoryGB);
        overallProvisionedSpaceGB = overallProvisionedSpaceGB.add(provisionedSpaceGB);
        overallUsedSpaceGB = overallUsedSpaceGB.add(usedSpaceGB);
        overallBackupGB = overallBackupGB.add(backupGB);
      }

      // Zaznamenat celkove hodnoty
      StatisticUsageDTO overallUsageDTO = new StatisticUsageDTO();
      overallUsageDTO.setServer(overallServer);
      overallUsageDTO.setCpu(overallCpu);
      overallUsageDTO.setMemoryGB(overallMemoryGB);
      overallUsageDTO.setProvisionedSpaceGB(overallProvisionedSpaceGB);
      overallUsageDTO.setUsedSpaceGB(overallUsedSpaceGB);
      overallUsageDTO.setBackupGB(overallBackupGB);
      statisticReportDTO.setOverallDTO(overallUsageDTO);

      // Dopocitat procenta
      for (StatisticContractDTO componentDTO : componentDTOs) {
        StatisticUsageDTO usageDTO = componentDTO.getUsageDTO();

        if (overallServer != 0) {
          usageDTO.setServerPercentage(usageDTO.getServer().floatValue() / overallServer * 100);
        } else {
          usageDTO.setServerPercentage(0);
        }

        if (overallCpu != 0) {
          usageDTO.setCpuPercentage(usageDTO.getCpu().floatValue() / overallCpu * 100);
        } else {
          usageDTO.setCpuPercentage(0);
        }

        if (overallMemoryGB.floatValue() != 0) {
          usageDTO.setMemoryGbPercentage(usageDTO.getMemoryGB().floatValue() / overallMemoryGB.floatValue() * 100);
        } else {
          usageDTO.setMemoryGbPercentage(0);
        }

        if (overallProvisionedSpaceGB.floatValue() != 0) {
          usageDTO.setProvisionedSpaceGbPercentage(usageDTO.getProvisionedSpaceGB().floatValue() / overallProvisionedSpaceGB.floatValue() * 100);
        } else {
          usageDTO.setProvisionedSpaceGbPercentage(0);
        }

        if (overallUsedSpaceGB.floatValue() != 0) {
          usageDTO.setUsedSpaceGbPercentage(usageDTO.getUsedSpaceGB().floatValue() / overallUsedSpaceGB.floatValue() * 100);
        } else {
          usageDTO.setUsedSpaceGbPercentage(0);
        }

        if (overallBackupGB.floatValue() != 0) {
          usageDTO.setBackupGbPercentage(usageDTO.getBackupGB().floatValue() / overallBackupGB.floatValue() * 100);
        } else {
          usageDTO.setBackupGbPercentage(0);
        }
      }

      return statisticReportDTO;
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding findContractStatisticReport.", exc);
      throw new ResBillException(exc);
    }
  }

  /**
	 * 
	 */
  @Override
  public Contract saveContract(Contract contract) throws ContractServiceException, ResBillException {
    try {
      if (contract.getId() != null) {
        // kontrola pokusu o zmenu zakaznika
        Contract origContract = contractDAO.findContract(contract.getId());
        if (!origContract.getCustomer().equals(contract.getCustomer())) {
          throw new ContractServiceException(Reason.CUSTOMER_MODIFICATION);
        }

        // byl zmenen pocatek platnosti kontraktu
        if (!origContract.getPeriod().getBeginDate().equals(contract.getPeriod().getBeginDate())) {
          // kontroly prirazeni typu uctovani
          ContractInvoiceType firstCIT = contractInvoiceTypeDAO.findFirstContractInvoiceType(contract.getId());
          if (firstCIT != null) {
            // kontrola naruseni platnosti prvniho prirazeni typu uctovani
            if (!contract.getPeriod().getBeginDate().before(firstCIT.getPeriod().getBeginDate()) && !Period.isDateInPeriod(contract.getPeriod().getBeginDate(), firstCIT.getPeriod())) {
              throw new ContractServiceException(Reason.CONTRACT_INVOICE_TYPE_INVALID_PERIOD);
            }

            // zmena pocatku platnosti prirazeni prvniho typu uctovani
            firstCIT.getPeriod().setBeginDate(contract.getPeriod().getBeginDate());
            contractInvoiceTypeDAO.saveContractInvoiceType(firstCIT);
          }

          // kontroly prirazeni tarifu
          ContractTariff firstCT = contractTariffDAO.findFirstContractTariff(contract.getId());
          if (firstCT != null) {
            // kontrola naruseni platnosti prvniho prirazeni tarifu
            if (!contract.getPeriod().getBeginDate().before(firstCT.getPeriod().getBeginDate()) && !Period.isDateInPeriod(contract.getPeriod().getBeginDate(), firstCT.getPeriod())) {
              throw new ContractServiceException(Reason.CONTRACT_TARIFF_INVALID_PERIOD);
            }

            // kontrola, zda obdobi stale pokryva stejne jiz fakturovane importy
            // puvodni sada dennich vyuziti
            List<Integer> origDUIds = getInvoicedDailyUsageIds(firstCT);
            // zmena pocatku platnosti prirazeni prvniho tarifu
            firstCT.getPeriod().setBeginDate(contract.getPeriod().getBeginDate());
            // sada po editaci
            List<Integer> newDUIds = getInvoicedDailyUsageIds(firstCT);
            if (!CollectionUtils.isEqualCollection(origDUIds, newDUIds)) {
              throw new ContractServiceException(Reason.CONTRACT_TARIFF_INVOICE_DAILY_USAGE_UNCOVERED);
            }

            contractTariffDAO.saveContractTariff(firstCT);
          }
        }
        // zmenen konkretni (konecny) konec platnosti kontraktu
        if (contract.getPeriod().getEndDate() != null && !Objects.equals(origContract.getPeriod().getEndDate(), contract.getPeriod().getEndDate())) {
          // kontroly prirazeni typu uctovani
          ContractInvoiceType lastCIT = contractInvoiceTypeDAO.findLastContractInvoiceType(contract.getId());
          if (lastCIT != null) {
            // kontrola naruseni platnosti posledniho prirazeni typu uctovani
            if (!Period.isDateInPeriod(contract.getPeriod().getEndDate(), lastCIT.getPeriod())) {
              throw new ContractServiceException(Reason.CONTRACT_INVOICE_TYPE_INVALID_PERIOD);
            }
          }

          // kontroly prirazeni tarifu
          ContractTariff lastCT = contractTariffDAO.findLastContractTariff(contract.getId());
          if (lastCT != null) {
            // kontrola naruseni platnosti posledniho prirazeni tarifu
            if (!Period.isDateInPeriod(contract.getPeriod().getEndDate(), lastCT.getPeriod())) {
              throw new ContractServiceException(Reason.CONTRACT_TARIFF_INVALID_PERIOD);
            }
          }
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

      // kontrakt smazan i s vazbami (kaskada)
      return contractDAO.deleteContract(contract);
    } catch (ContractServiceException e) {
      throw e;
    } catch (Exception e) {
      log.error("An unexpected error occured while deleting Contract with id=" + contractId, e);
      throw new ResBillException(e);
    }
  }
}
