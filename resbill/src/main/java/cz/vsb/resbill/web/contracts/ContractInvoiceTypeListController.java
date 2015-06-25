package cz.vsb.resbill.web.contracts;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.criteria.ContractInvoiceTypeCriteria;
import cz.vsb.resbill.criteria.ContractInvoiceTypeCriteria.OrderBy;
import cz.vsb.resbill.model.ContractInvoiceType;
import cz.vsb.resbill.service.ContractInvoiceTypeService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from contracts/contractInoiceTypeList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/contracts/invoiceTypes")
public class ContractInvoiceTypeListController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractInvoiceTypeListController.class);

	private static final String CONTRACT_INVOICE_TYPES_MODEL_KEY = "contractInvoiceTypes";

	@Inject
	private ContractInvoiceTypeService contractInvoiceTypeService;

	private void loadContractInvoiceTypes(Integer contractId, ModelMap model) {
		List<ContractInvoiceType> cits = null;
		try {
			ContractInvoiceTypeCriteria criteria = new ContractInvoiceTypeCriteria();
			criteria.setContractId(contractId);
			criteria.setFetchInvoiceType(true);
			criteria.setOrderBy(OrderBy.PERIOD_ASC);
			cits = contractInvoiceTypeService.findContractInvoiceTypes(criteria, null, null);
			model.addAttribute(CONTRACT_INVOICE_TYPES_MODEL_KEY, cits);
		} catch (Exception e) {
			log.error("Cannot load list of contract invoiceTypes.", e);

			model.addAttribute(CONTRACT_INVOICE_TYPES_MODEL_KEY, cits);
			WebUtils.addGlobalError(model, CONTRACT_INVOICE_TYPES_MODEL_KEY, "error.load.contract.invoiceTypes");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of contract invoiceTypes size: " + (cits != null ? cits.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all invoiceTypes associated to contract and binds it with the key "contractInvoiceTypes" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(@RequestParam(value = CONTRACT_ID_PARAM_KEY, required = true) Integer contractId, ModelMap model) {
		loadContractInvoiceTypes(contractId, model);

		return "contracts/contractInvoiceTypeList";
	}

}
