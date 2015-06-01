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
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Invoice;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface InvoiceService {

  Invoice findInvoice(Integer invoiceId) throws ResBillException;

  Invoice findInvoice(Integer invoiceId, boolean initializeDetails) throws ResBillException;

  List<Invoice> findInvoices(InvoiceCriteria criteria, Integer offset, Integer limit) throws ResBillException;

  Invoice deleteInvoice(Integer invoiceId) throws ResBillException;

  InvoiceCreateResultDTO createInvoices(InvoiceCreateCriteria criteria) throws ResBillException;

  Invoice createContractInvoice(Contract contract, Date month);
}
