package cz.vsb.resbill.service;

import java.util.List;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.model.Tariff;

public interface TariffService {

	/**
	 * Finds a {@link Tariff} with given key.
	 * 
	 * @param personId
	 *          primary key of a {@link Tariff}
	 * @return found {@link Tariff}, otherwise <code>null</code>
	 */
	Tariff findTariff(Integer tariffId);

	/**
	 * Finds a list of {@link Tariff} entities according to given criteria.
	 * 
	 * @param criteria
	 *          filtering criteria
	 * @return list of found {@link Tariff} entities
	 */
	List<Tariff> findTariffs(TariffCriteria criteria);

	/**
	 * Persists given {@link Tariff} entity.
	 * 
	 * @param person
	 *          entity to save
	 * @return saved {@link Tariff} entity (with generated primary key)
	 */
	Tariff saveTariff(Tariff tariff);

	/**
	 * Deletes {@link Tariff} entity with given primary key.
	 * 
	 * @param personId
	 *          key of deleted {@link Tariff} entity
	 * @return deleted {@link Tariff} entity
	 */
	Tariff deleteTariff(Integer tariffId);
}
