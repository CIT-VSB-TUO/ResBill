package cz.vsb.resbill.web.servers;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.criteria.ContractServerCriteria.OrderBy;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.service.ContractServerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from servers/serverContractList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/servers/contracts")
@SessionAttributes({ "serverEditDTO", "serverHeaderDTO" })
public class ServerContractListController {

  private static final Logger   log                        = LoggerFactory.getLogger(ServerContractListController.class);

  private static final String   SERVER_CONTRACTS_MODEL_KEY = "serverContracts";

  @Inject
  private ContractServerService contractServerService;

  private void loadServerContracts(Integer serverId, ModelMap model) {
    List<ContractServer> css = null;
    try {
      ContractServerCriteria criteria = new ContractServerCriteria();
      criteria.setServerId(serverId);
      criteria.setFetchContract(true);
      criteria.setOrderBy(OrderBy.PERIOD);
      css = contractServerService.findContractServers(criteria, null, null);
      model.addAttribute(SERVER_CONTRACTS_MODEL_KEY, css);
    } catch (Exception e) {
      log.error("Cannot load list of server contracts.", e);

      model.addAttribute(SERVER_CONTRACTS_MODEL_KEY, css);
      WebUtils.addGlobalError(model, SERVER_CONTRACTS_MODEL_KEY, "error.load.server.contracts");
    }
    if (log.isDebugEnabled()) {
      log.debug("Loaded list of server contracts size: " + (css != null ? css.size() : null));
    }
  }

  /**
   * Handles all GET requests. Loads a list of all contracts associated to server and binds it with the key "contractServers" into a model.
   * 
   * @param model
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public String view(@RequestParam(value = "serverId", required = true) Integer serverId, ModelMap model) {
    loadServerContracts(serverId, model);

    return "servers/serverContractList";
  }

}
