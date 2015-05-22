package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.dao.TariffDAO;
import cz.vsb.resbill.model.Tariff;
import cz.vsb.resbill.service.TariffService;

@Service
@Transactional
public class TariffServiceImpl implements TariffService {

	@Inject
	private TariffDAO tariffDAO;

	@Override
	public Tariff findTariff(Integer tariffId) {
		return tariffDAO.findTariff(tariffId);
	}

	@Override
	public List<Tariff> findTariffs(TariffCriteria criteria) {
		return tariffDAO.findTariffs(criteria);
	}

	@Override
	public Tariff saveTariff(Tariff tariff) {
		return tariffDAO.saveTariff(tariff);
	}

	@Override
	public Tariff deleteTariff(Integer tariffId) {
		Tariff tariff = tariffDAO.findTariff(tariffId);
		if (tariff != null) {
			tariff = tariffDAO.deleteTariff(tariff);
		}
		return tariff;
	}
}
