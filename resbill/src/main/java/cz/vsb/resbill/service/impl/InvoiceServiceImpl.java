/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.dao.InvoiceDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.service.InvoiceService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

	private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

	@Inject
	private InvoiceDAO invoiceDAO;

	/**
	 * 
	 */
	@Override
	public Invoice findInvoice(Integer invoiceId) throws ResBillException {
		return findInvoice(invoiceId, false);
	}

	/**
	 * 
	 */
	@Override
	public Invoice findInvoice(Integer invoiceId, boolean initializeAttachment) throws ResBillException {
		try {
			Invoice invoice = invoiceDAO.findInvoice(invoiceId);

			if (invoice != null) {
				if (initializeAttachment) {
					invoice.getAttachment();
				}
			}

			return invoice;
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding Invoice by id=" + invoiceId, exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * 
	 */
	@Override
	public List<Invoice> findInvoices(InvoiceCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return invoiceDAO.findInvoices(criteria, offset, limit);
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding Invoices.", exc);
			throw new ResBillException(exc);
		}
	}

	/**
   * 
   */
	@Override
	public Invoice deleteInvoice(Integer invoiceId) throws ResBillException {
		try {

			Invoice invoice = invoiceDAO.findInvoice(invoiceId);
			if (invoice != null) {
				invoice = invoiceDAO.deleteInvoice(invoice);
			}

			return invoice;

		} catch (Exception exc) {
			log.error("An unexpected error occured while deleting Invoice with id=" + invoiceId, exc);
			throw new ResBillException(exc);
		}
	}

}
