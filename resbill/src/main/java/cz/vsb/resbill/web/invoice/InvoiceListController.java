/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.invoice;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.service.InvoiceService;
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
	
	@Inject
	private InvoiceService invoiceService;

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
}
