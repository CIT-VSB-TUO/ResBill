/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.dto.ContractAgendaDTO;
import cz.vsb.resbill.dto.ContractDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface ContractService {

	Contract findContract(Integer contractId) throws ResBillException;

	List<Contract> findContracts(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	List<ContractDTO> findContractDTOs(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException;

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
	List<ContractAgendaDTO> findContractAgendaDTOs(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException;
}
