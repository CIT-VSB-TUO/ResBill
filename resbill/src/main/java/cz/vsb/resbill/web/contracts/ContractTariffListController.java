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

import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.criteria.ContractTariffCriteria.OrderBy;
import cz.vsb.resbill.model.ContractTariff;
import cz.vsb.resbill.service.ContractTariffService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from contracts/contractTariffList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/contracts/tariffs")
public class ContractTariffListController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractTariffListController.class);

	private static final String CONTRACT_TARIFFS_MODEL_KEY = "contractTariffs";

	@Inject
	private ContractTariffService contractTariffService;

	private void loadContractTariffs(Integer contractId, ModelMap model) {
		List<ContractTariff> cts = null;
		try {
			ContractTariffCriteria criteria = new ContractTariffCriteria();
			criteria.setContractId(contractId);
			criteria.setFetchTariff(true);
			criteria.setOrderBy(OrderBy.PERIOD_ASC);
			cts = contractTariffService.findContractTariffs(criteria, null, null);
			model.addAttribute(CONTRACT_TARIFFS_MODEL_KEY, cts);
		} catch (Exception e) {
			log.error("Cannot load list of contract tariffs.", e);

			model.addAttribute(CONTRACT_TARIFFS_MODEL_KEY, cts);
			WebUtils.addGlobalError(model, CONTRACT_TARIFFS_MODEL_KEY, "error.load.contract.tariffs");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of contract tariffs size: " + (cts != null ? cts.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all tariffs associated to contract and binds it with the key "contractTariffs" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(@RequestParam(value = CONTRACT_ID_PARAM_KEY, required = true) Integer contractId, ModelMap model) {
		loadContractTariffs(contractId, model);

		return "contracts/contractTariffList";
	}

}
