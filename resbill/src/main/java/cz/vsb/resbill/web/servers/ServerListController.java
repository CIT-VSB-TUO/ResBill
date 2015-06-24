package cz.vsb.resbill.web.servers;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.criteria.ServerCriteria.OrderBy;
import cz.vsb.resbill.dto.server.ServerListDTO;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.ServerService;
import cz.vsb.resbill.util.WebUtils;

/**
 * A controller for handling requests for/from servers/serverList.html page template.
 * 
 * @author HAL191
 *
 */
@Controller
@RequestMapping("/servers")
public class ServerListController {

	private static final Logger log = LoggerFactory.getLogger(ServerListController.class);

	private static final String SERVER_LIST_DTOS_MODEL_KEY = "serverListDTOs";

	@Inject
	private ServerService serverService;

	private void loadServerListDTOs(ModelMap model) {
		List<ServerListDTO> servers = null;
		try {
			ServerCriteria criteria = new ServerCriteria();
			criteria.setOrderBy(Arrays.asList(new OrderBy[] { OrderBy.SERVER_ID_ASC }));
			servers = serverService.findServerListDTOs(criteria, null, null);
			model.addAttribute(SERVER_LIST_DTOS_MODEL_KEY, servers);
		} catch (Exception e) {
			log.error("Cannot load list of servers.", e);

			model.addAttribute(SERVER_LIST_DTOS_MODEL_KEY, servers);
			WebUtils.addGlobalError(model, SERVER_LIST_DTOS_MODEL_KEY, "error.load.servers");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of servers size: " + (servers != null ? servers.size() : null));
		}
	}

	/**
	 * Handles all GET requests. Loads a list of all servers and binds it with the key "servers" into a model.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
	  loadServerListDTOs(model);

		return "servers/serverList";
	}
}
