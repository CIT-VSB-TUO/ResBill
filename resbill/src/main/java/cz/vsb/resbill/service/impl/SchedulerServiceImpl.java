/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service.impl;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cz.vsb.resbill.service.DailyImportService;
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

}
