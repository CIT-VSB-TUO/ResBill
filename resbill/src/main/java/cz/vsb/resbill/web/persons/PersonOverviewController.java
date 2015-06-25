package cz.vsb.resbill.web.persons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.dto.person.PersonOverviewDTO;
import cz.vsb.resbill.exception.PersonServiceException;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.util.WebUtils;

@Controller
@RequestMapping("/persons/overview")
@SessionAttributes("personOverviewDTO")
public class PersonOverviewController extends AbstractPersonController {

	private static final Logger log = LoggerFactory.getLogger(PersonOverviewController.class);

	private static final String PERSON_OVERVIEW_DTO_MODEL_KEY = "personOverviewDTO";

	private void loadPersonOverviewDTO(Integer personId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested personId=" + personId);
		}
		PersonOverviewDTO dto = null;
		try {
			dto = personService.findPersonOverviewDTO(personId);
			model.addAttribute(PERSON_OVERVIEW_DTO_MODEL_KEY, dto);
		} catch (Exception e) {
			log.error("Cannot load PersonOverviewDTO with id: " + personId, e);

			model.addAttribute(PERSON_OVERVIEW_DTO_MODEL_KEY, dto);
			WebUtils.addGlobalError(model, PERSON_OVERVIEW_DTO_MODEL_KEY, "error.load.person");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded person: " + dto);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = PERSON_ID_PARAM_KEY, required = true) Integer personId, ModelMap model) {
		loadPersonOverviewDTO(personId, model);

		return "persons/personOverview";
	}

	/**
	 * Handle GET requests for deleting {@link Person} entity.
	 * 
	 * @param person
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam(value = PERSON_ID_PARAM_KEY, required = true) Integer personId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Person.id to delete: " + personId);
		}
		try {
			Person person = personService.deletePerson(personId);
			if (log.isDebugEnabled()) {
				log.debug("Deleted person: " + person);
			}
			return "redirect:/persons";
		} catch (PersonServiceException e) {
			switch (e.getReason()) {
			case CUSTOMERS_CONTACT_PERSON:
				WebUtils.addGlobalError(model, PERSON_OVERVIEW_DTO_MODEL_KEY, "error.delete.person.customers.contact");
				break;
			case CONTRACT_RESPONSIBILITY:
				WebUtils.addGlobalError(model, PERSON_OVERVIEW_DTO_MODEL_KEY, "error.delete.person.contract.responsibility");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				WebUtils.addGlobalError(model, PERSON_OVERVIEW_DTO_MODEL_KEY, "error.delete.person");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete person with id: " + personId, e);
			WebUtils.addGlobalError(model, PERSON_OVERVIEW_DTO_MODEL_KEY, "error.delete.person");
		}
		return "persons/personOverview";
	}
}
