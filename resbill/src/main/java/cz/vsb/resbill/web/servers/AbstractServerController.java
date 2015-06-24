package cz.vsb.resbill.web.servers;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.dto.server.ServerHeaderDTO;
import cz.vsb.resbill.service.ServerService;

public abstract class AbstractServerController {

	private static final Logger log = LoggerFactory.getLogger(AbstractServerController.class);

	private static final String SERVER_HEADER_DTO_MODEL_KEY = "serverHeaderDTO";

	@Inject
	private ServerService serverService;

	@ModelAttribute(SERVER_HEADER_DTO_MODEL_KEY)
	public ServerHeaderDTO getServerHeaderDTO(@RequestParam(value = "serverId", required = false) Integer serverId) {
		if (serverId != null) {
			try {
				return serverService.findServerHeaderDTO(serverId);
			} catch (Exception e) {
				log.error("Cannot load serverHeaderDTO with id: " + serverId, e);
			}
		}
		return null;
	}

}
