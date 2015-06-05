/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.dailyImport;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.dto.DailyImportAllReportsResultDTO;
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

  private static final Logger log                                      = LoggerFactory.getLogger(DailyImportListController.class);

  public static final String  MODEL_OBJECT_KEY_DAILY_IMPORTS           = "dailyImports";

  public static final String  MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_DTO  = "importAllResultsDTO";

  public static final String  MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_SHOW = "showImportAllResults";

  @Inject
  private DailyImportService  dailyImportService;

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
      criteria.setOrderBy(Arrays.asList(new DailyImportCriteria.OrderBy[] { DailyImportCriteria.OrderBy.DATE_DESC }));

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

  /**
   * 
   * @param model
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.POST, params = "importAllReports")
  public String importAllReports(ModelMap model) {
    model.addAttribute(MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_DTO, null);
    model.addAttribute(MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_SHOW, false);

    // try {
    //
    // DailyImportAllReportsResultDTO resultDTO = dailyImportService.importAllReports();
    // model.addAttribute(MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_DTO, resultDTO);
    //
    // } catch (Exception exc) {
    // log.error("Cannot importAllReports.", exc);
    // model.addAttribute(MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_DTO, new DailyImportAllReportsResultDTO());
    // WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_DTO, "error.save.dailyImport.importAll");
    // }
    //
    // loadDailyImports(model);
    //
    // return "dailyImport/dailyImportList";

    try {
      DailyImportAllReportsResultDTO resultDTO = dailyImportService.importAllReports();
      model.addAttribute(MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_DTO, resultDTO);

      model.addAttribute(MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_SHOW, true);

    } catch (Exception exc) {
      log.error("Cannot importAllReports.", exc);
      WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_IMPORT_ALL_RESULTS_DTO, "error.save.dailyImport.importAll");
    }

    return view(model);
  }
}
