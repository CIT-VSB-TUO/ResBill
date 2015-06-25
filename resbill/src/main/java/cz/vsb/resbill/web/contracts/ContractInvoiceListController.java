/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.contracts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.criteria.InvoiceCreateCriteria;
import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.criteria.InvoiceExportCriteria;
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.dto.InvoiceDTO;
import cz.vsb.resbill.dto.InvoiceExportResultDTO;
import cz.vsb.resbill.dto.contract.ContractHeaderDTO;
import cz.vsb.resbill.service.InvoiceService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Controller
@RequestMapping("/contracts/invoices")
public class ContractInvoiceListController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractInvoiceListController.class);

	public static final String MODEL_OBJECT_KEY_INVOICES = "invoices";

	public static final String MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO = "invoicesCreateResultsDTO";

	public static final String MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_SHOW = "showInvoicesCreateResults";

	public static final String MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_DTO = "invoicesExportResultsDTO";

	public static final String MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_SHOW = "showInvoicesExportResults";

	@Inject
	private InvoiceService invoiceService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/yyyy"), true));
	}

	/**
	 * 
	 * @param model
	 * @param msgKey
	 */
	protected static void addGlobalError(ModelMap model, String msgKey) {
		WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICES, msgKey);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(@RequestParam(value = CONTRACT_ID_PARAM_KEY, required = true) Integer contractId, ModelMap model) {
		loadInvoiceDTOs(contractId, model);

		return "contracts/contractInvoiceList";
	}

	/**
	 * 
	 * @return
	 */
	protected List<InvoiceDTO> loadInvoiceDTOs(Integer contractId, ModelMap model) {

		List<InvoiceDTO> dtos = null;

		try {
			List<InvoiceCriteria.OrderBy> orderBy = new ArrayList<InvoiceCriteria.OrderBy>();
			orderBy.add(InvoiceCriteria.OrderBy.ORDER_DESC);

			InvoiceCriteria criteria = new InvoiceCriteria();
			criteria.setContractId(contractId);
			criteria.setOrderBy(orderBy);

			dtos = invoiceService.findInvoiceDTOs(criteria, null, null);

			model.addAttribute(MODEL_OBJECT_KEY_INVOICES, dtos);
		} catch (Exception exc) {
			log.error("Cannot load Invoices.", exc);

			dtos = null;

			model.addAttribute(MODEL_OBJECT_KEY_INVOICES, dtos);
			addGlobalError(model, "error.load.invoices");
		}

		return dtos;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "createInvoices")
	public String createInvoices(@RequestParam(value = "month", required = true) Date month, ModelMap model) {
		ContractHeaderDTO contractHeaderDTO = (ContractHeaderDTO) model.get(CONTRACT_HEADER_DTO_MODEL_KEY);

		model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, null);
		model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_SHOW, false);

		try {
			if (month != null) {
				InvoiceCreateCriteria criteria = new InvoiceCreateCriteria();
				criteria.setContractId(contractHeaderDTO.getContractId());
				criteria.setMonth(month);

				InvoiceCreateResultDTO resultDTO = invoiceService.createInvoices(criteria);
				model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, resultDTO);

				model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_SHOW, true);
			} else {
				WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, "error.save.invoice.create.noMonth");
			}
		} catch (Exception exc) {
			log.error("Cannot createInvoices for contract with ID: " + contractHeaderDTO.getContractId() + ".", exc);
			WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, "error.save.invoice.create");
		}

		return view(contractHeaderDTO.getContractId(), model);

	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "exportInvoices")
	public String exportInvoices(@RequestParam(value = "expMonth", required = true) Date expMonth, ModelMap model) {
		ContractHeaderDTO contractHeaderDTO = (ContractHeaderDTO) model.get(CONTRACT_HEADER_DTO_MODEL_KEY);

		model.addAttribute(MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_DTO, null);
		model.addAttribute(MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_SHOW, false);

		try {
			if (expMonth != null) {
				InvoiceExportCriteria criteria = new InvoiceExportCriteria();
				criteria.setContractId(contractHeaderDTO.getContractId());
				criteria.setMonth(expMonth);

				InvoiceExportResultDTO resultDTO = invoiceService.exportInvoices(criteria);
				model.addAttribute(MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_DTO, resultDTO);

				model.addAttribute(MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_SHOW, true);
			} else {
				WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_DTO, "error.save.invoice.export.noMonth");
			}
		} catch (Exception exc) {
			log.error("Cannot exportInvoices for contract with ID: " + contractHeaderDTO.getContractId() + ".", exc);
			WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICES_EXPORT_RESULTS_DTO, "error.save.invoice.export");
		}

		return view(contractHeaderDTO.getContractId(), model);
	}
}
