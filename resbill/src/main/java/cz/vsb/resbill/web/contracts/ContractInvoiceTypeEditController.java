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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.criteria.InvoiceTypeCriteria.OrderBy;
import cz.vsb.resbill.dto.ContractInvoiceTypeEditDTO;
import cz.vsb.resbill.exception.ContractInvoiceTypeServiceException;
import cz.vsb.resbill.model.ContractInvoiceType;
import cz.vsb.resbill.model.InvoiceType;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.service.ContractInvoiceTypeService;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.service.InvoiceTypeService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from contracts/contractInvoiceTypeEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/contracts/invoiceTypes/edit")
@SessionAttributes({ "contractInvoiceTypeEditDTO", "contractEditDTO" })
public class ContractInvoiceTypeEditController {

	private static final Logger log = LoggerFactory.getLogger(ContractInvoiceTypeEditController.class);

	private static final String CONTRACT_INVOICE_TYPE_EDIT_DTO_MODEL_KEY = "contractInvoiceTypeEditDTO";

	@Inject
	private ContractInvoiceTypeService contractInvoiceTypeService;

	@Inject
	private InvoiceTypeService invoiceTypeService;

	@Inject
	private ContractService contractService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute("invoiceTypes")
	public List<InvoiceType> getInvoiceTypes() {
		try {
			InvoiceTypeCriteria criteria = new InvoiceTypeCriteria();
			criteria.setOrderBy(OrderBy.ID);
			return invoiceTypeService.findInvoiceTypes(criteria, null, null);
		} catch (Exception e) {
			log.error("Cannot load invoiceTypes", e);
			return null;
		}
	}

	private void loadContractInvoiceTypeEditDTO(Integer contractInvoiceTypeId, Integer contractId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested contractInvoiceType.id=" + contractInvoiceTypeId);
		}
		ContractInvoiceTypeEditDTO citEditDTO = null;
		try {
			if (contractInvoiceTypeId != null) {
				citEditDTO = new ContractInvoiceTypeEditDTO(contractInvoiceTypeService.findContractInvoiceType(contractInvoiceTypeId));
			} else {
				ContractInvoiceType cs = new ContractInvoiceType();
				cs.setPeriod(new Period());
				cs.getPeriod().setBeginDate(DateUtils.truncate(new Date(), Calendar.DATE));
				if (contractId != null) {
					cs.setContract(contractService.findContract(contractId));
				}
				citEditDTO = new ContractInvoiceTypeEditDTO(cs);
			}
			model.addAttribute(CONTRACT_INVOICE_TYPE_EDIT_DTO_MODEL_KEY, citEditDTO);
		} catch (Exception e) {
			log.error("Cannot load contract invoiceType with id: " + contractInvoiceTypeId, e);

			model.addAttribute(CONTRACT_INVOICE_TYPE_EDIT_DTO_MODEL_KEY, citEditDTO);
			WebUtils.addGlobalError(model, CONTRACT_INVOICE_TYPE_EDIT_DTO_MODEL_KEY, "error.load.contract.invoiceType");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded contractInvoiceTypeEditDTO: " + citEditDTO);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link ContractInvoiceTypeEditDTO} entity with the key "contractInvoiceTypeEditDTO" into a model.
	 * 
	 * @param contractInvoiceTypeId
	 *          key of a {@link ContractInvoiceTypeEditDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "contractInvoiceTypeId", required = false) Integer contractInvoiceTypeId, @RequestParam(value = "contractId", required = false) Integer contractId,
			ModelMap model) {
		loadContractInvoiceTypeEditDTO(contractInvoiceTypeId, contractId, model);

		return "contracts/contractInvoiceTypeEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link ContractInvoiceTypeEditDTO} instance.
	 * 
	 * @param contractInvoiceTypeEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(CONTRACT_INVOICE_TYPE_EDIT_DTO_MODEL_KEY) ContractInvoiceTypeEditDTO contractInvoiceTypeEditDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractInvoiceTypeEditDTO to save: " + contractInvoiceTypeEditDTO);
		}
		if (!bindingResult.hasErrors()) {
			try {
				ContractInvoiceType cit = contractInvoiceTypeEditDTO.getContractInvoiceType();
				if (cit.getId() == null) {
					cit.setInvoiceType(invoiceTypeService.findInvoiceType(contractInvoiceTypeEditDTO.getInvoiceTypeId()));
				}
				cit = contractInvoiceTypeService.saveContractInvoiceType(cit);
				if (log.isDebugEnabled()) {
					log.debug("Saved contract invoiceType: " + cit);
				}
				redirectAttributes.addAttribute("contractId", cit.getContract().getId());
				return "redirect:/contracts/invoiceTypes";
			} catch (ContractInvoiceTypeServiceException e) {
				switch (e.getReason()) {
				// TODO osetreni vyjimek
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.contract.invoiceType");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save ContractInvoiceTypeEditDTO: " + contractInvoiceTypeEditDTO, e);
				bindingResult.reject("error.save.contract.invoiceType");
			}
		} else {
			bindingResult.reject("error.save.contract.invoiceType.validation");
		}
		return "contracts/contractInvoiceTypeEdit";
	}

	/**
	 * Handle POST requests for deleting {@link ContractInvoiceTypeEditDTO} instance.
	 * 
	 * @param contractInvoiceTypeEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute(CONTRACT_INVOICE_TYPE_EDIT_DTO_MODEL_KEY) ContractInvoiceTypeEditDTO contractInvoiceTypeEditDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractInvoiceTypeEditDTO to delete: " + contractInvoiceTypeEditDTO);
		}
		try {
			ContractInvoiceType ct = contractInvoiceTypeService.deleteContractInvoiceType(contractInvoiceTypeEditDTO.getContractInvoiceType().getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted ContractInvoiceType: " + ct);
			}
			redirectAttributes.addAttribute("contractId", ct.getContract().getId());
			return "redirect:/contracts/invoiceTypes";
		} catch (ContractInvoiceTypeServiceException e) {
			switch (e.getReason()) {
			// TODO osetreni vyjimek
			default:
				log.warn("Unsupported reason: " + e);
				bindingResult.reject("error.delete.contract.invoiceType");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete ContractInvoiceTypeEditDTO: " + contractInvoiceTypeEditDTO, e);
			bindingResult.reject("error.delete.contract.invoiceType");
		}
		return "contracts/contractInvoiceTypeEdit";
	}
}
