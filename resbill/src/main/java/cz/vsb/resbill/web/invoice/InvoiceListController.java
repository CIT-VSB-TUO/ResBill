/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.invoice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
//		loadDailyImports(model);

		return "invoice/invoiceList";
	}
}
