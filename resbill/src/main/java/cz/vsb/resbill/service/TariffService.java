package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.TariffServiceException;
import cz.vsb.resbill.model.Tariff;

/**
 * A service for processing {@link Tariff} entities.
 * 
 * @author HAL191
 *
 */
public interface TariffService {

	/**
	 * Finds a {@link Tariff} with given key.
	 * 
	 * @param personId
	 *          primary key of a {@link Tariff}
	 * @return found {@link Tariff}, otherwise <code>null</code>
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Tariff findTariff(Integer tariffId) throws ResBillException;

	/**
	 * Finds a list of {@link Tariff} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @return list of found {@link Tariff} entities
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	List<Tariff> findTariffs(TariffCriteria criteria, Integer offset, Integer limit) throws ResBillException;

	/**
	 * Persists given {@link Tariff} entity.
	 * 
	 * @param person
	 *          entity to save
	 * @return saved {@link Tariff} entity (with generated primary key)
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Tariff saveTariff(Tariff tariff) throws ResBillException;

	/**
	 * Deletes {@link Tariff} entity with given primary key.
	 * 
	 * @param personId
	 *          key of deleted {@link Tariff} entity
	 * @return deleted {@link Tariff} entity
	 * @throws TariffServiceException
	 *           if specific deleting error occurs
	 * @throws ResBillException
	 *           if unexpected error occurs
	 */
	Tariff deleteTariff(Integer tariffId) throws TariffServiceException, ResBillException;
}