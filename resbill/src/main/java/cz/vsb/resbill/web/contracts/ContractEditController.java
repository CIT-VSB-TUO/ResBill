/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.contracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/contracts/edit")
public class ContractEditController {

  private static final Logger log = LoggerFactory.getLogger(ContractEditController.class);

  /**
   * 
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public String view(ModelMap model) {

    return "contracts/contractEdit";
  }
}
