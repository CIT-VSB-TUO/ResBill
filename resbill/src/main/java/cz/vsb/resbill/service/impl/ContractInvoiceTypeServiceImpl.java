package cz.vsb.resbill.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractInvoiceTypeCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.ContractInvoiceTypeDAO;
import cz.vsb.resbill.exception.ContractInvoiceTypeServiceException;
import cz.vsb.resbill.exception.ContractInvoiceTypeServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractInvoiceType;
import cz.vsb.resbill.model.Period;
import cz.vsb.resbill.service.ContractInvoiceTypeService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link ContractInvoiceTypeService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class ContractInvoiceTypeServiceImpl implements ContractInvoiceTypeService {

	private static final Logger log = LoggerFactory.getLogger(ContractInvoiceTypeServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ContractInvoiceTypeDAO contractInvoiceTypeDAO;

	@Inject
	private ContractDAO contractDAO;

	@Override
	public ContractInvoiceType findContractInvoiceType(Integer contractInvoiceTypeId) throws ResBillException {
		try {
			return contractInvoiceTypeDAO.findContractInvoiceType(contractInvoiceTypeId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ContractInvoiceType by id=" + contractInvoiceTypeId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<ContractInvoiceType> findContractInvoiceTypes(ContractInvoiceTypeCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return contractInvoiceTypeDAO.findContractInvoiceTypes(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for ContractInvoiceType entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractInvoiceType saveContractInvoiceType(ContractInvoiceType contractInvoiceType) throws ContractInvoiceTypeServiceException, ResBillException {
		try {
			// editovat lze pouze posledni prirazeni
			if (contractInvoiceType.getPeriod().getEndDate() != null) {
				throw new ContractInvoiceTypeServiceException(Reason.NOT_LAST_CONTRACT_INVOICE_TYPE);
			}
			// zjisteni kontraktu
			Contract contract = contractDAO.findContract(contractInvoiceType.getContract().getId());

			if (contractInvoiceType.getId() == null) { // nove prirazeni
				// zjisteni posledniho (aktualniho) prirazeni
				ContractInvoiceType lastCIT = contractInvoiceTypeDAO.findLastContractInvoiceType(contract.getId());
				if (lastCIT == null) {
					contractInvoiceType.setPrevious(null);
					// prvni prirazeni ma platnost od pocatku kontraktu
					contractInvoiceType.getPeriod().setBeginDate(contract.getPeriod().getBeginDate());
				} else {
					contractInvoiceType.setPrevious(lastCIT);
				}
			} else { // editace existujiciho prirazeni
				// kontrola modifikace prirazeni
				ContractInvoiceType origCIT = contractInvoiceTypeDAO.findContractInvoiceType(contractInvoiceType.getId());
				if (!origCIT.getContract().equals(contract) || !origCIT.getInvoiceType().equals(contractInvoiceType.getInvoiceType())) {
					throw new ContractInvoiceTypeServiceException(Reason.CONTRACT_INVOICE_TYPE_MODIFICATION);
				}
				if (contractInvoiceType.getPrevious() == null) {// prvni prirazeni
					// kontrola zmeny pocatecniho data
					if (!contractInvoiceType.getPeriod().getBeginDate().equals(contract.getPeriod().getBeginDate())) {
						throw new ContractInvoiceTypeServiceException(Reason.FIRST_CONTRACT_INVOICE_TYPE_BEGIN_DATE_MODIFICATION);
					}
				}
			}
			if (contractInvoiceType.getPrevious() != null) {
				// kontrola zda pocatek spada do platnosti kontraktu
				checkContractBelonging(contractInvoiceType);

				// ukonceni platnosti predchoziho prirazeni, je-li treba
				Date terminationDate = DateUtils.addDays(contractInvoiceType.getPeriod().getBeginDate(), -1);
				if (!terminationDate.equals(contractInvoiceType.getPrevious().getPeriod().getEndDate())) {
					terminateContractInvoiceType(contractInvoiceType.getPrevious(), terminationDate);
				}
			}

			return contractInvoiceTypeDAO.saveContractInvoiceType(contractInvoiceType);
		} catch (ContractInvoiceTypeServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving ContractInvoiceType entity: " + contractInvoiceType, e);
			throw new ResBillException(e);
		}
	}

	private void checkContractBelonging(ContractInvoiceType cit) throws ContractInvoiceTypeServiceException {
		if (!Period.isDateInPeriod(cit.getPeriod().getBeginDate(), cit.getContract().getPeriod())) {
			throw new ContractInvoiceTypeServiceException(Reason.OUT_OF_CONTRACT_DURATION);
		}
	}

	private void terminateContractInvoiceType(ContractInvoiceType cit, Date terminationDate) throws ContractInvoiceTypeServiceException {
		if (!Period.isValid(cit.getPeriod().getBeginDate(), terminationDate)) {
			throw new ContractInvoiceTypeServiceException(Reason.INVALID_PERIOD);
		}
		// provedeni ukonceni
		cit.getPeriod().setEndDate(terminationDate);
		contractInvoiceTypeDAO.saveContractInvoiceType(cit);
	}

	@Override
	public ContractInvoiceType deleteContractInvoiceType(Integer contractInvoiceTypeId) throws ContractInvoiceTypeServiceException, ResBillException {
		try {
			ContractInvoiceType contractInvoiceType = contractInvoiceTypeDAO.findContractInvoiceType(contractInvoiceTypeId);

			// nelze smazat neposledni prirazeni
			if (contractInvoiceType.getPeriod().getEndDate() != null) {
				throw new ContractInvoiceTypeServiceException(Reason.NOT_LAST_CONTRACT_INVOICE_TYPE);
			}

			if (contractInvoiceType.getPrevious() != null) {
				// zplatneni predchoziho prirazeni
				unterminateContractInvoiceType(contractInvoiceType.getPrevious());
			}

			return contractInvoiceTypeDAO.deleteContractInvoiceType(contractInvoiceType);
		} catch (ContractInvoiceTypeServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting ContractInvoiceType with id=" + contractInvoiceTypeId, e);
			throw new ResBillException(e);
		}
	}

	private void unterminateContractInvoiceType(ContractInvoiceType cit) {
		cit.getPeriod().setEndDate(null);
		contractInvoiceTypeDAO.saveContractInvoiceType(cit);
	}
}
