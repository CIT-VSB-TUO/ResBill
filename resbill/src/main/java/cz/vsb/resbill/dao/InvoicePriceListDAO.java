package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.InvoicePriceListCriteria;
import cz.vsb.resbill.model.InvoicePriceList;

/**
 * Data access interface for {@link InvoicePriceList} model entity.
 * 
 * @author HAL191
 */
public interface InvoicePriceListDAO {

	/**
	 * Finds a {@link InvoicePriceList} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link InvoicePriceList} entity
	 */
	InvoicePriceList findInvoicePriceList(Integer invoicePriceListId);

	/**
	 * Finds {@link InvoicePriceList} entities by specified criteria
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 *          order number of first result to return
	 * @param limit
	 *          maximum number of results to return
	 * @return list of {@link InvoicePriceList} entities
	 */
	List<InvoicePriceList> findInvoicePriceLists(InvoicePriceListCriteria criteria, Integer offset, Integer limit);
}
