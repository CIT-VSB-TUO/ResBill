package cz.vsb.resbill.web.contracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.dto.contract.ContractOverviewDTO;
import cz.vsb.resbill.exception.ContractServiceException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.util.WebUtils;

@Controller
@RequestMapping("/contracts/overview")
@SessionAttributes("contractOverviewDTO")
public class ContractOverviewController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractOverviewController.class);

	private static final String CONTRACT_OVERVIEW_DTO_MODEL_KEY = "contractOverviewDTO";

	private void loadContractOverviewDTO(Integer contractId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested contractId=" + contractId);
		}
		ContractOverviewDTO dto = null;
		try {
			dto = contractService.findContractOverviewDTO(contractId);
			model.addAttribute(CONTRACT_OVERVIEW_DTO_MODEL_KEY, dto);
		} catch (Exception e) {
			log.error("Cannot load ContractOverviewDTO with id: " + contractId, e);

			model.addAttribute(CONTRACT_OVERVIEW_DTO_MODEL_KEY, dto);
			WebUtils.addGlobalError(model, CONTRACT_OVERVIEW_DTO_MODEL_KEY, "error.load.contract");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded contract: " + dto);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = CONTRACT_ID_PARAM_KEY, required = true) Integer contractId, ModelMap model) {
		loadContractOverviewDTO(contractId, model);

		return "contracts/contractOverview";
	}

	/**
	 * Handle GET requests for deleting {@link Contract} instance.
	 * 
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam(value = CONTRACT_ID_PARAM_KEY, required = true) Integer contractId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Contract.id to delete: " + contractId);
		}
		try {
			Contract contract = contractService.deleteContract(contractId);
			if (log.isDebugEnabled()) {
				log.debug("Deleted contract: " + contract);
			}
			return "redirect:/contracts";
		} catch (ContractServiceException e) {
			switch (e.getReason()) {
			case TRANSACTION_EXISTENCE:
				WebUtils.addGlobalError(model, CONTRACT_OVERVIEW_DTO_MODEL_KEY, "error.delete.contract.transaction.exists");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				WebUtils.addGlobalError(model, CONTRACT_OVERVIEW_DTO_MODEL_KEY, "error.delete.contract");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete Contract with id=" + contractId, e);
			WebUtils.addGlobalError(model, CONTRACT_OVERVIEW_DTO_MODEL_KEY, "error.delete.contract");
		}
		return "contracts/contractOverview";
	}
}
