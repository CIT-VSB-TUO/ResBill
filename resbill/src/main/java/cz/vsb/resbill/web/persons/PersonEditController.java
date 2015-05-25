package cz.vsb.resbill.web.persons;

import java.util.Locale;

import javax.inject.Inject;
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

import cz.vsb.resbill.exception.PersonServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;

/**
 * A controller for handling requests for/from persons/personEdit.html page
 * template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/persons/edit")
public class PersonEditController {

	private static final Logger log = LoggerFactory.getLogger(PersonEditController.class);

	@Inject
	private PersonService personService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private Person getPerson(Integer personId) throws ResBillException {
		if (personId != null) {
			return personService.findPerson(personId);
		} else {
			return new Person();
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link Person} entity with the key
	 * "person" into a model.
	 * 
	 * @param personId
	 *          key of a {@link Person} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "personId", required = false) Integer personId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested personId=" + personId);
		}
		try {
			model.addAttribute("person", getPerson(personId));
			if (log.isDebugEnabled()) {
				log.debug("Edited person: " + model.get("person"));
			}
		} catch (ResBillException e) {
			log.error("Cannot load person with id: " + personId, e);
			// TODO error notification
		}
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
	public String save(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult) {
		if (log.isDebugEnabled()) {
			log.debug("Person to save: " + person);
		}
		if (!bindingResult.hasErrors()) {
			try {
				person = personService.savePerson(person);
				if (log.isDebugEnabled()) {
					log.debug("Saved person: " + person);
				}
			} catch (PersonServiceException e) {
				switch (e.getReason()) {
				case NONUNIQUE_EMAIL:
					bindingResult.reject("error.save.person.constraint.unique.email");
					break;
				default:
					log.warn("Unknown reason: " + e);
					bindingResult.reject("error.save.person");
					break;
				}
				return "persons/personEdit";
			} catch (ResBillException e) {
				log.error("Cannot save person: " + person, e);
				bindingResult.reject("error.save.person");
				return "persons/personEdit";
			}
		} else {
			bindingResult.reject("error.save.person.validation");
			return "persons/personEdit";
		}
		return "redirect:/persons";
	}

	/**
	 * Handle POST requests for deleting {@link Person} entity.
	 * 
	 * @param person
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute("person") Person person, BindingResult bindingResult) {
		if (log.isDebugEnabled()) {
			log.debug("Person to delete: " + person);
		}
		try {
			person = personService.deletePerson(person.getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted person: " + person);
			}
		} catch (PersonServiceException e) {
			// TODO implement
			bindingResult.reject("error.delete.person.constraint.relations");
			return "persons/personEdit";
		} catch (ResBillException e) {
			log.error("Cannot delete person: " + person, e);
			bindingResult.reject("error.delete.person");
			return "persons/personEdit";
		}
		return "redirect:/persons";
	}
}
