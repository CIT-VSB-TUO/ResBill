package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.exception.ResBillException;
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
}
