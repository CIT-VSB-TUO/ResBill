package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.dao.PersonDAO;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	@Inject
	private PersonDAO personDAO;

	@Override
	public List<Person> findPersons(PersonCriteria criteria) {
		return personDAO.findPersons(criteria);
	}

}
