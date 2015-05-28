package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ContractTariffCriteria;
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
}
