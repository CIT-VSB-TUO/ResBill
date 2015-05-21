package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.exception.ConstraintViolationException;
import cz.vsb.resbill.model.Person;

public interface PersonService {

	Person fidPerson(Integer personId);

	List<Person> findPersons(PersonCriteria criteria);

	Person savePerson(Person person) throws ConstraintViolationException;

	Person deletePerson(Integer personId);
}
