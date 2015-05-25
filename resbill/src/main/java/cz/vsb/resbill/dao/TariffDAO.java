package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.model.Tariff;

/**
 * 
 * Data acces interface for {@link Tariff} model entity.
 * 
 * @author HAL191
 *
 */
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
	 * Finds {@link Tariff} entities by specified criteria
	 * 
	 * @param criteria
	 * @param offset
	 * @param limit
	 * @return list of {@link Tariff} entities
	 */
	List<Tariff> findTariffs(TariffCriteria criteria, Integer offset, Integer limit);

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
