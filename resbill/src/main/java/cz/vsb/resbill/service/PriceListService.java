package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.dto.tariff.PriceListDTO;
import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.PriceList;

/**
 * A service for processing {@link PriceList} entities.
 * 
 * @author HAL191
 *
 */
public interface PriceListService {

	/**
	 * Finds a {@link PriceList} with given key.
	 * 
	 * @param personId
	 *          primary key of a {@link PriceList}
	 * @return found {@link PriceList}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	PriceList findPriceList(Integer priceListId) throws ResBillException;

	/**
	 * Finds a list of {@link PriceList} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link PriceList} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<PriceList> findPriceLists(PriceListCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Finds a list of {@link PriceListDTO} instances according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @param offset
	 * @param limit
	 * @return list of found {@link PriceListDTO} instances
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<PriceListDTO> findPriceListDTOs(PriceListCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link PriceList} entity.
	 * 
	 * @param priceList
	 *          entity to save
	 * @return saved {@link PriceList} entity (with generated primary key)
	 * @throws PriceListServiceException
	 *           if specific saving error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	PriceList savePriceList(PriceList priceList) throws PriceListServiceException, ResBillException;

	/**
	 * Deletes {@link PriceList} entity with given primary key.
	 * 
	 * @param priceListId
	 *          key of deleted {@link PriceList} entity
	 * @return deleted {@link PriceList} entity
	 * @throws PriceListServiceException
	 *           if specific deleting error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	PriceList deletePriceList(Integer priceListId) throws PriceListServiceException, ResBillException;

}
