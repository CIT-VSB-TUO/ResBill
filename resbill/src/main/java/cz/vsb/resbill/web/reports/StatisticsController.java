/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.reports;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.criteria.statistics.StatisticContractCriteria;
import cz.vsb.resbill.criteria.statistics.StatisticCriteria;
import cz.vsb.resbill.dto.statistics.StatisticContractDTO;
import cz.vsb.resbill.dto.statistics.StatisticReportDTO;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/reports/statistics")
@SessionAttributes("statisticCriteria")
public class StatisticsController {

  private static final Logger log                                 = LoggerFactory.getLogger(StatisticsController.class);

  public static final String  MODEL_OBJECT_KEY_STATISTIC_CRITERIA = "statisticCriteria";

  public static final String  MODEL_OBJECT_KEY_CONTRACT_STATISTIC = "contractStatistic";

  @Inject
  private ContractService     contractService;

  /**
   * 
   * @param model
   * @param msgKey
   */
  protected static void addGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_STATISTIC_CRITERIA, msgKey);
  }

  /**
   * 
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public String view(ModelMap model) {
    resetStatisticCriteria(model);

    // loadContractStatistic(model);
    // loadServerAgendaDTOs(model);
    // loadDailyImportAgendaDTOs(model);

    return "reports/statistics";
  }

  /**
   * 
   * @return
   */
  public StatisticCriteria resetStatisticCriteria(ModelMap model) {
    StatisticCriteria statisticCriteria = new StatisticCriteria();

    Date today = new Date();
    today = DateUtils.truncate(today, Calendar.DATE);

    statisticCriteria.setBeginDate(today);
    statisticCriteria.setEndDate(today);

    model.addAttribute(MODEL_OBJECT_KEY_STATISTIC_CRITERIA, statisticCriteria);

    return statisticCriteria;
  }

  /**
   * 
   * @return
   */
  protected StatisticReportDTO<StatisticContractDTO> loadContractStatistic(StatisticCriteria statisticCriteria, ModelMap model) {

    StatisticReportDTO<StatisticContractDTO> statisticReportDTO = null;

    try {
      StatisticContractCriteria criteria = new StatisticContractCriteria();
      criteria.setBeginDate(statisticCriteria.getBeginDate());
      criteria.setEndDate(statisticCriteria.getEndDate());

      statisticReportDTO = contractService.findContractStatisticReport(criteria);

      model.addAttribute(MODEL_OBJECT_KEY_CONTRACT_STATISTIC, statisticReportDTO);
    } catch (Exception exc) {
      log.error("Cannot load Contracts.", exc);

      statisticReportDTO = null;

      model.addAttribute(MODEL_OBJECT_KEY_CONTRACT_STATISTIC, statisticReportDTO);
      addGlobalError(model, "error.load.statistics.contracts");
    }

    return statisticReportDTO;
  }

  /**
   * 
   * @param statisticCriteria
   * @param bindingResult
   * @param model
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.POST, params = "contractStatistic")
  public String createContractStatistics(@Valid @ModelAttribute(MODEL_OBJECT_KEY_STATISTIC_CRITERIA) StatisticCriteria statisticCriteria, BindingResult bindingResult, ModelMap model) {

    if (!bindingResult.hasErrors()) {
      // Zadavatel momentalne nechce pouzivat rozsah, ale pouze jedno datum. Pokud se rozmysli, pak tento radek odkomentujeme a poupravime uzivatelske rozhrani 
      statisticCriteria.setEndDate(statisticCriteria.getBeginDate());
      
      loadContractStatistic(statisticCriteria, model);
    } else {
      bindingResult.reject("error.load.statistics.contracts.validation");
    }

    return "reports/statistics";
  }
}
