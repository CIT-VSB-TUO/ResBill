package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Server;
import cz.vsb.resbill.service.ResBillService;
import cz.vsb.resbill.service.ServerService;

/**
 * An implementation of {@link ServerService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class ServerServiceImpl implements ServerService {

	private static final Logger log = LoggerFactory.getLogger(ServerServiceImpl.class);

	@Inject
	private ServerDAO serverDAO;

	@Override
	public Server findServer(Integer serverId) throws ResBillException {
		try {
			return serverDAO.findServer(serverId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding Server by id=" + serverId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<Server> findServers(ServerCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return serverDAO.findServers(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for Server entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

}
