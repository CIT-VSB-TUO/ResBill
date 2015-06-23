package cz.vsb.resbill.web.persons;

import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.exception.PersonServiceException;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from persons/personEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/persons/edit")
@SessionAttributes("person")
public class PersonEditController extends AbstractPersonController {

	private static final Logger log = LoggerFactory.getLogger(PersonEditController.class);

	private static final String PERSON_MODEL_KEY = "person";

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private void loadPerson(Integer personId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested personId=" + personId);
		}
		Person person = null;
		try {
			if (personId != null) {
				person = personService.findPerson(personId);
			} else {
				person = new Person();
			}
			model.addAttribute(PERSON_MODEL_KEY, person);
		} catch (Exception e) {
			log.error("Cannot load person with id: " + personId, e);

			model.addAttribute(PERSON_MODEL_KEY, person);
			WebUtils.addGlobalError(model, PERSON_MODEL_KEY, "error.load.person");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded person: " + person);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link Person} entity with the key "person" into a model.
	 * 
	 * @param personId
	 *          key of a {@link Person} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "personId", required = false) Integer personId, ModelMap model) {
		loadPerson(personId, model);

		return "persons/personEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link Person} entity.
	 * 
	 * @param person
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(PERSON_MODEL_KEY) Person person, BindingResult bindingResult) {
		if (log.isDebugEnabled()) {
			log.debug("Person to save: " + person);
		}
		if (!bindingResult.hasErrors()) {
			try {
				person = personService.savePerson(person);
				if (log.isDebugEnabled()) {
					log.debug("Saved person: " + person);
				}
				return "redirect:/persons";
			} catch (PersonServiceException e) {
				switch (e.getReason()) {
				case NONUNIQUE_EMAIL:
					bindingResult.reject("error.save.person.constraint.unique.email");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.person");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save person: " + person, e);
				bindingResult.reject("error.save.person");
			}
		} else {
			bindingResult.reject("error.save.person.validation");
		}
		return "persons/personEdit";
	}

}
