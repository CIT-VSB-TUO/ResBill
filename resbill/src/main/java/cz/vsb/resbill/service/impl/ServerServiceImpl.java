package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.dao.ContractServerDAO;
import cz.vsb.resbill.dao.DailyUsageDAO;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.dto.DailyUsageDTO;
import cz.vsb.resbill.dto.agenda.ServerAgendaDTO;
import cz.vsb.resbill.dto.contract.ContractDTO;
import cz.vsb.resbill.dto.server.ServerEditDTO;
import cz.vsb.resbill.dto.server.ServerHeaderDTO;
import cz.vsb.resbill.dto.server.ServerListDTO;
import cz.vsb.resbill.dto.server.ServerOverviewDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.ServerServiceException;
import cz.vsb.resbill.exception.ServerServiceException.Reason;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.DailyUsage;
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

	@Inject
	private DailyUsageDAO dailyUsageDAO;

	/**
	 * 
	 */
	@Override
	public Server findServer(Integer serverId) throws ResBillException {
		try {
			return serverDAO.findServer(serverId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding Server by id=" + serverId, e);
			throw new ResBillException(e);
		}
	}

	/**
   * 
   */
	public ServerHeaderDTO findServerHeaderDTO(Integer serverId) throws ResBillException {
		try {
			Server server = findServer(serverId);

			return new ServerHeaderDTO(server);
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ServerHeaderDTO by server.id=" + serverId, e);
			throw new ResBillException(e);
		}
	}

	/**
   * 
   */
	public ServerOverviewDTO findServerOverviewDTO(Integer serverId) throws ResBillException {
		try {
			Server server = findServer(serverId);

			ServerOverviewDTO dto = new ServerOverviewDTO(server);

			DailyUsage lastDailyUsage = dailyUsageDAO.findServerLastDailyUsage(server.getId());
			if (lastDailyUsage != null) {
				DailyUsageDTO lastDailyUsageDTO = new DailyUsageDTO(lastDailyUsage);
				dto.setLastDailyUsageDTO(lastDailyUsageDTO);
			}

			ContractServer currentContractServer = contractServerDAO.findCurrentContractServer(serverId);
			if (currentContractServer != null) {
				dto.fill(currentContractServer);
				ContractDTO currentContractDTO = new ContractDTO(currentContractServer.getContract());
				dto.setCurrentContractDTO(currentContractDTO);
			}

			return dto;
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ServerOverviewDTO by server.id=" + serverId, e);
			throw new ResBillException(e);
		}
	}

	/**
	 * 
	 */
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

	/**
	 * 
	 */
	@Override
	public List<Server> findServers(ServerCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return serverDAO.findServers(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for Server entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	/**
   * 
   */
	@Override
	public List<ServerListDTO> findServerListDTOs(ServerCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			List<Object[]> fieldsList = serverDAO.findServersForList(criteria, offset, limit);
			List<ServerListDTO> dtos = new ArrayList<ServerListDTO>();

			for (Object[] fields : fieldsList) {
				ServerListDTO dto = new ServerListDTO(fields);
				dtos.add(dto);
			}

			return dtos;
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding ServerListDTOs.", exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * Nalezne servery pro pouziti na Dashboardu (Agenda).
	 * 
	 * Tj. nalezne vsechny servery, ktere splnuji alespon jednu pozadovanou vlastnost (Feature) - tim se lisi od standardni metody findServers.
	 * 
	 * @param criteria
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Override
	public List<ServerAgendaDTO> findServerAgendaDTOs(ServerCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			ServerCriteria crit = null;
			List<Server> servers = null;
			Set<Integer> serverIds = new HashSet<Integer>();

			// Servery odebírající zdroje mimo období pokryte kontrakty
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ServerCriteria.Feature.DAILY_USAGE_OUT_OF_CONTRACT));
			servers = findServers(crit, null, null);
			Set<Integer> outOfContractServerIds = new HashSet<Integer>();
			for (Server server : servers) {
				outOfContractServerIds.add(server.getId());
			}
			serverIds.addAll(outOfContractServerIds);

			// Servery bez kontraktu
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ServerCriteria.Feature.NO_CONTRACT));
			servers = findServers(crit, null, null);
			Set<Integer> noContractServerIds = new HashSet<Integer>();
			for (Server server : servers) {
				noContractServerIds.add(server.getId());
			}
			serverIds.addAll(noContractServerIds);

			// Servery bez denniho vyuziti zdroju
			crit = criteria.clone();
			crit.setFeatures(EnumSet.of(ServerCriteria.Feature.NO_DAILY_USAGE));
			servers = findServers(crit, null, null);
			Set<Integer> noDailyUsagesServerIds = new HashSet<Integer>();
			for (Server server : servers) {
				noDailyUsagesServerIds.add(server.getId());
			}
			serverIds.addAll(noDailyUsagesServerIds);

			// Pokud neexistuje zadny "zajimavy" kontrakt, pak vratim prazdny seznam
			if (serverIds.isEmpty()) {
				return new ArrayList<ServerAgendaDTO>();
			}

			// Ziskam vsechny "zajimave" kontrakty setridene dle puvodniho pozadavku
			crit = new ServerCriteria();
			crit.setServerIds(serverIds);
			crit.setOrderBy(criteria.getOrderBy());

			servers = findServers(crit, offset, limit);
			List<ServerAgendaDTO> dtos = new ArrayList<ServerAgendaDTO>();

			for (Server server : servers) {
				ServerAgendaDTO dto = new ServerAgendaDTO();
				dto.fill(server);
				dtos.add(dto);

				if (outOfContractServerIds.contains(server.getId())) {
					dto.setDailyUsagesOutOfContract(true);
				}
				if (noContractServerIds.contains(server.getId())) {
					dto.setNoContract(true);
				}
				if (noDailyUsagesServerIds.contains(server.getId())) {
					dto.setNoDailyUsage(true);
				}
			}

			return dtos;
		} catch (Exception exc) {
			log.error("An unexpected error occured while finding ServerAgendaDTOs.", exc);
			throw new ResBillException(exc);
		}
	}

	/**
	 * 
	 */
	@Override
	public Server saveServer(Server server) throws ServerServiceException, ResBillException {
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

	/**
	 * 
	 */
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
			DailyUsage dailyUsage = dailyUsageDAO.findServerLastDailyUsage(serverId);
			if (dailyUsage != null) {
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
