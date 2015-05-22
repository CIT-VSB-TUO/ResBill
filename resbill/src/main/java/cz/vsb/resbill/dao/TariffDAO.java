package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.model.Tariff;

public interface TariffDAO {

	/**
	 * Finds a {@link Tariff} with given primary key.
	 * 
	 * @param id
	 *          primary key
	 * @return found {@link Tariff} entity
	 */
	Tariff findTariff(Integer id);

	/**
	 * Finds persons by specified criteria
	 * 
	 * @param criteria
	 * @return list of {@link Tariff} entities
	 */
	List<Tariff> findTariffs(TariffCriteria criteria);

	/**
	 * Saves current state of given {@link Tariff} entity.
	 * 
	 * @param tariff
	 *          entity to save
	 * @return saved entity
	 */
	Tariff saveTariff(Tariff tariff);

	/**
	 * Deletes given {@link Tariff} entity.
	 * 
	 * @param tariff
	 *          entity to delete
	 * @return deleted entity
	 */
	Tariff deleteTariff(Tariff tariff);
}
