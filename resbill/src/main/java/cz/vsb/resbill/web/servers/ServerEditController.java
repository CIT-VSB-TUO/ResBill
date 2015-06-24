package cz.vsb.resbill.web.servers;

import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;

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

import cz.vsb.resbill.dto.server.ServerEditDTO;
import cz.vsb.resbill.exception.ServerServiceException;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.ServerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from servers/serverEdit.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/servers/edit")
@SessionAttributes("serverEditDTO")
public class ServerEditController extends AbstractServerController {

	private static final Logger log = LoggerFactory.getLogger(ServerEditController.class);

	private static final String SERVER_EDIT_DTO_MODEL_KEY = "serverEditDTO";

	@Inject
	private ServerService serverService;

	@InitBinder
	public void initBinder(WebDataBinder binder, Locale locale) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private void loadServerEditDTO(Integer serverId, ModelMap model) {
		if (log.isDebugEnabled()) {
			log.debug("Requested server.id=" + serverId);
		}
		ServerEditDTO serverEditDTO = null;
		try {
			if (serverId != null) {
				serverEditDTO = serverService.findServerEditDTO(serverId);
			} else {
				serverEditDTO = new ServerEditDTO(new Server(), null);
			}
			model.addAttribute(SERVER_EDIT_DTO_MODEL_KEY, serverEditDTO);
		} catch (Exception e) {
			log.error("Cannot load server with id: " + serverId, e);

			model.addAttribute(SERVER_EDIT_DTO_MODEL_KEY, serverEditDTO);
			WebUtils.addGlobalError(model, SERVER_EDIT_DTO_MODEL_KEY, "error.load.server");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded serverEditDTO: " + serverEditDTO);
		}
	}

	/**
	 * Handles all GET requests. Binds loaded {@link ServerEditDTO} entity with the key "serverEditDTO" into a model.
	 * 
	 * @param serverId
	 *          key of a {@link ServerEditDTO} to view/edit
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String view(@RequestParam(value = "serverId", required = false) Integer serverId, ModelMap model) {
		loadServerEditDTO(serverId, model);

		return "servers/serverEdit";
	}

	/**
	 * Handles POST requests for saving edited {@link ServerEditDTO} instance.
	 * 
	 * @param server
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, params = "save")
	public String save(@Valid @ModelAttribute(SERVER_EDIT_DTO_MODEL_KEY) ServerEditDTO serverEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (log.isDebugEnabled()) {
			log.debug("ServerEditDTO to save: " + serverEditDTO);
		}
		if (!bindingResult.hasErrors()) {
			try {
				Server server = serverService.saveServer(serverEditDTO.getServer());
				if (log.isDebugEnabled()) {
					log.debug("Saved server: " + server);
				}
				redirectAttributes.addAttribute("serverId", server.getId());
				return "redirect:/servers/overview";
			} catch (ServerServiceException e) {
				switch (e.getReason()) {
				case NONUNIQUE_SERVER_ID:
					bindingResult.reject("error.save.server.constraint.unique.serverId");
					break;
				default:
					log.warn("Unsupported reason: " + e);
					bindingResult.reject("error.save.server");
					break;
				}
			} catch (Exception e) {
				log.error("Cannot save ServerEditDTO: " + serverEditDTO, e);
				bindingResult.reject("error.save.server");
			}
		} else {
			bindingResult.reject("error.save.server.validation");
		}
		return "servers/serverEdit";
	}

}
