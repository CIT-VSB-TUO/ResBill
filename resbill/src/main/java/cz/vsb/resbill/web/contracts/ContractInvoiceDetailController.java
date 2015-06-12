/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.contracts;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.service.InvoiceService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/contracts/invoices/detail")
@SessionAttributes("contractEditDTO")
public class ContractInvoiceDetailController {

  private static final Logger log                      = LoggerFactory.getLogger(ContractInvoiceDetailController.class);

  public static final String  MODEL_OBJECT_KEY_INVOICE = "invoice";

  @Inject
  private InvoiceService      invoiceService;

  /**
   * 
   * @param model
   * @param msgKey
   */
  protected static void addGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_INVOICE, msgKey);
  }

  /**
   * 
   * @param invoiceId
   * @param model
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  public String view(@RequestParam(value = "invoiceId", required = true) Integer invoiceId, ModelMap model) {
    loadInvoice(invoiceId, model);

    return "contracts/contractInvoiceDetail";
  }

  /**
   * 
   * @param invoiceId
   * @param model
   * @return
   */
  protected Invoice loadInvoice(Integer invoiceId, ModelMap model) {

    Invoice invoice = null;

    try {
      invoice = invoiceService.findInvoice(invoiceId, true, true);
      model.addAttribute(MODEL_OBJECT_KEY_INVOICE, invoice);

    } catch (Exception exc) {
      log.error("Cannot load Invoice with id: " + invoiceId, exc);

      invoice = null;

      model.addAttribute(MODEL_OBJECT_KEY_INVOICE, invoice);
      addGlobalError(model, "error.load.invoice");
    }

    return invoice;
  }

  /**
   * 
   * @param dailyImportId
   * @param model
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
  public String delete(@RequestParam(value = "invoiceId", required = true) Integer invoiceId, ModelMap model, RedirectAttributes redirectAttributes) {

    Invoice invoice = loadInvoice(invoiceId, model);

    if (invoice != null) {
      try {

        invoice = invoiceService.deleteInvoice(invoice.getId());

        redirectAttributes.addAttribute("contractId", invoice.getContract().getId());
        return "redirect:/contracts/invoices";

      } catch (Exception exc) {
        log.error("Cannot delete invoice: " + invoice, exc);
        addGlobalError(model, "error.delete.invoice");
      }
    }

    return "contractInvoiceDetail";
  }
}
