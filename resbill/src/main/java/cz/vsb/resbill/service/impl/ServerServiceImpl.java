package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.dao.ContractServerDAO;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.dto.ServerEditDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.ServerServiceException;
import cz.vsb.resbill.exception.ServerServiceException.Reason;
import cz.vsb.resbill.model.ContractServer;
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

	@Inject
	private ContractServerDAO contractServerDAO;

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
	public ServerEditDTO findServerEditDTO(Integer serverId) throws ResBillException {
		try {
			Server server = findServer(serverId);
			ContractServer cs = contractServerDAO.findCurrentContractServer(serverId);

			return new ServerEditDTO(server, cs);
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ServerEditDTO by server.id=" + serverId, e);
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

	@Override
	public Server saveServer(Server server) throws ServerServiceException, ResBillException {
		// TODO Auto-generated method stub
		try {
			// kontrola jedinecnosti serverId
			ServerCriteria criteria = new ServerCriteria();
			criteria.setServerId(server.getServerId());
			List<Server> servers = serverDAO.findServers(criteria, null, null);
			Server foundServer = DataAccessUtils.singleResult(servers);
			if (foundServer != null && !foundServer.getId().equals(server.getId())) {
				throw new ServerServiceException(Reason.NONUNIQUE_SERVER_ID);
			}

			return serverDAO.saveServer(server);
		} catch (ServerServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving Server entity: " + server, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Server deleteServer(Integer serverId) throws ServerServiceException, ResBillException {
		try {
			Server server = serverDAO.findServer(serverId);
			// kontrola zavislosti
			// kontrakty
			ContractServerCriteria csCriteria = new ContractServerCriteria();
			csCriteria.setServerId(serverId);
			List<ContractServer> css = contractServerDAO.findContractServers(csCriteria, null, null);
			if (!css.isEmpty()) {
				throw new ServerServiceException(Reason.CONTRACT_ASSOCIATED);
			}
			// denni pouziti
			if (!server.getDailyUsages().isEmpty()) { // TODO prepsat do DAO?
				throw new ServerServiceException(Reason.DAILY_USAGE_EXISTENCE);
			}
			return serverDAO.deleteServer(server);
		} catch (ServerServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting Server with id=" + serverId, e);
			throw new ResBillException(e);
		}
	}
}
