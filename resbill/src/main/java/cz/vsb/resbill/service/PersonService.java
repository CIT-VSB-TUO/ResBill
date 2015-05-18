package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.model.Person;

public interface PersonService {

	List<Person> findPersons(PersonCriteria criteria);
}
