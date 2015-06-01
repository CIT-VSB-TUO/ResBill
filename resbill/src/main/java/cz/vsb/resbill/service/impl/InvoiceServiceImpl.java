/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.criteria.InvoiceCreateCriteria;
import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.DailyUsageDAO;
import cz.vsb.resbill.dao.InvoiceDAO;
import cz.vsb.resbill.dao.InvoiceTypeDAO;
import cz.vsb.resbill.dao.PriceListDAO;
import cz.vsb.resbill.dao.TransactionTypeDAO;
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.exception.DailyImportException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.File;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.InvoiceType;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.PriceList;
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
  private TransactionTypeDAO  transactionTypeDAO;

  @Inject
  private ContractDAO         contractDAO;

  @Inject
  private DailyUsageDAO       dailyUsageDAO;

  @Inject
  private PriceListDAO        priceListDAO;

  @Inject
  private InvoiceService      invoiceService;

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
  public Invoice findInvoice(Integer invoiceId, boolean initializeDetails) throws ResBillException {
    try {
      Invoice invoice = invoiceDAO.findInvoice(invoiceId);

      if (invoice != null) {
        if (initializeDetails) {
          invoice.getDetails();
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

    // Vytvareni faktur pro jednotlive kontrakty
    for (Contract contract : contracts) {
      try {
        Invoice invoice = invoiceService.createContractInvoice(contract, month);

        if (invoice.getNoPriceList().booleanValue()) {
          resultDTO.setContractsNumberNoPriceList(resultDTO.getContractsNumberNoPriceList() + 1);
        } else {
          resultDTO.setContractsNumberOk(resultDTO.getContractsNumberOk() + 1);
        }
      } catch (Exception exc) {
        log.error("An unexpected error occured while createContractInvoice for contractId: " + contract.getId(), exc);
        resultDTO.setContractsNumberError(resultDTO.getContractsNumberError() + 1);
      }
    }

    resultDTO.setEndTimestamp(new Date());

    log.info("Fakturace dokoncena: " + resultDTO);

    return resultDTO;
  }

  /**
   * Vytvori fakturu pro pozadovany kontrakt v pozadovanem mesici
   * 
   * @param contract
   * @return
   * @throws DailyImportException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Invoice createContractInvoice(Contract contract, Date month) {
    log.info("Zacinam fakturovat pro kontrakt s ID: " + contract.getId());

    Invoice invoice = new Invoice();
    invoice.setContract(contract);
    invoice.setDecisiveDate(new Date());

    // TODO: fakturu naplnit smysluplnymi udaji
    invoice.setInvoiceType(invoiceTypeDAO.findInvoiceType(1));
    invoice.setAmount(BigDecimal.ZERO);
    invoice.setTransactionType(transactionTypeDAO.findTransactionType(1));
    invoice.setOrder(2);
    File file = new File();
    file.setName("Foo file");
    file.setSize(new Long(0));
    file.setContentType("Foo content type");
    invoice.setAttachment(file);

    StringBuilder details = new StringBuilder();

    Date firstDay = DateUtils.truncate(month, Calendar.MONTH);
    Date lastDay = DateUtils.addDays(DateUtils.addMonths(firstDay, 1), -1);

    invoice.setPeriod(new Period());
    invoice.getPeriod().setBeginDate(firstDay);
    invoice.getPeriod().setEndDate(lastDay);

    // Ziskat vsechny DailyUsages, ktere maji byt fakturovany
    List<DailyUsage> dailyUsages = dailyUsageDAO.findUninvoicedDailyUsages(lastDay, contract.getId());
    log.info("Pocet DailyUsage k fakturaci: " + dailyUsages.size());

    // Pro kazdy DailyUsage dohledat cenik
    List<TmpDailyUsagePriceList> usagePrices = new ArrayList<TmpDailyUsagePriceList>();
    for (DailyUsage dailyUsage : dailyUsages) {
      TmpDailyUsagePriceList usagePrice = new TmpDailyUsagePriceList();
      usagePrices.add(usagePrice);

      usagePrice.dailyUsage = dailyUsage;

      PriceList priceList = priceListDAO.findContractDailyUsagePriceList(contract.getId(), dailyUsage.getId());
      usagePrice.priceList = priceList;

      if (priceList == null) {
        invoice.setNoPriceList(true);
      }
    }

    // Kompletni vypis nalezenych DailyUsage s dohledanymi ceniky, roztrideno po jednotlivych serverech kontraktu
    String serverId = null;
    StringBuilder detailPricing = new StringBuilder();
    for (TmpDailyUsagePriceList usagePrice : usagePrices) {
      // Odradkovani
      if (detailPricing.length() > 0) {
        detailPricing.append("\n");
      }

      // Hlavicka serveru
      if (serverId == null || !serverId.equals(usagePrice.dailyUsage.getServer().getServerId())) {
        serverId = usagePrice.dailyUsage.getServer().getServerId();

        detailPricing.append("\n");
        detailPricing.append("Server - ID: ");
        detailPricing.append(serverId);
        detailPricing.append("; jmeno: ");
        detailPricing.append(usagePrice.dailyUsage.getServer().getName());
        detailPricing.append("\n");
      }

      detailPricing.append(createDetailPricing(usagePrice));
    }
    details.append("\n\n\n");
    details.append("Detailní rozpis:\n");
    details.append(detailPricing);

    log.debug(details.toString());

    invoice.setDetails(details.toString());

    invoiceDAO.saveInvoice(invoice);

    log.info("Fakturace pro kontrakt s ID: " + contract.getId() + " ukoncena.");

    return invoice;
  }

  /**
   * 
   * @param usagePrice
   * @return
   */
  protected String createDetailPricing(TmpDailyUsagePriceList usagePrice) {
    // TODO: pokud toto zachovame, tak by bylo vhodne text nekam vytahnout

    StringBuilder pricing = new StringBuilder();

    pricing.append(usagePrice.dailyUsage.getDailyImport().getDate());

    // CPU
    pricing.append(": CPU: ");
    pricing.append(usagePrice.dailyUsage.getCpu());
    pricing.append(" á ");
    pricing.append(usagePrice.priceList.getCpuPrice());
    pricing.append(" Kč/měsíc");

    // RAM
    pricing.append("; RAM: ");
    pricing.append(usagePrice.dailyUsage.getMemoryMB());
    pricing.append(" á ");
    pricing.append(usagePrice.priceList.getMemoryMBPrice());
    pricing.append(" Kč/měsíc");

    // Storage
    pricing.append("; Storage: ");
    pricing.append(usagePrice.dailyUsage.getProvisionedSpaceGB());
    pricing.append(" á ");
    pricing.append(usagePrice.priceList.getSpaceGBPrice());
    pricing.append(" Kč/měsíc");

    // Backup
    pricing.append("; Backup: ");
    pricing.append(usagePrice.dailyUsage.getBackupGB());
    pricing.append(" á ");
    pricing.append(usagePrice.priceList.getBackupGBPrice());
    pricing.append(" Kč/měsíc");

    return pricing.toString();
  }

  /**
   * 
   * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
   *
   */
  private static class TmpDailyUsagePriceList {

    public DailyUsage dailyUsage;

    public PriceList  priceList;
  }
}
