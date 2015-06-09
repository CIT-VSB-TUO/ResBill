package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractTariffCriteria;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.exception.ContractTariffServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractTariff;
import cz.vsb.resbill.service.ContractTariffService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link ContractTariffService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class ContractTariffServiceImpl implements ContractTariffService {

	private static final Logger log = LoggerFactory.getLogger(ContractTariffServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ContractTariffDAO contractTariffDAO;

	@Override
	public ContractTariff findContractTariff(Integer contractTariffId) throws ResBillException {
		try {
			return contractTariffDAO.findContractTariff(contractTariffId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ContractTariff by id=" + contractTariffId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<ContractTariff> findContractTariffs(ContractTariffCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return contractTariffDAO.findContractTariffs(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for ContractTariff entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractTariff saveContractTariff(ContractTariff contractTariff) throws ContractTariffServiceException, ResBillException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractTariff deleteContractTariff(Integer contractTariffId) throws ContractTariffServiceException, ResBillException {
		// TODO Auto-generated method stub
		return null;
	}
}
