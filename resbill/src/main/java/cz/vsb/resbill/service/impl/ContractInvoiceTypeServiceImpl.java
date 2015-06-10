package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.ContractInvoiceTypeCriteria;
import cz.vsb.resbill.dao.ContractInvoiceTypeDAO;
import cz.vsb.resbill.exception.ContractInvoiceTypeServiceException;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.ContractInvoiceType;
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
		// TODO doplnit kontroly
		try {
			return contractInvoiceTypeDAO.saveContractInvoiceType(contractInvoiceType);
			// } catch (ContractInvoiceTypeServiceException e) {
			// throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving ContractInvoiceType entity: " + contractInvoiceType, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractInvoiceType deleteContractInvoiceType(Integer contractInvoiceTypeId) throws ContractInvoiceTypeServiceException, ResBillException {
		// TODO doplnit kontroly
		try {
			ContractInvoiceType contractInvoiceType = contractInvoiceTypeDAO.findContractInvoiceType(contractInvoiceTypeId);
			return contractInvoiceTypeDAO.deleteContractInvoiceType(contractInvoiceType);
			// } catch (ContractInvoiceTypeServiceException e) {
			// throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting ContractInvoiceType with id=" + contractInvoiceTypeId, e);
			throw new ResBillException(e);
		}
	}
}
