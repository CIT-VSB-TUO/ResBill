package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.model.Tariff;

public interface TariffDAO {

	Tariff findTariff(Integer id);

	List<Tariff> findTariffs(TariffCriteria criteria);
}
