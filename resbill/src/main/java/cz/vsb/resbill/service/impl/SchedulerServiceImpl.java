/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cz.vsb.resbill.criteria.InvoiceCreateCriteria;
import cz.vsb.resbill.dto.InvoiceCreateResultDTO;
import cz.vsb.resbill.service.DailyImportService;
import cz.vsb.resbill.service.InvoiceService;
import cz.vsb.resbill.service.SchedulerService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

  private static final Logger log = LoggerFactory.getLogger(SchedulerServiceImpl.class);

  @Inject
  private DailyImportService  dailyImportService;

  @Inject
  private InvoiceService      invoiceService;

  /**
   * Import vsech dennich reportu, ktere lezi v dohodnutem adresari.
   * 
   */

  @Override
  @Scheduled(cron = "0 0 3 * * *")
  public void importAllReports() {
    log.info("START scheduled task 'importAllReports()'.");
    Date begin = new Date();

    try {
      dailyImportService.importAllReports();
    } catch (Exception exc) {
      log.error("An unexpected error occured while importAllReports().", exc);
    }

    Date end = new Date();
    log.info("END scheduled task 'importAllReports()' after: " + (end.getTime() - begin.getTime()) + " ms.");
  }

  /**
   * 
   */
  @Override
  @Scheduled(cron = "0 0 4 1 * *")
  public void createAllInvoices() {
    log.info("START scheduled task 'createAllInvoices()'.");
    Date begin = new Date();

    try {
      Date today = new Date();
      Date month = DateUtils.addDays(DateUtils.truncate(today, Calendar.MONTH), -1);

      InvoiceCreateCriteria criteria = new InvoiceCreateCriteria();
      criteria.setMonth(month);

      InvoiceCreateResultDTO resultDTO = invoiceService.createInvoices(criteria);
      log.info(resultDTO.toString());
      
    } catch (Exception exc) {
      log.error("An unexpected error occured while createAllInvoices().", exc);
    }

    Date end = new Date();
    log.info("END scheduled task 'createAllInvoices()' after: " + (end.getTime() - begin.getTime()) + " ms.");
  }
}
