package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.InvoiceType;

/**
 * A service for processing {@link InvoiceType} entities.
 * 
 * @author HAL191
 *
 */
public interface InvoiceTypeService {

	/**
	 * Finds a {@link InvoiceType} with given key.
	 * 
	 * @param invoiceTypeId
	 *          primary key of a {@link InvoiceType}
	 * @return found {@link InvoiceType}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	InvoiceType findInvoiceType(Integer invoiceTypeId) throws ResBillException;

	/**
	 * Finds a list of {@link InvoiceType} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @return list of found {@link InvoiceType} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<InvoiceType> findInvoiceTypes(InvoiceTypeCriteria criteria, Integer offset, Integer limit) throws ResBillException;

}
