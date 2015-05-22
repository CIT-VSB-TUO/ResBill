/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.dailyImport;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.service.DailyImportService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Controller
@RequestMapping("/dailyImport/detail")
public class DailyImportDetailController {

	private Logger log = LoggerFactory.getLogger(DailyImportListController.class);

	@Inject
	private DailyImportService dailyImportService;

	/**
	 * 
	 * @param model
	 * @param dailyImportId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model, @RequestParam(value = "dailyImportId", required = false) Integer dailyImportId) {

		model.addAttribute("dailyImport", getDailyImport(dailyImportId));

		return "dailyImport/dailyImportDetail";
	}

	/**
	 * 
	 * @return
	 */
	public DailyImport getDailyImport(Integer dailyImportId) {

		DailyImport result = null;

		try {
			result = dailyImportService.findDailyImport(dailyImportId);
		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
			// TODO: vypsat chybovou zpravu uzivateli
		}

		return result;
	}
}
