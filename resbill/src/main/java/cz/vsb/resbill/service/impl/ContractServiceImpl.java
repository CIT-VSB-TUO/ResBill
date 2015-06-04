/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dto.ContractAgendaDTO;
import cz.vsb.resbill.dto.ContractDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.service.ResBillService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@ResBillService
public class ContractServiceImpl implements ContractService {

  private static final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

  @Inject
  private ContractDAO         contractDAO;

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
      outOfTariffContractIds.addAll(afterEndContractIds);

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

      // Pokud neexistuje zadny "zajima" kontrakt, pak vratim prazdny seznam
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
      }

      return dtos;
    } catch (Exception exc) {
      log.error("An unexpected error occured while finding ContractAgendaDTOs.", exc);
      throw new ResBillException(exc);
    }
  }

}
