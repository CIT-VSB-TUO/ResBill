package cz.vsb.resbill.web.contracts;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.criteria.ContractServerCriteria.OrderBy;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.service.ContractServerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from contracts/contractServerList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/contracts/servers")
public class ContractServerListController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractServerListController.class);

	private static final String CONTRACT_SERVERS_MODEL_KEY = "contractServers";

	@Inject
	private ContractServerService contractServerService;

	private void loadContractServers(Integer contractId, ModelMap model) {
		List<ContractServer> css = null;
		try {
			ContractServerCriteria criteria = new ContractServerCriteria();
			criteria.setContractId(contractId);
			criteria.setFetchServer(true);
			criteria.setOrderBy(OrderBy.PERIOD);
			css = contractServerService.findContractServers(criteria, null, null);
			model.addAttribute(CONTRACT_SERVERS_MODEL_KEY, css);
		} catch (Exception e) {
			log.error("Cannot load list of contract servers.", e);

			model.addAttribute(CONTRACT_SERVERS_MODEL_KEY, css);
			WebUtils.addGlobalError(model, CONTRACT_SERVERS_MODEL_KEY, "error.load.contract.servers");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of contract servers size: " + (css != null ? css.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all servers associated to contract and binds it with the key "contractServers" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(@RequestParam(value = CONTRACT_ID_PARAM_KEY, required = true) Integer contractId, ModelMap model) {
		loadContractServers(contractId, model);

		return "contracts/contractServerList";
	}

}
