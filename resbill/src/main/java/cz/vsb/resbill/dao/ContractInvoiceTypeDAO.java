package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ContractInvoiceTypeCriteria;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractInvoiceType;

/**
 * Data access interface for {@link ContractInvoiceType} model entity.
 * 
 * @author HAL191
 */
public interface ContractInvoiceTypeDAO {

	/**
	 * Finds a {@link ContractInvoiceType} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link ContractInvoiceType} entity
	 */
	ContractInvoiceType findContractInvoiceType(Integer contractInvoiceTypeId);

	/**
	 * Finds the first {@link ContractInvoiceType} of {@link Contract} specified by the given key.
	 * 
	 * @param contractId
	 * @return
	 */
	ContractInvoiceType findFirstContractInvoiceType(Integer contractId);

	/**
	 * Finds the last (current) {@link ContractInvoiceType} of {@link Contract} specified by the given key.
	 * 
	 * @param contractId
	 * @return
	 */
	ContractInvoiceType findLastContractInvoiceType(Integer contractId);

	/**
	 * Finds {@link ContractInvoiceType} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link ContractInvoiceType} entities
	 */
	List<ContractInvoiceType> findContractInvoiceTypes(ContractInvoiceTypeCriteria criteria, Integer offset, Integer limit);

	/**
	 * Saves current state of given {@link ContractInvoiceType} entity.
	 * 
	 * @param contractInvoiceType
	 *          entity to save
	 * @return saved entity
	 */
	ContractInvoiceType saveContractInvoiceType(ContractInvoiceType contractInvoiceType);

	/**
	 * Deletes given {@link ContractInvoiceType} entity.
	 * 
	 * @param contractInvoiceType
	 *          entity to delete
	 * @return deleted entity
	 */
	ContractInvoiceType deleteContractInvoiceType(ContractInvoiceType contractInvoiceType);
}
