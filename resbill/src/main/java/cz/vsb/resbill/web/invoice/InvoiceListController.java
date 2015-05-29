/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.invoice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import cz.vsb.resbill.dto.DailyImportAllReportsResultDTO;
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.service.InvoiceService;
import cz.vsb.resbill.util.DateFormatter;
import cz.vsb.resbill.util.PeriodLimitedEntityValidator;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceListController {

	private static final Logger log = LoggerFactory.getLogger(InvoiceListController.class);

	public static final String MODEL_OBJECT_KEY_INVOICES = "invoices";

	public static final String MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO = "invoicesCreateResultsDTO";

	public static final String MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_SHOW = "showInvoicesCreateResults";

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
	public String view(ModelMap model) {
		loadInvoices(model);

		return "invoice/invoiceList";
	}

	/**
	 * 
	 * @return
	 */
	protected List<Invoice> loadInvoices(ModelMap model) {

		List<Invoice> invoices = null;

		try {
			InvoiceCriteria criteria = new InvoiceCriteria();
			criteria.setOrderBy(InvoiceCriteria.OrderBy.DECISIVE_DATE);
			criteria.setOrderDesc(true);

			invoices = invoiceService.findInvoices(criteria, null, null);

			model.addAttribute(MODEL_OBJECT_KEY_INVOICES, invoices);
		} catch (Exception exc) {
			log.error("Cannot load Invoices.", exc);

			invoices = null;

			model.addAttribute(MODEL_OBJECT_KEY_INVOICES, invoices);
			addGlobalError(model, "error.load.invoices");
		}

		return invoices;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "createInvoices")
	public String createInvoices(@RequestParam(value = "month", required = true) Date month, ModelMap model) {
		model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, null);
		model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_SHOW, false);

		try {
			if (month != null) {
				InvoiceCreateCriteria criteria = new InvoiceCreateCriteria();
				criteria.setMonth(month);

				InvoiceCreateResultDTO resultDTO = invoiceService.createInvoices(criteria);
				model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, resultDTO);

				model.addAttribute(MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_SHOW, true);
			} else {
				WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, "error.save.invoice.create.noMonth");
			}
		} catch (Exception exc) {
			log.error("Cannot createInvoices.", exc);
			WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO, "error.save.invoice.create");
		}

		return view(model);
	}
}
