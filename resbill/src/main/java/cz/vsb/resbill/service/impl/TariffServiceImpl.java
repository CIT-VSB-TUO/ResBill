package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.dao.TariffDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.TariffServiceException;
import cz.vsb.resbill.exception.TariffServiceException.Reason;
import cz.vsb.resbill.model.ContractTariff;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.ResBillService;
import cz.vsb.resbill.service.TariffService;

/**
 * An implementation of {@link TariffService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class TariffServiceImpl implements TariffService {

	private static final Logger log = LoggerFactory.getLogger(TariffServiceImpl.class);

	@Inject
	private TariffDAO tariffDAO;

	@Inject
	private ContractTariffDAO contractTariffDAO;

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
			Tariff tariff = tariffDAO.findTariff(tariffId);

			// kontrola zavislosti
			// prirazeni ke kontraktu
			ContractTariffCriteria ctCriteria = new ContractTariffCriteria();
			ctCriteria.setTariffId(tariffId);
			List<ContractTariff> contractTariffs = contractTariffDAO.findContractTariffs(ctCriteria, null, null);
			if (!contractTariffs.isEmpty()) {
				throw new TariffServiceException(Reason.CONTRACT_TARIFF);
			}

			return tariffDAO.deleteTariff(tariff);
		} catch (TariffServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting Tariff with id=" + tariffId, e);
			throw new ResBillException(e);
		}
	}
}
