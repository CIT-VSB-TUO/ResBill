package cz.vsb.resbill.web.persons;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.dto.person.PersonHeaderDTO;
import cz.vsb.resbill.service.PersonService;

public abstract class AbstractPersonController {

	private static final Logger log = LoggerFactory.getLogger(AbstractPersonController.class);

	protected static final String PERSON_HEADER_DTO_MODEL_KEY = "personHeaderDTO";

	protected static final String PERSON_ID_PARAM_KEY = "personId";

	@Inject
	protected PersonService personService;

	@ModelAttribute(PERSON_HEADER_DTO_MODEL_KEY)
	public PersonHeaderDTO getPersonHeaderDTO(@RequestParam(value = PERSON_ID_PARAM_KEY, required = false) Integer personId) {
		if (personId != null) {
			try {
				return personService.findPersonHeaderDTO(personId);
			} catch (Exception e) {
				log.error("Cannot load personHeaderDTO with id: " + personId, e);
			}
		}
		return null;
	}
}
