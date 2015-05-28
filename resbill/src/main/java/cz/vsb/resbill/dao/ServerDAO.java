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

	Server findServer(Integer id);

	Server findServer(String serverServerId);

	List<Server> findServers(ServerCriteria criteria);

	Server saveServer(Server server);
}
