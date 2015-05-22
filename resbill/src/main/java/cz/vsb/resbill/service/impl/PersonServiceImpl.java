package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.dao.PersonDAO;
import cz.vsb.resbill.exception.ConstraintViolationException;
import cz.vsb.resbill.exception.ConstraintViolationException.Reason;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	@Inject
	private PersonDAO personDAO;

	@Override
	public Person fidPerson(Integer personId) {
		return personDAO.findPerson(personId);
	}

	@Override
	public List<Person> findPersons(PersonCriteria criteria) {
		return personDAO.findPersons(criteria);
	}

	@Override
	public Person savePerson(Person person) throws ConstraintViolationException {
		// kontrola jedinecnosti emailu
		PersonCriteria criteria = new PersonCriteria();
		criteria.setEmail(person.getEmail());
		List<Person> persons = personDAO.findPersons(criteria);
		Person foundPerson = DataAccessUtils.singleResult(persons);
		if (foundPerson != null && !foundPerson.getId().equals(person.getId())) {
			throw new ConstraintViolationException(Reason.UNIQUE_KEY);
		}
		return personDAO.savePerson(person);
	}

	@Override
	public Person deletePerson(Integer personId) {
		Person person = personDAO.findPerson(personId);
		if (person != null) {
			person = personDAO.deletePerson(person);
		}
		return person;
	}
}
