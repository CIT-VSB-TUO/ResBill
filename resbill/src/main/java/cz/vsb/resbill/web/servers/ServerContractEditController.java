package cz.vsb.resbill.web.servers;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
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

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.criteria.ContractCriteria.Feature;
import cz.vsb.resbill.criteria.ContractCriteria.OrderBy;
import cz.vsb.resbill.dto.ContractServerEditDTO;
import cz.vsb.resbill.exception.ContractServerServiceException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.Period;
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
@RequestMapping("/servers/contracts/edit")
@SessionAttributes("contractServerEditDTO")
public class ServerContractEditController extends AbstractServerController {

	private static final Logger log = LoggerFactory.getLogger(ServerContractEditController.class);

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

	@ModelAttribute("contracts")
	public List<Contract> getContracts() {
		try {
			ContractCriteria criteria = new ContractCriteria();
			criteria.setOrderBy(Collections.singletonList(OrderBy.NAME_ASC));
			criteria.setFeatures(EnumSet.of(Feature.TARIFF));
			return contractService.findContracts(criteria, null, null);
		} catch (Exception e) {
			log.error("Cannot load contracts", e);
			return null;
		}
	}

	private void loadContractServerEditDTO(Integer contractServerId, Integer serverId, ModelMap model) {
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
				if (serverId != null) {
					cs.setServer(serverService.findServer(serverId));
				}
				csEditDTO = new ContractServerEditDTO(cs);
			}
			model.addAttribute(CONTRACT_SERVER_EDIT_DTO_MODEL_KEY, csEditDTO);
		} catch (Exception e) {
			log.error("Cannot load contract server with id: " + contractServerId, e);

			model.addAttribute(CONTRACT_SERVER_EDIT_DTO_MODEL_KEY, csEditDTO);
			WebUtils.addGlobalError(model, CONTRACT_SERVER_EDIT_DTO_MODEL_KEY, "error.load.server.contract");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded contractServerEditDTO: " + csEditDTO);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link ContractServerEditDTO} entity with the key "contractServerEditDTO" into a model.
	 * 
	 * @param serverId
	 *          key of a {@link ContractServerEditDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "contractServerId", required = false) Integer contractServerId, @RequestParam(value = "serverId", required = false) Integer serverId, ModelMap model) {
		loadContractServerEditDTO(contractServerId, serverId, model);

		return "servers/serverContractEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link ContractServerEditDTO} instance.
	 * 
	 * @param server
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
					cs.setContract(contractService.findContract(contractServerEditDTO.getContractId()));
				}
				cs = contractServerService.saveContractServer(cs);
				if (log.isDebugEnabled()) {
					log.debug("Saved contract server: " + cs);
				}
				redirectAttributes.addAttribute("serverId", cs.getServer().getId());
				return "redirect:/servers/contracts";
			} catch (ContractServerServiceException e) {
				switch (e.getReason()) {
				case CONTRACT_SERVER_MODIFICATION:
					bindingResult.reject("error.save.server.contract.modified");
					break;
				case SERVER_ASSOCIATION_PERIOD_COLLISION:
					bindingResult.reject("error.save.server.contract.period.collision");
					break;
				case OUT_OF_CONTRACT_DURATION:
					bindingResult.reject("error.save.server.contract.out.of.duration");
					break;
				case CONTRACT_WITHOUT_TARIFF:
					bindingResult.reject("error.save.server.contract.without.tariff");
					break;
				case INVOICE_DAILY_USAGE_UNCOVERED:
					bindingResult.reject("error.save.server.contract.invoiced.dailyUsage");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.server.contract");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save ContractServerEditDTO: " + contractServerEditDTO, e);
				bindingResult.reject("error.save.server.contract");
			}
		} else {
			bindingResult.reject("error.save.server.contract.validation");
		}
		return "servers/serverContractEdit";
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
			redirectAttributes.addAttribute("serverId", cs.getServer().getId());
			return "redirect:/servers/contracts";
		} catch (ContractServerServiceException e) {
			switch (e.getReason()) {
			case DAILY_USAGE_INVOICED:
				bindingResult.reject("error.delete.server.contract.invoiced.dailyUsage");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				bindingResult.reject("error.delete.server.contract");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete ContractServerEditDTO: " + contractServerEditDTO, e);
			bindingResult.reject("error.delete.server.contract");
		}
		return "servers/serverContractEdit";
	}
}
