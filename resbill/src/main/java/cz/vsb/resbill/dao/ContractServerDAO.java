package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.model.ContractServer;
import cz.vsb.resbill.model.Server;

/**
 * Data access interface for {@link ContractServer} model entity.
 * 
 * @author HAL191
 */
public interface ContractServerDAO {

	/**
	 * Finds a {@link ContractServer} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link ContractServer} entity
	 */
	ContractServer findContractServer(Integer contractServerId, boolean fetchContract, boolean fetchServer);

	/**
	 * Finds a {@link ContractServer} currently associated with given {@link Server}.
	 * 
	 * @param serverId
	 * @return found {@link ContractServer} entity
	 */
	ContractServer findCurrentContractServer(Integer serverId);

	/**
	 * Finds {@link ContractServer} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link ContractServer} entities
	 */
	List<ContractServer> findContractServers(ContractServerCriteria criteria, Integer offset, Integer limit);

	/**
	 * Saves current state of given {@link ContractServer} entity.
	 * 
	 * @param contractServer
	 *          entity to save
	 * @return saved entity
	 */
	ContractServer saveContractServer(ContractServer contractServer);

	/**
	 * Deletes given {@link ContractServer} entity.
	 * 
	 * @param contractServer
	 *          entity to delete
	 * @return deleted entity
	 */
	ContractServer deleteContractServer(ContractServer contractServer);
}
