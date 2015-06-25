/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.criteria.statistics.StatisticContractCriteria;
import cz.vsb.resbill.dto.agenda.ContractAgendaDTO;
import cz.vsb.resbill.dto.contract.ContractHeaderDTO;
import cz.vsb.resbill.dto.contract.ContractListDTO;
import cz.vsb.resbill.dto.statistics.StatisticContractDTO;
import cz.vsb.resbill.dto.statistics.StatisticReportDTO;
import cz.vsb.resbill.exception.ContractServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface ContractService {

	/**
	 * Finds a {@link Contract} with given key.
	 * 
	 * @param contractId
	 *          primary key of a {@link Contract}
	 * @return found {@link Contract}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Contract findContract(Integer contractId) throws ResBillException;

	/**
	 * Finds {@link ContractHeaderDTO}
	 * 
	 * @param contractId
	 * @return
	 * @throws ResBillException
	 */
	ContractHeaderDTO findContractHeaderDTO(Integer contractId) throws ResBillException;

	/**
	 * Finds a list of {@link Contract} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @return list of found {@link Contract} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<Contract> findContracts(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Finds a list of all {@link ContractListDTO} instances.
	 * 
	 * @return
	 * @throws ResBillException
	 *           if an error occurs
	 */
	List<ContractListDTO> findContractListDTOs(ContractCriteria criteria, Integer offset, Integer limit) throws ResBillException;

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

	/**
	 * 
	 * @param criteria
	 * @return
	 * @throws ResBillException
	 */
	StatisticReportDTO<StatisticContractDTO> findContractStatisticReport(StatisticContractCriteria criteria) throws ResBillException;

	/**
	 * Persists given {@link Contract} entity.
	 * 
	 * @param contract
	 *          entity to save
	 * @return saved {@link Contract} entity (with generated primary key)
	 * @throws ContractServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Contract saveContract(Contract contract) throws ContractServiceException, ResBillException;

	/**
	 * Deletes {@link Contract} entity with given primary key.
	 * 
	 * @param contractId
	 *          key of deleted {@link Contract} entity
	 * @return deleted {@link Contract} entity
	 * @throws ContractServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Contract deleteContract(Integer contractId) throws ContractServiceException, ResBillException;
}
