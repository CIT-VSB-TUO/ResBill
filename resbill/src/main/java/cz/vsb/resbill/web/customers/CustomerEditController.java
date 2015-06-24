package cz.vsb.resbill.web.customers;

import java.util.List;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.criteria.PersonCriteria.OrderBy;
import cz.vsb.resbill.dto.CustomerEditDTO;
import cz.vsb.resbill.exception.CustomerServiceException;
import cz.vsb.resbill.model.Customer;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.PersonService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from customers/customerEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/customers/edit")
@SessionAttributes("customerEditDTO")
public class CustomerEditController extends AbstractCustomerController {

	private static final Logger log = LoggerFactory.getLogger(CustomerEditController.class);

	private static final String CUSTOMER_EDIT_DTO_MODEL_KEY = "customerEditDTO";

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
			criteria.setOrderBy(OrderBy.EMAIL);
			return personService.findPersons(criteria, null, null);
		} catch (Exception e) {
			log.error("Cannot load persons", e);
			return null;
		}
	}

	private void loadCustomerEditDTO(Integer customerId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested customerId=" + customerId);
		}
		CustomerEditDTO customerEditDTO = null;
		try {
			if (customerId != null) {
				customerEditDTO = new CustomerEditDTO(customerService.findCustomer(customerId));
			} else {
				customerEditDTO = new CustomerEditDTO(new Customer());
			}
			model.addAttribute(CUSTOMER_EDIT_DTO_MODEL_KEY, customerEditDTO);
		} catch (Exception e) {
			log.error("Cannot load customer with id: " + customerId, e);

			model.addAttribute(CUSTOMER_EDIT_DTO_MODEL_KEY, customerEditDTO);
			WebUtils.addGlobalError(model, CUSTOMER_EDIT_DTO_MODEL_KEY, "error.load.customer");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded CustomerEditDTO: " + customerEditDTO);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link CustomerEditDTO} entity with the key "customerEditDTO" into a model.
	 * 
	 * @param customerId
	 *          key of a {@link CustomerEditDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "customerId", required = false) Integer customerId, ModelMap model) {
		loadCustomerEditDTO(customerId, model);

		return "customers/customerEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link CustomerEditDTO} instance.
	 * 
	 * @param customer
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(CUSTOMER_EDIT_DTO_MODEL_KEY) CustomerEditDTO customerEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("CustomerEditDTO to save: " + customerEditDTO);
		}
		if (!bindingResult.hasErrors()) {
			try {
				Customer customer = customerEditDTO.getCustomer();
				customer.setContactPerson(personService.findPerson(customerEditDTO.getContactPersonId()));
				customer = customerService.saveCustomer(customer);
				if (log.isDebugEnabled()) {
					log.debug("Saved customer: " + customer);
				}
				redirectAttributes.addAttribute("customerId", customer.getId());
				return "redirect:/customers/overview";
			} catch (CustomerServiceException e) {
				switch (e.getReason()) {
				case NONUNIQUE_NAME:
					bindingResult.reject("error.save.customer.constraint.unique.name");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.customer");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save CustomerEditDTO: " + customerEditDTO, e);
				bindingResult.reject("error.save.customer");
			}
		} else {
			bindingResult.reject("error.save.customer.validation");
		}
		return "customers/customerEdit";
	}

}
