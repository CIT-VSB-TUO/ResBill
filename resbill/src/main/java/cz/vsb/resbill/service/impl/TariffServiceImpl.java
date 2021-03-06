package cz.vsb.resbill.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.dao.PriceListDAO;
import cz.vsb.resbill.dao.TariffDAO;
import cz.vsb.resbill.dto.tariff.TariffHeaderDTO;
import cz.vsb.resbill.dto.tariff.TariffListDTO;
import cz.vsb.resbill.dto.tariff.TariffOverviewDTO;
import cz.vsb.resbill.dto.tariff.TariffPriceListDTO;
import cz.vsb.resbill.exception.PriceListServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.exception.TariffServiceException;
import cz.vsb.resbill.exception.TariffServiceException.Reason;
import cz.vsb.resbill.model.ContractTariff;
import cz.vsb.resbill.model.PriceList;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.PriceListService;
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

	@PersistenceContext
	private EntityManager em;

	@Inject
	private TariffDAO tariffDAO;

	@Inject
	private ContractTariffDAO contractTariffDAO;

	@Inject
	private PriceListDAO priceListDAO;

	@Inject
	private PriceListService priceListService;

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
	public TariffHeaderDTO findTariffHeaderDTO(Integer tariffId) throws ResBillException {
		try {
			return new TariffHeaderDTO(findTariff(tariffId));
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding TariffHeaderDTO by id=" + tariffId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public TariffOverviewDTO findTariffOverviewDTO(Integer tariffId) throws ResBillException {
		try {
			Tariff tariff = findTariff(tariffId);
			PriceList pl = priceListDAO.findLastPriceList(tariffId);
			return new TariffOverviewDTO(tariff, pl);
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while finding TariffOverviewDTO by id=" + tariffId, e);
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
	public List<TariffListDTO> findTariffListDTOs() throws ResBillException {
		try {
			StringBuilder jpql = new StringBuilder();
			jpql.append("SELECT tariff, pl FROM Tariff AS tariff");
			jpql.append(" JOIN tariff.prices AS pl");
			jpql.append(" WHERE pl.period.endDate IS NULL");
			jpql.append(" ORDER BY tariff.name");

			Query query = em.createQuery(jpql.toString());
			List<?> results = query.getResultList();

			List<TariffListDTO> dtos = new ArrayList<TariffListDTO>(results.size());
			for (Object result : results) {
				Object[] row = (Object[]) result;
				dtos.add(new TariffListDTO((Tariff) row[0], (PriceList) row[1]));
			}

			return dtos;
		} catch (Exception e) {
			log.error("An error occured while searching for TariffPriceListDTO instances", e);
			throw new ResBillException(e);
		}
	}

	@Override
	public TariffPriceListDTO findTariffPriceListDTO(Integer tariffId) throws ResBillException {
		try {
			Tariff tariff = findTariff(tariffId);
			PriceList priceList = priceListDAO.findLastPriceList(tariffId);

			return new TariffPriceListDTO(tariff, priceList);
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occured while finding TariffPriceListDTO by Tariff.id=" + tariffId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public Tariff saveTariffPriceListDTO(TariffPriceListDTO dto) throws PriceListServiceException, ResBillException {
		try {
			Tariff tariff = tariffDAO.saveTariff(dto.getTariff());
			if (dto.isLastPriceListEditable()) {
				priceListService.savePriceList(dto.getLastPriceList());
			}
			return tariff;
		} catch (PriceListServiceException e) {
			throw e;
		} catch (ResBillException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving TariffPriceListDTO: " + dto, e);
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
				throw new TariffServiceException(Reason.CONTRACT_ASSOCIATED);
			}

			// tarif smazan i s ceniky (kaskada)
			return tariffDAO.deleteTariff(tariff);
		} catch (TariffServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting Tariff with id=" + tariffId, e);
			throw new ResBillException(e);
		}
	}
}
