package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.ContractInvoiceTypeCriteria;
import cz.vsb.resbill.exception.ContractInvoiceTypeServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractInvoiceType;

/**
 * A service for processing {@link ContractInvoiceType} entities.
 * 
 * @author HAL191
 *
 */
public interface ContractInvoiceTypeService {

	/**
	 * Finds a {@link ContractInvoiceType} with given key.
	 * 
	 * @param contractInvoiceTypeId
	 *          primary key of a {@link ContractInvoiceType}
	 * @return found {@link ContractInvoiceType}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractInvoiceType findContractInvoiceType(Integer contractInvoiceTypeId) throws ResBillException;

	/**
	 * Finds a list of {@link ContractInvoiceType} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link ContractInvoiceType} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<ContractInvoiceType> findContractInvoiceTypes(ContractInvoiceTypeCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link ContractInvoiceType} entity.
	 * 
	 * @param contractInvoiceType
	 *          entity to save
	 * @return saved {@link ContractInvoiceType} entity (with generated primary key)
	 * @throws ContractInvoiceTypeServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractInvoiceType saveContractInvoiceType(ContractInvoiceType contractInvoiceType) throws ContractInvoiceTypeServiceException, ResBillException;

	/**
	 * Deletes {@link ContractInvoiceType} entity with given primary key.
	 * 
	 * @param contractInvoiceTypeId
	 *          key of deleted {@link ContractInvoiceType} entity
	 * @return deleted {@link ContractInvoiceType} entity
	 * @throws ContractInvoiceTypeServiceException
	 *           if specific error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	ContractInvoiceType deleteContractInvoiceType(Integer contractInvoiceTypeId) throws ContractInvoiceTypeServiceException, ResBillException;
}
