package cz.vsb.resbill.web.customers;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.criteria.CustomerCriteria.OrderBy;
import cz.vsb.resbill.dto.customer.CustomerListDTO;
import cz.vsb.resbill.service.CustomerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from customers/customerList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/customers")
public class CustomerListController {

	private static final Logger log = LoggerFactory.getLogger(CustomerListController.class);

	private static final String CUSTOMERS_MODEL_KEY = "customerListDTOs";

	@Inject
	private CustomerService customerService;

	private void loadCustomerListDTOs(ModelMap model) {
		List<CustomerListDTO> customerListDTOs = null;
		try {
			CustomerCriteria criteria = new CustomerCriteria();
			criteria.setOrderBy(OrderBy.NAME);
			customerListDTOs = customerService.findCustomerListDTOs(criteria, null, null);
			model.addAttribute(CUSTOMERS_MODEL_KEY, customerListDTOs);
		} catch (Exception e) {
			log.error("Cannot load list of customerListDTOs.", e);

			model.addAttribute(CUSTOMERS_MODEL_KEY, customerListDTOs);
			WebUtils.addGlobalError(model, CUSTOMERS_MODEL_KEY, "error.load.customers");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of customerListDTOs size: " + (customerListDTOs != null ? customerListDTOs.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all customers and binds it with the key "customerListDTOs" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		loadCustomerListDTOs(model);

		return "customers/customerList";
	}
}
