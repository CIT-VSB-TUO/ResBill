package cz.vsb.resbill.web.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.dto.customer.CustomerOverviewDTO;
import cz.vsb.resbill.exception.CustomerServiceException;
import cz.vsb.resbill.model.Customer;
import cz.vsb.resbill.util.WebUtils;

@Controller
@RequestMapping("/customers/overview")
@SessionAttributes("customerOverviewDTO")
public class CustomerOverviewController extends AbstractCustomerController {

	private static final Logger log = LoggerFactory.getLogger(CustomerOverviewController.class);

	private static final String CUSTOMER_OVERVIEW_DTO_MODEL_KEY = "customerOverviewDTO";

	private void loadCustomerOverviewDTO(Integer customerId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested customerId=" + customerId);
		}
		CustomerOverviewDTO dto = null;
		try {
			dto = customerService.findCustomerOverviewDTO(customerId);
			model.addAttribute(CUSTOMER_OVERVIEW_DTO_MODEL_KEY, dto);
		} catch (Exception e) {
			log.error("Cannot load CustomerOverviewDTO with id: " + customerId, e);

			model.addAttribute(CUSTOMER_OVERVIEW_DTO_MODEL_KEY, dto);
			WebUtils.addGlobalError(model, CUSTOMER_OVERVIEW_DTO_MODEL_KEY, "error.load.customer");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded customer: " + dto);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "customerId", required = true) Integer customerId, ModelMap model) {
		loadCustomerOverviewDTO(customerId, model);

		return "customers/customerOverview";
	}

	/**
	 * Handle GET requests for deleting {@link Customer} instance.
	 * 
	 * @param customer
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam(value = "customerId", required = true) Integer customerId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Customer.id to delete: " + customerId);
		}
		try {
			Customer customer = customerService.deleteCustomer(customerId);
			if (log.isDebugEnabled()) {
				log.debug("Deleted customer: " + customer);
			}
			return "redirect:/customers";
		} catch (CustomerServiceException e) {
			switch (e.getReason()) {
			case CONTRACT_EXISTENCE:
				WebUtils.addGlobalError(model, CUSTOMER_OVERVIEW_DTO_MODEL_KEY, "error.delete.customer.contracts.exist");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				WebUtils.addGlobalError(model, CUSTOMER_OVERVIEW_DTO_MODEL_KEY, "error.delete.customer");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete Customer with id=" + customerId, e);
			WebUtils.addGlobalError(model, CUSTOMER_OVERVIEW_DTO_MODEL_KEY, "error.delete.customer");
		}
		return "customers/customerOverview";
	}
}
