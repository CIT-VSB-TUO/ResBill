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

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.service.DailyImportService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/dailyImport")
public class DailyImportListController {

	private static final Logger log = LoggerFactory.getLogger(DailyImportListController.class);

	public static final String MODEL_OBJECT_KEY_DAILY_IMPORTS = "dailyImports";

	@Inject
	private DailyImportService dailyImportService;

	/**
	 * 
	 * @param model
	 * @param msgKey
	 */
	protected static void addGlobalError(ModelMap model, String msgKey) {
		WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_DAILY_IMPORTS, msgKey);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		loadDailyImports(model);

		return "dailyImport/dailyImportList";
	}

	/**
	 * 
	 * @return
	 */
	protected List<DailyImport> loadDailyImports(ModelMap model) {

		List<DailyImport> dailyImports = null;

		try {
			DailyImportCriteria criteria = new DailyImportCriteria();
			criteria.setOrderBy(DailyImportCriteria.OrderBy.DATE);
			criteria.setOrderDesc(true);

			dailyImports = dailyImportService.findDailyImports(criteria, null, null);

			model.addAttribute(MODEL_OBJECT_KEY_DAILY_IMPORTS, dailyImports);
		} catch (Exception exc) {
			log.error("Cannot load DailyImports.", exc);

			dailyImports = null;

			model.addAttribute(MODEL_OBJECT_KEY_DAILY_IMPORTS, dailyImports);
			addGlobalError(model, "error.load.dailyImports");
		}

		return dailyImports;
	}

}
