package cz.vsb.resbill.web.persons;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;

/**
 * A controller for handling requests for/from persons/personList.html page
 * template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/persons")
public class PersonListController {

	private static final Logger log = LoggerFactory.getLogger(PersonListController.class);

	@Inject
	private PersonService personService;

	private List<Person> getPersons() throws ResBillException {
		return personService.findPersons(new PersonCriteria(), null, null);
	}

	/**
	 * Handles all GET requests. Loads a list of all persons and binds it with the
	 * key "persons" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		try {
			model.addAttribute("persons", getPersons());
		} catch (ResBillException e) {
			log.error("Cannot load list of persons.", e);
			// TODO error handle
		}
		return "persons/personList";
	}
}
