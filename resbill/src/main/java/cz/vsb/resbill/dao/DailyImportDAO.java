/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao;

import java.util.Date;
import java.util.List;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.model.DailyImport;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public interface DailyImportDAO {

	DailyImport findDailyImport(Integer id);

	DailyImport findDailyImport(Date date);

	List<DailyImport> findDailyImports(DailyImportCriteria criteria, Integer offset, Integer limit);

	DailyImport saveDailyImport(DailyImport dailyImport);
	
	DailyImport deleteDailyImport(DailyImport dailyImport);
}
