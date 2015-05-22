package cz.vsb.resbill.web.persons;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;

@Controller
@RequestMapping("/persons")
public class PersonListController {

	@Inject
	private PersonService personService;

	@ModelAttribute("persons")
	public List<Person> getPersons() {
		return personService.findPersons(new PersonCriteria());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String view() {
		return "persons/personList";
	}
}
