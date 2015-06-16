/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.servers;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.dto.ServerEditDTO;
import cz.vsb.resbill.dto.ServerHeaderDTO;
import cz.vsb.resbill.dto.ServerOverviewDTO;
import cz.vsb.resbill.exception.ServerServiceException;
import cz.vsb.resbill.service.ServerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Controller
@RequestMapping("/servers/overview")
@SessionAttributes({"serverHeaderDTO", "serverOverviewDTO"})
public class ServerOverviewController {

  private static final Logger log                                  = LoggerFactory.getLogger(ServerOverviewController.class);

  public static final String  MODEL_OBJECT_KEY_SERVER_OVERVIEW_DTO = "serverOverviewDTO";

  public static final String  MODEL_OBJECT_KEY_SERVER_HEADER_DTO   = "serverHeaderDTO";

  @Inject
  private ServerService       serverService;

  /**
   * 
   * @param model
   * @param msgKey
   */
  protected static void addGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_SERVER_OVERVIEW_DTO, msgKey);
  }

  /**
   * 
   * @param invoiceId
   * @param model
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  public String view(@RequestParam(value = "serverId", required = true) Integer serverId, ModelMap model) {
    loadServerHeaderDTO(serverId, model);
    loadServerOverviewDTO(serverId, model);

    return "servers/serverOverview";
  }

  /**
   * 
   * @param serverId
   * @param model
   */
  private void loadServerOverviewDTO(Integer serverId, ModelMap model) {

    ServerOverviewDTO serverOverviewDTO = null;
    try {
      serverOverviewDTO = serverService.findServerOverviewDTO(serverId);
      model.addAttribute(MODEL_OBJECT_KEY_SERVER_OVERVIEW_DTO, serverOverviewDTO);
    } catch (Exception e) {
      log.error("Cannot load serverOverviewDTO with id: " + serverId, e);

      model.addAttribute(MODEL_OBJECT_KEY_SERVER_OVERVIEW_DTO, serverOverviewDTO);
      addGlobalError(model, "error.load.server");
    }

  }

  /**
   * 
   * @param serverId
   * @param model
   */
  private void loadServerHeaderDTO(Integer serverId, ModelMap model) {

    ServerHeaderDTO serverHeaderDTO = null;
    try {
      serverHeaderDTO = serverService.findServerHeaderDTO(serverId);
      model.addAttribute(MODEL_OBJECT_KEY_SERVER_HEADER_DTO, serverHeaderDTO);
    } catch (Exception e) {
      log.error("Cannot load serverHeaderDTO with id: " + serverId, e);

      model.addAttribute(MODEL_OBJECT_KEY_SERVER_HEADER_DTO, serverHeaderDTO);
      WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_SERVER_HEADER_DTO, "error.load.server");
    }
  }
  
  /**
   * Handle POST requests for deleting {@link ServerEditDTO} instance.
   * 
   * @param server
   * @param bindingResult
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
  public String delete(@Valid @ModelAttribute(MODEL_OBJECT_KEY_SERVER_HEADER_DTO) ServerHeaderDTO serverHeaderDTO, BindingResult bindingResult) {

    try {
      serverService.deleteServer(serverHeaderDTO.getId());

      return "redirect:/servers";
    } catch (ServerServiceException e) {
      switch (e.getReason()) {
      case CONTRACT_ASSOCIATED:
        bindingResult.reject("error.delete.server.contract.associated");
        break;
      case DAILY_USAGE_EXISTENCE:
        bindingResult.reject("error.delete.server.dailyUsage.exists");
        break;
      default:
        log.warn("Unsupported reason: " + e);
        bindingResult.reject("error.delete.server");
        break;
      }
    } catch (Exception e) {
      log.error("Cannot delete ServerHeaderDTO: " + serverHeaderDTO, e);
      bindingResult.reject("error.delete.server");
    }
    return "servers/serverOverview";
  }
}
