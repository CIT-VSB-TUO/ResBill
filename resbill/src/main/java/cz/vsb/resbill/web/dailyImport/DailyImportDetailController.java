/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.dailyImport;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.exception.DailyImportException;
import cz.vsb.resbill.model.DailyImport;
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

	public static final String MODEL_OBJECT_KEY_DAILY_IMPORT = "dailyImport";

	@Inject
	private DailyImportService dailyImportService;

	/**
	 * 
	 * @param model
	 * @param msgKey
	 */
	protected static void addGlobalError(ModelMap model, String msgKey) {
		WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_DAILY_IMPORT, msgKey);
	}

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
			dailyImport = dailyImportService.findDailyImport(dailyImportId, true, true);
			model.addAttribute(MODEL_OBJECT_KEY_DAILY_IMPORT, dailyImport);
		} catch (Exception exc) {
			log.error("Cannot load DailyImport with id: " + dailyImportId, exc);

			dailyImport = null;

			model.addAttribute(MODEL_OBJECT_KEY_DAILY_IMPORT, dailyImport);
			addGlobalError(model, "error.load.dailyImport");
		}

		return dailyImport;
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

		if (dailyImport != null) {
			try {

				dailyImport = dailyImportService.deleteDailyImport(dailyImport.getId());
				return "redirect:/dailyImport";

			} catch (DailyImportException exc) {
				if (exc.getReason() == DailyImportException.Reason.USED_ON_INVOICE) {
					log.error("Cannot delete dailyImport - error.delete.dailyImport.usedOnInvoice: " + dailyImport, exc);
					addGlobalError(model, "error.delete.dailyImport.usedOnInvoice");
				} else {
					log.error("Cannot delete dailyImport: " + dailyImport, exc);
					addGlobalError(model, "error.delete.dailyImport");
				}
			} catch (Exception exc) {
				log.error("Cannot delete dailyImport: " + dailyImport, exc);
				addGlobalError(model, "error.delete.dailyImport");
			}
		}

		return "dailyImport/dailyImportDetail";
	}
}
