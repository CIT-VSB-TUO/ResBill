/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.dto.ContractAgendaDTO;
import cz.vsb.resbill.dto.DailyImportAgendaDTO;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.service.DailyImportService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/reports/agenda")
public class AgendaController {

  private static final Logger log                            = LoggerFactory.getLogger(AgendaController.class);

  /**
   * Pocet dnu do minulosti, ktere nas jeste zajimaji pro zobrazeni problemu dennich importu
   */
  public static final int     DAILY_IMPORT_HISTORY_DAYS      = 7;

  public static final String  MODEL_OBJECT_KEY_CONTRACTS     = "contracts";

  public static final String  MODEL_OBJECT_KEY_DAILY_IMPORTS = "dailyImports";

  @Inject
  private ContractService     contractService;

  @Inject
  private DailyImportService  dailyImportService;

  /**
   * 
   * @param model
   * @param msgKey
   */
  protected static void addContractsGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_CONTRACTS, msgKey);
  }

  /**
   * 
   * @param model
   * @param msgKey
   */
  protected static void addDailyImportsGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_DAILY_IMPORTS, msgKey);
  }

  /**
   * 
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public String view(ModelMap model) {
    loadContractAgendaDTOs(model);
    loadDailyImportAgendaDTOs(model);

    return "reports/agenda";
  }

  /**
   * 
   * @return
   */
  protected List<ContractAgendaDTO> loadContractAgendaDTOs(ModelMap model) {

    List<ContractAgendaDTO> dtos = null;

    try {
      List<ContractCriteria.OrderBy> orderBy = new ArrayList<ContractCriteria.OrderBy>();
      orderBy.add(ContractCriteria.OrderBy.NAME_ASC);
      orderBy.add(ContractCriteria.OrderBy.EVIDENCE_NUMBER_ASC);

      ContractCriteria criteria = new ContractCriteria();
      criteria.setOrderBy(orderBy);

      dtos = contractService.findContractAgendaDTOs(criteria, null, null);

      model.addAttribute(MODEL_OBJECT_KEY_CONTRACTS, dtos);
    } catch (Exception exc) {
      log.error("Cannot load Contracts.", exc);

      dtos = null;

      model.addAttribute(MODEL_OBJECT_KEY_CONTRACTS, dtos);
      addContractsGlobalError(model, "error.load.agenda.contracts");
    }

    return dtos;
  }

  /**
   * 
   * @return
   */
  protected List<DailyImportAgendaDTO> loadDailyImportAgendaDTOs(ModelMap model) {

    List<DailyImportAgendaDTO> dtos = null;

    try {
      List<DailyImportCriteria.OrderBy> orderBy = new ArrayList<DailyImportCriteria.OrderBy>();
      orderBy.add(DailyImportCriteria.OrderBy.DATE_DESC);

      // Zajimaji nas pouze importy provedene v poslednim obdobi
      Date now = new Date();
      Date today = DateUtils.truncate(now, Calendar.DATE);
      Date histDay = DateUtils.addDays(today, -DAILY_IMPORT_HISTORY_DAYS);
      

      DailyImportCriteria criteria = new DailyImportCriteria();
      criteria.setOrderBy(orderBy);
      criteria.setBeginImportEndDate(histDay);

      dtos = dailyImportService.findDailyImportAgendaDTOs(criteria, null, null);

      model.addAttribute(MODEL_OBJECT_KEY_DAILY_IMPORTS, dtos);
    } catch (Exception exc) {
      log.error("Cannot load DailyImports.", exc);

      dtos = null;

      model.addAttribute(MODEL_OBJECT_KEY_DAILY_IMPORTS, dtos);
      addDailyImportsGlobalError(model, "error.load.agenda.dailyImports");
    }

    return dtos;
  }
}
