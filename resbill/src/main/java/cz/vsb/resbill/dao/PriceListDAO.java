package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.model.PriceList;

/**
 * Data access interface for {@link PriceList} model entity.
 * 
 * @author HAL191
 *
 */
public interface PriceListDAO {

	/**
	 * Finds a {@link PriceList} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link PriceList} entity
	 */
	PriceList findPriceList(Integer id);

	/**
	 * Finds {@link PriceList} entities by specified criteria
	 * 
	 * @param criteria
	 * @param offset
	 * @param limit
	 * @return list of {@link PriceList} entities
	 */
	List<PriceList> findPriceLists(PriceListCriteria criteria, Integer offset, Integer limit);

	/**
	 * Saves current state of given {@link PriceList} entity.
	 * 
	 * @param priceList
	 *          entity to save
	 * @return saved entity
	 */
	PriceList savePriceList(PriceList priceList);
}
