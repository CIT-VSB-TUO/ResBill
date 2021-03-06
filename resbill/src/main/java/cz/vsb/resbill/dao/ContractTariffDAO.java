package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractTariff;

/**
 * Data access interface for {@link ContractTariff} model entity.
 * 
 * @author HAL191
 */
public interface ContractTariffDAO {

	/**
	 * Finds a {@link ContractTariff} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link ContractTariff} entity
	 */
	ContractTariff findContractTariff(Integer contractTariffId);

	/**
	 * Finds the first {@link ContractTariff} of {@link Contract} specified by the given key.
	 * 
	 * @param contractId
	 * @return
	 */
	ContractTariff findFirstContractTariff(Integer contractId);

	/**
	 * Finds the last (current) {@link ContractTariff} of {@link Contract} specified by the given key.
	 * 
	 * @param contractId
	 * @return
	 */
	ContractTariff findLastContractTariff(Integer contractId);

	/**
	 * Finds {@link ContractTariff} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link ContractTariff} entities
	 */
	List<ContractTariff> findContractTariffs(ContractTariffCriteria criteria, Integer offset, Integer limit);

	/**
	 * Saves current state of given {@link ContractTariff} entity.
	 * 
	 * @param contractTariff
	 *          entity to save
	 * @return saved entity
	 */
	ContractTariff saveContractTariff(ContractTariff contractTariff);

	/**
	 * Deletes given {@link ContractTariff} entity.
	 * 
	 * @param contractTariff
	 *          entity to delete
	 * @return deleted entity
	 */
	ContractTariff deleteContractTariff(ContractTariff contractTariff);
}
