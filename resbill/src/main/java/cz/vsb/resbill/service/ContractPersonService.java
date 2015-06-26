package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ContractPersonCriteria;
import cz.vsb.resbill.exception.ContractPersonServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractPerson;

/**
 * A service for processing {@link ContractPerson} entities.
 * 
 * @author HAL191
 *
 */
public interface ContractPersonService {

	/**
	 * Finds a {@link ContractPerson} with given key.
	 * 
	 * @param contractPersonId
	 *          primary key of a {@link ContractPerson}
	 * @return found {@link ContractPerson}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractPerson findContractPerson(Integer contractPersonId) throws ResBillException;

	/**
	 * Finds a list of {@link ContractPerson} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link ContractPerson} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<ContractPerson> findContractPersons(ContractPersonCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link ContractPerson} entity.
	 * 
	 * @param contractPerson
	 *          entity to save
	 * @return saved {@link ContractPerson} entity (with generated primary key)
	 * @throws ContractPersonServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractPerson saveContractPerson(ContractPerson contractPerson) throws ContractPersonServiceException, ResBillException;

	/**
	 * Deletes {@link ContractPerson} entity with given primary key.
	 * 
	 * @param contractPersonId
	 *          key of deleted {@link ContractPerson} entity
	 * @return deleted {@link ContractPerson} entity
	 * @throws ContractPersonServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractPerson deleteContractPerson(Integer contractPersonId) throws ContractPersonServiceException, ResBillException;
}
