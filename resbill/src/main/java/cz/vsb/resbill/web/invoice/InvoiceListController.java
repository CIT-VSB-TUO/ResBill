/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.invoice;

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
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.dto.InvoiceDTO;
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

  private static final Logger log                                           = LoggerFactory.getLogger(InvoiceListController.class);

  public static final String  MODEL_OBJECT_KEY_INVOICES                     = "invoices";

  public static final String  MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_DTO  = "invoicesCreateResultsDTO";

  public static final String  MODEL_OBJECT_KEY_INVOICES_CREATE_RESULTS_SHOW = "showInvoicesCreateResults";

  @Inject
  private InvoiceService      invoiceService;

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
    loadInvoiceDTOs(model);

    return "invoice/invoiceList";
  }

  /**
   * 
   * @return
   */
  protected List<InvoiceDTO> loadInvoiceDTOs(ModelMap model) {

    List<InvoiceDTO> dtos = null;

    try {
      List<InvoiceCriteria.OrderBy> orderBy = new ArrayList<InvoiceCriteria.OrderBy>();
      orderBy.add(InvoiceCriteria.OrderBy.CUSTOMER_NAME_ASC);
      orderBy.add(InvoiceCriteria.OrderBy.CONTRACT_NAME_ASC);
      orderBy.add(InvoiceCriteria.OrderBy.ORDER_DESC);

      InvoiceCriteria criteria = new InvoiceCriteria();
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
