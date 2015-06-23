/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.invoice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.dto.InvoiceDetailDTO;
import cz.vsb.resbill.model.File;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.service.InvoiceService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Controller
@RequestMapping("/invoice/detail")
public class InvoiceDetailController {

  private static final Logger log                      = LoggerFactory.getLogger(InvoiceDetailController.class);

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
    loadInvoiceDetailDTO(invoiceId, model);

    return "invoice/invoiceDetail";
  }

  /**
   * 
   * @param invoiceId
   * @param model
   * @return
   */
  protected InvoiceDetailDTO loadInvoiceDetailDTO(Integer invoiceId, ModelMap model) {

    InvoiceDetailDTO invoiceDetailDTO = null;

    try {
      invoiceDetailDTO = invoiceService.findInvoiceDetailDTO(invoiceId);
      model.addAttribute(MODEL_OBJECT_KEY_INVOICE, invoiceDetailDTO);

    } catch (Exception exc) {
      log.error("Cannot load InvoiceDetailDTO with id: " + invoiceId, exc);

      invoiceDetailDTO = null;

      model.addAttribute(MODEL_OBJECT_KEY_INVOICE, invoiceDetailDTO);
      addGlobalError(model, "error.load.invoice");
    }

    return invoiceDetailDTO;
  }

  /**
   * 
   * @param dailyImportId
   * @param model
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
  public String delete(@RequestParam(value = "invoiceId", required = true) Integer invoiceId, ModelMap model) {

    InvoiceDetailDTO invoiceDetailDTO = loadInvoiceDetailDTO(invoiceId, model);

    if (invoiceDetailDTO != null) {
      try {

        Invoice invoice = invoiceService.deleteInvoice(invoiceDetailDTO.getTransactionId());
        return "redirect:/invoice";

      } catch (Exception exc) {
        log.error("Cannot delete invoice: " + invoiceDetailDTO, exc);
        addGlobalError(model, "error.delete.invoice");
      }
    }

    return "invoice/invoiceDetail";
  }

  /**
   * 
   * @param invoiceId
   * @param model
   * @return
   */
  @RequestMapping(value = "download", method = RequestMethod.GET)
  public void download(@RequestParam(value = "invoiceId", required = true) Integer invoiceId, HttpServletRequest request, HttpServletResponse response) {

    try {
      File attachment = invoiceService.findInvoiceAttachment(invoiceId);

      if (attachment != null) {
        try (InputStream in = new ByteArrayInputStream(attachment.getContent()); OutputStream out = response.getOutputStream()) {

          response.setContentLength(attachment.getSize().intValue());
          response.setContentType(attachment.getContentType());

          // response header
          String headerKey = "Content-Disposition";
          String headerValue = String.format("attachment; filename=\"%s\"", attachment.getName());
          response.setHeader(headerKey, headerValue);

          // Write response
          IOUtils.copy(in, out);

        } catch (Exception exc) {
          log.error("Cannot download invoice with ID: " + invoiceId, exc);
        }
      }

    } catch (Exception exc) {
      log.error("Cannot download invoice with ID: " + invoiceId, exc);
    }

  }
}
