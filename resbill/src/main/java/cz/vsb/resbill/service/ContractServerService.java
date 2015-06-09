package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.exception.ContractServerServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractServer;

/**
 * A service for processing {@link ContractServer} entities.
 * 
 * @author HAL191
 *
 */
public interface ContractServerService {

	/**
	 * Finds a {@link ContractServer} with given key.
	 * 
	 * @param contractServerId
	 *          primary key of a {@link ContractServer}
	 * @return found {@link ContractServer}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractServer findContractServer(Integer contractServerId) throws ResBillException;

	/**
	 * Finds a list of {@link ContractServer} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link ContractServer} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<ContractServer> findContractServers(ContractServerCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link ContractServer} entity.
	 * 
	 * @param contractServer
	 *          entity to save
	 * @return saved {@link ContractServer} entity (with generated primary key)
	 * @throws ContractServerServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractServer saveContractServer(ContractServer contractServer) throws ContractServerServiceException, ResBillException;

	/**
	 * Deletes {@link ContractServer} entity with given primary key.
	 * 
	 * @param contractServerId
	 *          key of deleted {@link ContractServer} entity
	 * @return deleted {@link ContractServer} entity
	 * @throws ContractServerServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractServer deleteContractServer(Integer contractServerId) throws ContractServerServiceException, ResBillException;
}
