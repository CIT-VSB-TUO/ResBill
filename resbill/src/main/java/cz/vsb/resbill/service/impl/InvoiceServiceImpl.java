/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.InvoiceCreateCriteria;
import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.InvoiceDAO;
import cz.vsb.resbill.dao.InvoiceTypeDAO;
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.InvoiceType;
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
  private InvoiceDAO          invoiceDAO;

  @Inject
  private InvoiceTypeDAO      invoiceTypeDAO;

  @Inject
  private ContractDAO         contractDAO;

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

  /**
   * 
   * @param criteria
   * @return
   * @throws ResBillException
   */
  @Override
  public InvoiceCreateResultDTO createInvoices(InvoiceCreateCriteria criteria) throws ResBillException {
    log.info("Zacinam fakturaci.");

    InvoiceCreateResultDTO resultDTO = new InvoiceCreateResultDTO();
    resultDTO.setBeginTimestamp(new Date());

    // Ziskam datum posledniho dne v pozadovanem mesici
    Date month = criteria.getMonth();
    Date lastDay = DateUtils.addDays(DateUtils.addMonths(DateUtils.truncate(month, Calendar.MONTH), 1), -1);

    // Ziskam poradi mesice v roce
    Calendar lastDayCal = Calendar.getInstance();
    lastDayCal.setTime(lastDay);
    Integer monthOrder = lastDayCal.get(Calendar.MONTH) + 1;

    // Ziskam ID typu uctovani platnych pro tento mesic
    List<Integer> invoiceTypeIds = new ArrayList<Integer>();
    InvoiceTypeCriteria invoiceTypeCriteria = new InvoiceTypeCriteria();
    invoiceTypeCriteria.setZeroModuloDividend(monthOrder);
    List<InvoiceType> invoiceTypes = invoiceTypeDAO.findInvoiceTypes(invoiceTypeCriteria, null, null);
    for (InvoiceType invoiceType : invoiceTypes) {
      invoiceTypeIds.add(invoiceType.getId());
    }

    // Ziskat vsechny kontrakty, jejichz servery maji alespon jedno nevyfakturovane DailyUsage v pozadovanem mesici NEBO DRIVE
    // Server musi byt kontraktu prirazen take nektery den v pozadovanem mesici NEBO DRIVE
    List<Contract> contracts = contractDAO.findUninvoicedContracts(lastDay, invoiceTypeIds);

    resultDTO.setContractsNumberAll(contracts.size());

    resultDTO.setEndTimestamp(new Date());

    log.info("Fakturace dokoncena: " + resultDTO);

    return resultDTO;
  }
}
