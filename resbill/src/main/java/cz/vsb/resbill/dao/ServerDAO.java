package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.model.Server;

public interface ServerDAO {

	Server findServer(Integer id);

	List<Server> findServers(ServerCriteria criteria);
}
