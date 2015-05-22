package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.exception.PersonServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Person;

public interface PersonService {

	/**
	 * Finds a {@link Person} with given key.
	 * 
	 * @param personId
	 *          primary key of a {@link Person}
	 * @return found {@link Person}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Person findPerson(Integer personId) throws ResBillException;

	/**
	 * Finds a list of {@link Person} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link Person} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<Person> findPersons(PersonCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link Person} entity.
	 * 
	 * @param person
	 *          entity to save
	 * @return saved {@link Person} entity (with generated primary key)
	 * @throws PersonServiceException
	 *           if specific saving error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Person savePerson(Person person) throws PersonServiceException, ResBillException;

	/**
	 * Deletes {@link Person} entity with given primary key.
	 * 
	 * @param personId
	 *          key of deleted {@link Person} entity
	 * @return deleted {@link Person} entity
	 * @throws PersonServiceException
	 *           if specific deleting error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Person deletePerson(Integer personId) throws PersonServiceException, ResBillException;
}
