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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.service.DailyImportService;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/dailyImport")
public class DailyImportListController {

	private static final Logger log = LoggerFactory.getLogger(DailyImportListController.class);

	@Inject
	private DailyImportService dailyImportService;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		model.addAttribute("dailyImports", getDailyImports());

		return "dailyImport/dailyImportList";
	}

	/**
	 * 
	 * @return
	 */
	public List<DailyImport> getDailyImports() {

		List<DailyImport> result = null;

		try {
			DailyImportCriteria criteria = new DailyImportCriteria();
			criteria.setOrderBy(DailyImportCriteria.OrderBy.DATE);
			criteria.setOrderDesc(true);

			result = dailyImportService.findDailyImports(criteria, null, null);
		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
			// TODO: vypsat chybovou zpravu uzivateli
		}

		return result;
	}
}
