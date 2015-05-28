/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.model.Invoice;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface InvoiceDAO {

	Invoice findInvoice(Integer id);

	List<Invoice> findInvoices(InvoiceCriteria criteria, Integer offset, Integer limit);

	Invoice saveInvoice(Invoice invoice);

	Invoice deleteInvoice(Invoice invoice);
}
