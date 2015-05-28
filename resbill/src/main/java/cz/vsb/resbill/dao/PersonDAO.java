package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.model.Person;

/**
 * Data access interface for {@link Person} model entity.
 * 
 * @author HAL191
 */
public interface PersonDAO {

	/**
	 * Finds a {@link Person} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link Person} entity
	 */
	Person findPerson(Integer perosnId);

	/**
	 * Finds {@link Person} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link Person} entities
	 */
	List<Person> findPersons(PersonCriteria criteria, Integer offset, Integer limit);

	/**
	 * Saves current state of given {@link Person} entity.
	 * 
	 * @param person
	 *          entity to save
	 * @return saved entity
	 */
	Person savePerson(Person person);

	/**
	 * Deletes given {@link Person} entity.
	 * 
	 * @param person
	 *          entity to delete
	 * @return deleted entity
	 */
	Person deletePerson(Person person);
}
