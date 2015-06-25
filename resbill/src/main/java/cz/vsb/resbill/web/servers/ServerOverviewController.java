/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.servers;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cz.vsb.resbill.dto.server.ServerEditDTO;
import cz.vsb.resbill.dto.server.ServerOverviewDTO;
import cz.vsb.resbill.exception.ServerServiceException;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.ServerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Controller
@RequestMapping("/servers/overview")
@SessionAttributes("serverOverviewDTO")
public class ServerOverviewController extends AbstractServerController {

	private static final Logger log = LoggerFactory.getLogger(ServerOverviewController.class);

	public static final String MODEL_OBJECT_KEY_SERVER_OVERVIEW_DTO = "serverOverviewDTO";

	@Inject
	private ServerService serverService;

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
	public String view(@RequestParam(value = SERVER_ID_PARAM_KEY, required = true) Integer serverId, ModelMap model) {
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
	 * Handle GET requests for deleting {@link ServerEditDTO} instance.
	 * 
	 * @param server
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam(value = SERVER_ID_PARAM_KEY, required = true) Integer serverId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Server.id to delete: " + serverId);
		}
		try {
			Server server = serverService.deleteServer(serverId);
			if (log.isDebugEnabled()) {
				log.debug("Deleted server: " + server);
			}
			return "redirect:/servers";
		} catch (ServerServiceException e) {
			switch (e.getReason()) {
			case CONTRACT_ASSOCIATED:
				addGlobalError(model, "error.delete.server.contract.associated");
				break;
			case DAILY_USAGE_EXISTENCE:
				addGlobalError(model, "error.delete.server.dailyUsage.exists");
				break;
			default:
				log.warn("Unsupported reason: " + e);
				addGlobalError(model, "error.delete.server");
				break;
			}
		} catch (Exception e) {
			log.error("Cannot delete server with id: " + serverId, e);
			addGlobalError(model, "error.delete.server");
		}
		return "servers/serverOverview";
	}
}
