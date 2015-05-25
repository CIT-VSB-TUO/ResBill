package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.dao.TariffDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.TariffServiceException;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.TariffService;

/**
 * An implementation of {@link TariffService} interface.
 * 
 * @author HAL191
 *
 */
@Service
@Transactional
public class TariffServiceImpl implements TariffService {

	private static final Logger log = LoggerFactory.getLogger(TariffServiceImpl.class);

	@Inject
	private TariffDAO tariffDAO;

	@Override
	public Tariff findTariff(Integer tariffId) throws ResBillException {
		try {
			return tariffDAO.findTariff(tariffId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding Tariff by id=" + tariffId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<Tariff> findTariffs(TariffCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return tariffDAO.findTariffs(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for Tariff entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Tariff saveTariff(Tariff tariff) throws ResBillException {
		try {
			return tariffDAO.saveTariff(tariff);
		} catch (Exception e) {
			log.error("An unexpected error occured while saving Tariff entity: " + tariff, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Tariff deleteTariff(Integer tariffId) throws TariffServiceException, ResBillException {
		try {
			// TODO chybi kontrola zavislosti
			Tariff tariff = tariffDAO.findTariff(tariffId);
			return tariffDAO.deleteTariff(tariff);
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting Tariff with id=" + tariffId, e);
			throw new ResBillException(e);
		}
	}
}
