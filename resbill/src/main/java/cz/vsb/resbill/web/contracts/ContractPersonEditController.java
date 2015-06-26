package cz.vsb.resbill.web.contracts;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.support.DataAccessUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.criteria.ContractPersonCriteria;
import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.criteria.PersonCriteria.OrderBy;
import cz.vsb.resbill.dto.ContractPersonEditDTO;
import cz.vsb.resbill.exception.ContractPersonServiceException;
import cz.vsb.resbill.model.ContractPerson;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.ContractPersonService;
import cz.vsb.resbill.service.PersonService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from contracts/contractPersonEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/contracts/persons/edit")
@SessionAttributes("contractPersonEditDTO")
public class ContractPersonEditController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractPersonEditController.class);

	private static final String CONTRACT_PERSON_EDIT_DTO_MODEL_KEY = "contractPersonEditDTO";

	@Inject
	private ContractPersonService contractPersonService;

	@Inject
	private PersonService personService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute("persons")
	public List<Person> getPersons() {
		try {
			PersonCriteria criteria = new PersonCriteria();
			criteria.setOrderBy(OrderBy.NAME);
			return personService.findPersons(criteria, null, null);
		} catch (Exception e) {
			log.error("Cannot load persons", e);
			return null;
		}
	}

	private void loadContractPersonEditDTO(Integer personId, Integer contractId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested person.id=" + personId);
		}
		ContractPersonEditDTO cpEditDTO = null;
		try {
			if (personId != null) {
				ContractPersonCriteria cpCriteria = new ContractPersonCriteria();
				cpCriteria.setContractId(contractId);
				cpCriteria.setPersonId(personId);
				List<ContractPerson> cps = contractPersonService.findContractPersons(cpCriteria, null, Integer.valueOf(1));

				cpEditDTO = new ContractPersonEditDTO(DataAccessUtils.singleResult(cps));
			} else {
				ContractPerson ct = new ContractPerson();
				if (contractId != null) {
					ct.setContract(contractService.findContract(contractId));
				}
				cpEditDTO = new ContractPersonEditDTO(ct);
			}
			model.addAttribute(CONTRACT_PERSON_EDIT_DTO_MODEL_KEY, cpEditDTO);
		} catch (Exception e) {
			log.error("Cannot load contract person with person.id: " + personId, e);

			model.addAttribute(CONTRACT_PERSON_EDIT_DTO_MODEL_KEY, cpEditDTO);
			WebUtils.addGlobalError(model, CONTRACT_PERSON_EDIT_DTO_MODEL_KEY, "error.load.contract.person");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded contractPersonEditDTO: " + cpEditDTO);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link ContractPersonEditDTO} entity with the key "contractPersonEditDTO" into a model.
	 * 
	 * @param contractPersonId
	 *          key of a {@link ContractPersonEditDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "personId", required = false) Integer personId, @RequestParam(value = CONTRACT_ID_PARAM_KEY, required = false) Integer contractId, ModelMap model) {
		loadContractPersonEditDTO(personId, contractId, model);

		return "contracts/contractPersonEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link ContractPersonEditDTO} instance.
	 * 
	 * @param contractPersonEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(CONTRACT_PERSON_EDIT_DTO_MODEL_KEY) ContractPersonEditDTO contractPersonEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractPersonEditDTO to save: " + contractPersonEditDTO);
		}
		if (!bindingResult.hasErrors()) {
			try {
				ContractPerson ct = contractPersonEditDTO.getContractPerson();
				if (ct.getId() == null) {
					ct.setPerson(personService.findPerson(contractPersonEditDTO.getPersonId()));
				}
				ct = contractPersonService.saveContractPerson(ct);
				if (log.isDebugEnabled()) {
					log.debug("Saved contract person: " + ct);
				}
				redirectAttributes.addAttribute(CONTRACT_ID_PARAM_KEY, ct.getContract().getId());
				return "redirect:/contracts/overview";
			} catch (ContractPersonServiceException e) {
				switch (e.getReason()) {
				case NONUNIQUE_CONTRACT_PERSON:
					bindingResult.reject("error.save.contract.person.nonunique");
					break;
				case CONTRACT_PERSON_MODIFICATION:
					bindingResult.reject("error.save.contract.person.modified");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.contract.person");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save ContractPersonEditDTO: " + contractPersonEditDTO, e);
				bindingResult.reject("error.save.contract.person");
			}
		} else {
			bindingResult.reject("error.save.contract.person.validation");
		}
		return "contracts/contractPersonEdit";
	}

	/**
	 * Handle POST requests for deleting {@link ContractPersonEditDTO} instance.
	 * 
	 * @param contractPersonEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute(CONTRACT_PERSON_EDIT_DTO_MODEL_KEY) ContractPersonEditDTO contractPersonEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractPersonEditDTO to delete: " + contractPersonEditDTO);
		}
		try {
			ContractPerson ct = contractPersonService.deleteContractPerson(contractPersonEditDTO.getContractPerson().getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted ContractPerson: " + ct);
			}
			redirectAttributes.addAttribute(CONTRACT_ID_PARAM_KEY, ct.getContract().getId());
			return "redirect:/contracts/overview";
		} catch (ContractPersonServiceException e) {
			log.warn("Unsupported cause: " + e);
			bindingResult.reject("error.delete.contract.person");
		} catch (Exception e) {
			log.error("Cannot delete ContractPersonEditDTO: " + contractPersonEditDTO, e);
			bindingResult.reject("error.delete.contract.person");
		}
		return "contracts/contractPersonEdit";
	}
}
