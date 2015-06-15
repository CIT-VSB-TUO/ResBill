package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.dto.ServerAgendaDTO;
import cz.vsb.resbill.dto.ServerDTO;
import cz.vsb.resbill.dto.ServerEditDTO;
import cz.vsb.resbill.dto.ServerHeaderDTO;
import cz.vsb.resbill.dto.ServerOverviewDTO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.ServerServiceException;
import cz.vsb.resbill.model.Server;

/**
 * A service for processing {@link Server} entities.
 * 
 * @author HAL191
 *
 */
public interface ServerService {

  /**
   * Finds a {@link Server} with given key.
   * 
   * @param serverId
   *          primary key of a {@link Server}
   * @return found {@link Server}, otherwise <code>null</code>
   * @throws ResBillException
   *           if unexpected error occurs
   */
  Server findServer(Integer serverId) throws ResBillException;

  ServerDTO findServerDTO(Integer serverId) throws ResBillException;

  ServerHeaderDTO findServerHeaderDTO(Integer serverId) throws ResBillException;

  ServerOverviewDTO findServerOverviewDTO(Integer serverId) throws ResBillException;

  /**
   * Finds a {@link ServerEditDTO} for given key.
   * 
   * @param serverId
   *          primary key of a {@link Server}
   * @return found {@link ServerEditDTO}, otherwise <code>null</code>
   * @throws ResBillException
   *           if unexpected error occurs
   */
  ServerEditDTO findServerEditDTO(Integer serverId) throws ResBillException;

  /**
   * Finds a list of {@link Server} entities according to given criteria.
   * 
   * @param criteria
   *          filtering criteria
   * @param offset
   * @param limit
   * @return list of found {@link Server} entities
   * @throws ResBillException
   *           if unexpected error occurs
   */
  List<Server> findServers(ServerCriteria criteria, Integer offset, Integer limit) throws ResBillException;

  List<ServerDTO> findServerDTOs(ServerCriteria criteria, Integer offset, Integer limit) throws ResBillException;

  List<ServerAgendaDTO> findServerAgendaDTOs(ServerCriteria criteria, Integer offset, Integer limit) throws ResBillException;

  /**
   * Persists given {@link Server} entity.
   * 
   * @param server
   *          entity to save
   * @return saved {@link Server} entity (with generated primary key)
   * @throws ServerServiceException
   *           if specific error occurs
   * @throws ResBillException
   *           if unexpected error occurs
   */
  Server saveServer(Server server) throws ServerServiceException, ResBillException;

  /**
   * Deletes {@link Server} entity with given primary key.
   * 
   * @param serverId
   *          key of deleted {@link Server} entity
   * @return deleted {@link Server} entity
   * @throws ServerServiceException
   *           if specific error occurs
   * @throws ResBillException
   *           if unexpected error occurs
   */
  Server deleteServer(Integer serverId) throws ServerServiceException, ResBillException;
}
