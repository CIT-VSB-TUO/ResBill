/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.Date;
import java.util.List;

import cz.vsb.resbill.model.DailyUsage;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface DailyUsageDAO {

  DailyUsage saveDailyUsage(DailyUsage dailyUsage);

  /**
   * Pro zadany kontrakt najde pres vsechny servery kontraktu vsechny DailyUsage, ktere doposud nebyly fakturovany a jsou nejpozdeji v pozadovanem dni.
   * Navic pripoji cenik prislusejici DailyUsage (dle data importu).
   * 
   * Server musi byt kontraktu prirazen take nejpozdeji v pozadovanem dni.
   * 
   * @param lastDay
   * @param contractId
   * @return
   */
  List<Object[]> findUninvoicedDailyUsages(Date lastDay, Integer contractId);

  DailyUsage findServerLastDailyUsage(Integer serverId);
}
