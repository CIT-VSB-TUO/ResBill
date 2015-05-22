package cz.vsb.resbill.web.persons;

import java.util.Locale;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.exception.ConstraintViolationException;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;

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

	@ModelAttribute("person")
	public Person getPerson(@RequestParam(value = "personId", required = false) Integer personId) {
		if (log.isDebugEnabled()) {
			log.debug("Searched personId=" + personId);
		}
		Person person;
		if (personId != null) {
			person = personService.fidPerson(personId);
		} else {
			person = new Person();
		}
		if (log.isDebugEnabled()) {
			log.debug("Returned person: " + person);
		}
		return person;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view() {
		return "persons/personEdit";
	}

	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult) {
		if (log.isDebugEnabled()) {
			log.debug("Person to save: " + person);
		}
		if (!bindingResult.hasErrors()) {
			try {
				personService.savePerson(person);
			} catch (ConstraintViolationException e) {
				switch (e.getReason()) {
				case UNIQUE_KEY:
					bindingResult.reject("error.save.person.constraint.unique.email");
					break;
				default:
					log.warn("Unknown reason: " + e);
					bindingResult.reject("error.save.person");
					break;
				}
				return "persons/personEdit";
			} catch (Exception e) {
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

	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute("person") Person person, BindingResult bindingResult) {
		if (log.isDebugEnabled()) {
			log.debug("Person to delete: " + person);
		}
		try {
			person = personService.deletePerson(person.getId());
		} catch (PersistenceException e) {
			bindingResult.reject("error.delete.person.constraint.relations");
			return "persons/personEdit";
		} catch (Exception e) {
			log.error("Cannot delete person: " + person, e);
			bindingResult.reject("error.delete.person");
			return "persons/personEdit";
		}
		return "redirect:/persons";
	}
}
