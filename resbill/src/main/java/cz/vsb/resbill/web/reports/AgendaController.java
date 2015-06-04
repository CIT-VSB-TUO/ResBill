/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.reports;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.dto.ContractAgendaDTO;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/reports/agenda")
public class AgendaController {

  private static final Logger log                        = LoggerFactory.getLogger(AgendaController.class);

  public static final String  MODEL_OBJECT_KEY_CONTRACTS = "contracts";

  @Inject
  private ContractService     contractService;

  /**
   * 
   * @param model
   * @param msgKey
   */
  protected static void addGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_CONTRACTS, msgKey);
  }

  /**
   * 
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public String view(ModelMap model) {
    loadContractAgendaDTOs(model);

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
      addGlobalError(model, "error.load.agenda.contracts");
    }

    return dtos;
  }
}
