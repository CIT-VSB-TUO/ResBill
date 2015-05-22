package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.exception.ConstraintViolationException;
import cz.vsb.resbill.model.Person;

public interface PersonService {

	/**
	 * Finds a {@link Person} with given key.
	 * 
	 * @param personId
	 *          primary key of a {@link Person}
	 * @return found {@link Person}, otherwise <code>null</code>
	 */
	Person fidPerson(Integer personId);

	/**
	 * Finds a list of {@link Person} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @return list of found {@link Person} entities
	 */
	List<Person> findPersons(PersonCriteria criteria);

	/**
	 * Persists given {@link Person} entity.
	 * 
	 * @param person
	 *          entity to save
	 * @return saved {@link Person} entity (with generated primary key)
	 * @throws ConstraintViolationException
	 *           if any constraint is violated (non unique e-mail)
	 */
	Person savePerson(Person person) throws ConstraintViolationException;

	/**
	 * Deletes {@link Person} entity with given primary key.
	 * 
	 * @param personId
	 *          key of deleted {@link Person} entity
	 * @return deleted {@link Person} entity
	 */
	Person deletePerson(Integer personId);
}
