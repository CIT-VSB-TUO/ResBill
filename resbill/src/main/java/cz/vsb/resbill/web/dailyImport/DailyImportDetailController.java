/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.dailyImport;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.model.Person;
import cz.vsb.resbill.service.DailyImportService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Controller
@RequestMapping("/dailyImport/detail")
public class DailyImportDetailController {

	private static final Logger log = LoggerFactory.getLogger(DailyImportListController.class);

	public static final String DAILY_IMPORT_KEY = "dailyImport";

	@Inject
	private DailyImportService dailyImportService;

	/**
	 * 
	 * @param model
	 * @param dailyImportId
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "dailyImportId", required = true) Integer dailyImportId, ModelMap model) {

		loadDailyImport(dailyImportId, model);

		return "dailyImport/dailyImportDetail";
	}

	/**
	 * 
	 * @return
	 */
	protected DailyImport loadDailyImport(Integer dailyImportId, ModelMap model) {

		DailyImport dailyImport = null;

		try {
			dailyImport = dailyImportService.findDailyImport(dailyImportId);
			model.addAttribute(DAILY_IMPORT_KEY, dailyImport);

			throw new Exception("TEST EXC");
		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
			// TODO: vypsat chybovou zpravu uzivateli

//			dailyImport = new DailyImport();
			dailyImport = null;
			model.addAttribute(DAILY_IMPORT_KEY, dailyImport);

			addGlobalError(model, "error.load.dailyImport");
			addGlobalError(model, "text.dailyImport.error");
		}

		return dailyImport;
	}

	/**
	 * 
	 * @param model
	 * @param msgKey
	 */
	protected void addGlobalError(ModelMap model, String msgKey) {
		WebUtils.addGlobalError(model, DAILY_IMPORT_KEY, msgKey);
	}



	/**
	 * 
	 * @param dailyImportId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@RequestParam(value = "dailyImportId", required = true) Integer dailyImportId, ModelMap model) {

		DailyImport dailyImport = loadDailyImport(dailyImportId, model);

		try {
			System.out.println("Budu mazat");

			if (1 == 1)
				throw new Exception("POKUSNA VYJIMKA");

			dailyImport = dailyImportService.deleteDailyImport(dailyImport.getId());
		} catch (PersistenceException e) {
			addGlobalError(model, "error.delete.dailyImport.relations");
			return "dailyImport/dailyImportDetail";
		} catch (Exception e) {
			log.error("Cannot delete dailyImport: " + dailyImport, e);
			addGlobalError(model, "error.delete.dailyImport");
			return "dailyImport/dailyImportDetail";
		}
		return "redirect:/dailyImport";
	}
}
