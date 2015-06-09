package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.exception.ContractTariffServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractTariff;

/**
 * A service for processing {@link ContractTariff} entities.
 * 
 * @author HAL191
 *
 */
public interface ContractTariffService {

	/**
	 * Finds a {@link ContractTariff} with given key.
	 * 
	 * @param contractTariffId
	 *          primary key of a {@link ContractTariff}
	 * @return found {@link ContractTariff}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractTariff findContractTariff(Integer contractTariffId) throws ResBillException;

	/**
	 * Finds a list of {@link ContractTariff} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link ContractTariff} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<ContractTariff> findContractTariffs(ContractTariffCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link ContractTariff} entity.
	 * 
	 * @param contractTariff
	 *          entity to save
	 * @return saved {@link ContractTariff} entity (with generated primary key)
	 * @throws ContractTariffServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractTariff saveContractTariff(ContractTariff contractTariff) throws ContractTariffServiceException, ResBillException;

	/**
	 * Deletes {@link ContractTariff} entity with given primary key.
	 * 
	 * @param contractTariffId
	 *          key of deleted {@link ContractTariff} entity
	 * @return deleted {@link ContractTariff} entity
	 * @throws ContractTariffServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractTariff deleteContractTariff(Integer contractTariffId) throws ContractTariffServiceException, ResBillException;
}
