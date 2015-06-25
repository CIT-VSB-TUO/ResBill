/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.contracts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
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

import cz.vsb.resbill.criteria.CustomerCriteria;
import cz.vsb.resbill.criteria.CustomerCriteria.OrderBy;
import cz.vsb.resbill.dto.contract.ContractEditDTO;
import cz.vsb.resbill.exception.ContractServiceException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Customer;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.service.CustomerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/contracts/edit")
@SessionAttributes("contractEditDTO")
public class ContractEditController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractEditController.class);

	private static final String CONTRACT_EDIT_DTO_MODEL_KEY = "contractEditDTO";

	@Inject
	private CustomerService customerService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute("customers")
	public List<Customer> getCustomers() {
		try {
			CustomerCriteria criteria = new CustomerCriteria();
			criteria.setOrderBy(OrderBy.NAME);
			return customerService.findCustomers(criteria, null, null);
		} catch (Exception e) {
			log.error("Cannot load customers", e);
			return null;
		}
	}

	private void loadContractEditDTO(Integer contractId, Integer customerId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested contract.id=" + contractId);
		}
		ContractEditDTO editDTO = null;
		try {
			if (contractId != null) {
				editDTO = new ContractEditDTO(contractService.findContract(contractId));
			} else {
				Contract contract = new Contract();
				contract.setPeriod(new Period());
				contract.getPeriod().setBeginDate(DateUtils.truncate(new Date(), Calendar.DATE));
				editDTO = new ContractEditDTO(contract);
				if (customerId != null) {
					editDTO.setCustomerId(customerId);
					editDTO.setCustomerEditable(false);
				}
			}
			model.addAttribute(CONTRACT_EDIT_DTO_MODEL_KEY, editDTO);
		} catch (Exception e) {
			log.error("Cannot load contract with id: " + contractId, e);

			model.addAttribute(CONTRACT_EDIT_DTO_MODEL_KEY, editDTO);
			WebUtils.addGlobalError(model, CONTRACT_EDIT_DTO_MODEL_KEY, "error.load.contract");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded contractEditDTO: " + editDTO);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String view(@RequestParam(value = "contractId", required = false) Integer contractId, @RequestParam(value = "customerId", required = false) Integer customerId, ModelMap model) {
		loadContractEditDTO(contractId, customerId, model);

		return "contracts/contractEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link ContractEditDTO} instance.
	 * 
	 * @param contractEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(CONTRACT_EDIT_DTO_MODEL_KEY) ContractEditDTO contractEditDTO, BindingResult bindingResult) {
		if (log.isDebugEnabled()) {
			log.debug("ContractEditDTO to save: " + contractEditDTO);
		}
		if (!bindingResult.hasErrors()) {
			try {
				Contract contract = contractEditDTO.getContract();
				if (contract.getId() == null) {
					contract.setCustomer(customerService.findCustomer(contractEditDTO.getCustomerId()));
				}
				contract = contractService.saveContract(contract);
				if (log.isDebugEnabled()) {
					log.debug("Saved contract: " + contract);
				}
				return "redirect:/contracts";
			} catch (ContractServiceException e) {
				switch (e.getReason()) {
				case CUSTOMER_MODIFICATION:
					bindingResult.reject("error.save.contract.customer.modified");
					break;
				case SERVER_ASSOCIATION_EXCLUSION:
					bindingResult.reject("error.save.contract.server.exclusion");
					break;
				case CONTRACT_INVOICE_TYPE_INVALID_PERIOD:
					bindingResult.reject("error.save.contract.invoiceType.invalid.period");
					break;
				case CONTRACT_TARIFF_INVALID_PERIOD:
					bindingResult.reject("error.save.contract.tariff.invalid.period");
					break;
				case CONTRACT_TARIFF_INVOICE_DAILY_USAGE_UNCOVERED:
					bindingResult.reject("error.save.contract.tariff.invoiced");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.contract");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save ContractEditDTO: " + contractEditDTO, e);
				bindingResult.reject("error.save.contract");
			}
		} else {
			bindingResult.reject("error.save.contract.validation");
		}
		return "contracts/contractEdit";
	}

	/**
	 * Handle POST requests for deleting {@link ContractEditDTO} instance.
	 * 
	 * @param contractEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute(CONTRACT_EDIT_DTO_MODEL_KEY) ContractEditDTO contractEditDTO, BindingResult bindingResult) {
		if (log.isDebugEnabled()) {
			log.debug("ContractEditDTO to delete: " + contractEditDTO);
		}
		try {
			Contract cs = contractService.deleteContract(contractEditDTO.getContract().getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted Contract: " + cs);
			}
			return "redirect:/contracts";
		} catch (ContractServiceException e) {
			switch (e.getReason()) {
			case TRANSACTION_EXISTENCE:
				bindingResult.reject("error.delete.contract.transaction.exists");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				bindingResult.reject("error.delete.contract");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete ContractEditDTO: " + contractEditDTO, e);
			bindingResult.reject("error.delete.contract");
		}
		return "contracts/contractEdit";
	}
}
