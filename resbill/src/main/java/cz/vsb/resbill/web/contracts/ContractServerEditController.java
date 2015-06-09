package cz.vsb.resbill.web.contracts;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.criteria.ServerCriteria.OrderBy;
import cz.vsb.resbill.dto.ContractServerEditDTO;
import cz.vsb.resbill.exception.ContractServerServiceException;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.ContractServerService;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.service.ServerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from servers/serverContractEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/contracts/servers/edit")
@SessionAttributes({ "contractServerEditDTO", "contractEditDTO" })
public class ContractServerEditController {

	private static final Logger log = LoggerFactory.getLogger(ContractServerEditController.class);

	private static final String CONTRACT_SERVER_EDIT_DTO_MODEL_KEY = "contractServerEditDTO";

	@Inject
	private ContractServerService contractServerService;

	@Inject
	private ServerService serverService;

	@Inject
	private ContractService contractService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute("servers")
	public List<Server> getServers() {
		try {
			ServerCriteria criteria = new ServerCriteria();
			criteria.setOrderBy(Collections.singletonList(OrderBy.NAME_ASC));
			return serverService.findServers(criteria, null, null);
		} catch (Exception e) {
			log.error("Cannot load servers", e);
			return null;
		}
	}

	private void loadContractServerEditDTO(Integer contractServerId, Integer contractId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested contractServer.id=" + contractServerId);
		}
		ContractServerEditDTO csEditDTO = null;
		try {
			if (contractServerId != null) {
				csEditDTO = new ContractServerEditDTO(contractServerService.findContractServer(contractServerId));
			} else {
				ContractServer cs = new ContractServer();
				cs.setPeriod(new Period());
				cs.getPeriod().setBeginDate(DateUtils.truncate(new Date(), Calendar.DATE));
				if (contractId != null) {
					cs.setContract(contractService.findContract(contractId));
				}
				csEditDTO = new ContractServerEditDTO(cs);
			}
			model.addAttribute(CONTRACT_SERVER_EDIT_DTO_MODEL_KEY, csEditDTO);
		} catch (Exception e) {
			log.error("Cannot load contract server with id: " + contractServerId, e);

			model.addAttribute(CONTRACT_SERVER_EDIT_DTO_MODEL_KEY, csEditDTO);
			WebUtils.addGlobalError(model, CONTRACT_SERVER_EDIT_DTO_MODEL_KEY, "error.load.contract.server");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded contractServerEditDTO: " + csEditDTO);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link ContractServerEditDTO} entity with the key "contractServerEditDTO" into a model.
	 * 
	 * @param contractServerId
	 *          key of a {@link ContractServerEditDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "contractServerId", required = false) Integer contractServerId, @RequestParam(value = "contractId", required = false) Integer contractId, ModelMap model) {
		loadContractServerEditDTO(contractServerId, contractId, model);

		return "contracts/contractServerEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link ContractServerEditDTO} instance.
	 * 
	 * @param contractServerEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(CONTRACT_SERVER_EDIT_DTO_MODEL_KEY) ContractServerEditDTO contractServerEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractServerEditDTO to save: " + contractServerEditDTO);
		}
		if (!bindingResult.hasErrors()) {
			try {
				ContractServer cs = contractServerEditDTO.getContractServer();
				if (cs.getId() == null) {
					cs.setServer(serverService.findServer(contractServerEditDTO.getServerId()));
				}
				cs = contractServerService.saveContractServer(cs);
				if (log.isDebugEnabled()) {
					log.debug("Saved contract server: " + cs);
				}
				redirectAttributes.addAttribute("contractId", cs.getContract().getId());
				return "redirect:/contracts/servers";
			} catch (ContractServerServiceException e) {
				switch (e.getReason()) {
				case CONTRACT_SERVER_MODIFICATION:
					bindingResult.reject("error.save.contract.server.modified");
					break;
				case SERVER_ASSOCIATION_PERIOD_COLLISION:
					bindingResult.reject("error.save.contract.server.period.collision");
					break;
				case OUT_OF_CONTRACT_DURATION:
					bindingResult.reject("error.save.contract.server.out.of.duration");
					break;
				case CONTRACT_WITHOUT_TARIFF:
					bindingResult.reject("error.save.contract.server.without.tariff");
					break;
				case INVOICE_DAILY_USAGE_UNCOVERED:
					bindingResult.reject("error.save.contract.server.invoiced.dailyUsage");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.contract.server");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save ContractServerEditDTO: " + contractServerEditDTO, e);
				bindingResult.reject("error.save.contract.server");
			}
		} else {
			bindingResult.reject("error.save.contract.server.validation");
		}
		return "contracts/contractServerEdit";
	}

	/**
	 * Handle POST requests for deleting {@link ContractServerEditDTO} instance.
	 * 
	 * @param contractServerEditDTO
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
	public String delete(@ModelAttribute(CONTRACT_SERVER_EDIT_DTO_MODEL_KEY) ContractServerEditDTO contractServerEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ContractServerEditDTO to delete: " + contractServerEditDTO);
		}
		try {
			ContractServer cs = contractServerService.deleteContractServer(contractServerEditDTO.getContractServer().getId());
			if (log.isDebugEnabled()) {
				log.debug("Deleted ContractServer: " + cs);
			}
			redirectAttributes.addAttribute("contractId", cs.getContract().getId());
			return "redirect:/contracts/servers";
		} catch (ContractServerServiceException e) {
			switch (e.getReason()) {
			case DAILY_USAGE_INVOICED:
				bindingResult.reject("error.delete.contract.server.invoiced.dailyUsage");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				bindingResult.reject("error.delete.contract.server");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete ContractServerEditDTO: " + contractServerEditDTO, e);
			bindingResult.reject("error.delete.contract.server");
		}
		return "contracts/contractServerEdit";
	}
}
