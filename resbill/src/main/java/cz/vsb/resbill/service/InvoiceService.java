/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

import java.util.Date;
import java.util.List;

import cz.vsb.resbill.criteria.InvoiceCreateCriteria;
import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.dto.InvoiceDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.InvoiceType;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface InvoiceService {

  Invoice findInvoice(Integer invoiceId) throws ResBillException;

  Invoice findInvoice(Integer invoiceId, boolean initializeSummary, boolean initializeDetail) throws ResBillException;

  List<Invoice> findInvoices(InvoiceCriteria criteria, Integer offset, Integer limit) throws ResBillException;
  
  List<InvoiceDTO> findInvoiceDTOs(InvoiceCriteria criteria, Integer offset, Integer limit) throws ResBillException;

  Invoice deleteInvoice(Integer invoiceId) throws ResBillException;

  InvoiceCreateResultDTO createInvoices(InvoiceCreateCriteria criteria) throws ResBillException;

  Invoice createContractInvoice(Contract contract, InvoiceType invoiceType, Date month);
}
