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
import cz.vsb.resbill.dto.person.PersonDTO;
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

	private static final String PERSONS_MODEL_KEY = "personDTOs";

	@Inject
	private PersonService personService;

	private void loadPersonDTOs(ModelMap model) {
		List<PersonDTO> personDTOs = null;
		try {
			PersonCriteria criteria = new PersonCriteria();
			criteria.setOrderBy(OrderBy.EMAIL);
			personDTOs = personService.findPersonDTOs(criteria, null, null);
			model.addAttribute(PERSONS_MODEL_KEY, personDTOs);
		} catch (Exception e) {
			log.error("Cannot load list of persons.", e);

			model.addAttribute(PERSONS_MODEL_KEY, personDTOs);
			WebUtils.addGlobalError(model, PERSONS_MODEL_KEY, "error.load.persons");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of persons size: " + (personDTOs != null ? personDTOs.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all persons and binds it with the key "personDTOs" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		loadPersonDTOs(model);

		return "persons/personList";
	}
}
