/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.service;

/**
 * Sluzba seskupujici ulohy spoustene k urcitemu casu (nebo v urcitem intervalu).
 * 
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface SchedulerService {

  void importAllReports();
}
