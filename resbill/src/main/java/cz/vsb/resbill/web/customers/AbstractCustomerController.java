package cz.vsb.resbill.web.customers;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.dto.customer.CustomerHeaderDTO;
import cz.vsb.resbill.service.CustomerService;

public abstract class AbstractCustomerController {

	private static final Logger log = LoggerFactory.getLogger(AbstractCustomerController.class);

	protected static final String TARIFF_HEADER_DTO_MODEL_KEY = "customerHeaderDTO";

	protected static final String CUSTOMER_ID_PARAM_KEY = "customerId";

	@Inject
	protected CustomerService customerService;

	@ModelAttribute(TARIFF_HEADER_DTO_MODEL_KEY)
	public CustomerHeaderDTO getCustomerHeaderDTO(@RequestParam(value = CUSTOMER_ID_PARAM_KEY, required = false) Integer customerId) {
		if (customerId != null) {
			try {
				return customerService.findCustomerHeaderDTO(customerId);
			} catch (Exception e) {
				log.error("Cannot load CustomerHeaderDTO with id: " + customerId, e);
			}
		}
		return null;
	}
}
