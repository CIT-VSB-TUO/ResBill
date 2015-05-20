/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.Date;

import cz.vsb.resbill.model.DailyImport;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface DailyImportDAO {

	DailyImport findDailyImport(Integer id);

	DailyImport findDailyImport(Date date);

	DailyImport saveDailyImport(DailyImport dailyImport);
}
