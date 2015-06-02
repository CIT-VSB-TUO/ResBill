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
import cz.vsb.resbill.criteria.PersonCriteria.OrderBy;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from persons/personList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/persons")
public class PersonListController {

	private static final Logger log = LoggerFactory.getLogger(PersonListController.class);

	private static final String PERSONS_MODEL_KEY = "persons";

	@Inject
	private PersonService personService;

	private void loadPersons(ModelMap model) {
		List<Person> persons = null;
		try {
			PersonCriteria criteria = new PersonCriteria();
			criteria.setOrderBy(OrderBy.EMAIL);
			persons = personService.findPersons(criteria, null, null);
			model.addAttribute(PERSONS_MODEL_KEY, persons);
		} catch (Exception e) {
			log.error("Cannot load list of persons.", e);

			model.addAttribute(PERSONS_MODEL_KEY, persons);
			WebUtils.addGlobalError(model, PERSONS_MODEL_KEY, "error.load.persons");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of persons size: " + (persons != null ? persons.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all persons and binds it with the key "persons" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		loadPersons(model);

		return "persons/personList";
	}
}
