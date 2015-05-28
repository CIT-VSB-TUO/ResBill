package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ContractPersonCriteria;
import cz.vsb.resbill.model.ContractPerson;

/**
 * Data access interface for {@link ContractPerson} model entity.
 * 
 * @author HAL191
 */
public interface ContractPersonDAO {

	/**
	 * Finds a {@link ContractPerson} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link ContractPerson} entity
	 */
	ContractPerson findContractPerson(Integer contractPersonId);

	/**
	 * Finds {@link ContractPerson} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link ContractPerson} entities
	 */
	List<ContractPerson> findContractPersons(ContractPersonCriteria criteria, Integer offset, Integer limit);
}
