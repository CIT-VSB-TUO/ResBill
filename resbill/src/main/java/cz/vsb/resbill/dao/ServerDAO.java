package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.model.Server;

/**
 * Data access interface for {@link Server} model entity.
 * 
 * @author HAL191
 */
public interface ServerDAO {

	/**
	 * Finds a {@link Server} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link Server} entity
	 */
	Server findServer(Integer id);

	/**
	 * Finds a {@link Server} with given serverID.
	 * 
	 * @param serverId
	 *          natural identification of server
	 * @return found {@link Server} entity
	 */
	Server findServer(String serverId);

	/**
	 * Finds {@link Server} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link Server} entities
	 */
	List<Server> findServers(ServerCriteria criteria, Integer offset, Integer limit);

	/**
	 * 
	 * @param criteria
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Object[]> findServersForList(ServerCriteria criteria, Integer offset, Integer limit);
	
	/**
	 * Saves current state of given {@link Server} entity.
	 * 
	 * @param server
	 *          entity to save
	 * @return saved entity
	 */
	Server saveServer(Server server);

	/**
	 * Deletes given {@link Server} entity.
	 * 
	 * @param server
	 *          entity to delete
	 * @return deleted entity
	 */
	Server deleteServer(Server server);
}
