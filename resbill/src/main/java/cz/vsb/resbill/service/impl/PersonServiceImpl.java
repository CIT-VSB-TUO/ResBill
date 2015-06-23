package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;

import cz.vsb.resbill.criteria.ContractPersonCriteria;
import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.dao.ContractPersonDAO;
import cz.vsb.resbill.dao.CustomerDAO;
import cz.vsb.resbill.dao.PersonDAO;
import cz.vsb.resbill.dto.person.PersonDTO;
import cz.vsb.resbill.dto.person.PersonHeaderDTO;
import cz.vsb.resbill.dto.person.PersonOverviewDTO;
import cz.vsb.resbill.exception.PersonServiceException;
import cz.vsb.resbill.exception.PersonServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractPerson;
import cz.vsb.resbill.model.Customer;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link PersonService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class PersonServiceImpl implements PersonService {

	private static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

	@Inject
	private PersonDAO personDAO;

	@Inject
	private CustomerDAO customerDAO;

	@Inject
	private ContractPersonDAO contractPersonDAO;

	@Override
	public Person findPerson(Integer personId) throws ResBillException {
		try {
			return personDAO.findPerson(personId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding Person by id=" + personId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public PersonHeaderDTO findPersonHeaderDTO(Integer personId) throws ResBillException {
		try {
			return new PersonHeaderDTO(findPerson(personId));
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding PersonHeaderDTO by id=" + personId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public PersonOverviewDTO findPersonOverviewDTO(Integer personId) throws ResBillException {
		try {
			return new PersonOverviewDTO(findPerson(personId));
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding PersonOverviewDTO by id=" + personId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<Person> findPersons(PersonCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return personDAO.findPersons(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for Person entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<PersonDTO> findPersonDTOs(PersonCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			List<Person> persons = personDAO.findPersons(criteria, offset, limit);
			List<PersonDTO> dtos = new ArrayList<PersonDTO>(persons.size());
			for (Person person : persons) {
				dtos.add(new PersonDTO(person));
			}
			return dtos;
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for PersonDTOs by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Person savePerson(Person person) throws PersonServiceException, ResBillException {
		try {
			// kontrola jedinecnosti emailu
			PersonCriteria criteria = new PersonCriteria();
			criteria.setEmail(person.getEmail());
			List<Person> persons = personDAO.findPersons(criteria, null, null);
			Person foundPerson = DataAccessUtils.singleResult(persons);
			if (foundPerson != null && !foundPerson.getId().equals(person.getId())) {
				throw new PersonServiceException(Reason.NONUNIQUE_EMAIL);
			}
			return personDAO.savePerson(person);
		} catch (PersonServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving Person entity: " + person, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Person deletePerson(Integer personId) throws PersonServiceException, ResBillException {
		try {
			Person person = personDAO.findPerson(personId);

			// kontrola zavislosti
			// zakaznici
			CustomerCriteria customerCriteria = new CustomerCriteria();
			customerCriteria.setContactPersonId(personId);
			List<Customer> customers = customerDAO.findCustomers(customerCriteria, null, null);
			if (!customers.isEmpty()) {
				throw new PersonServiceException(Reason.CUSTOMERS_CONTACT_PERSON);
			}
			// zodpovednost za kontrakt
			ContractPersonCriteria cpCriteria = new ContractPersonCriteria();
			cpCriteria.setPersonId(personId);
			List<ContractPerson> contractPersons = contractPersonDAO.findContractPersons(cpCriteria, null, null);
			if (!contractPersons.isEmpty()) {
				throw new PersonServiceException(Reason.CONTRACT_RESPONSIBILITY);
			}

			return personDAO.deletePerson(person);
		} catch (PersonServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting Person with id=" + personId, e);
			throw new ResBillException(e);
		}
	}
}
