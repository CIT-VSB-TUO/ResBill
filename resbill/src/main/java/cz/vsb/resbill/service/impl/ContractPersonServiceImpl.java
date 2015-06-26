package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;

import cz.vsb.resbill.criteria.ContractPersonCriteria;
import cz.vsb.resbill.dao.ContractDAO;
import cz.vsb.resbill.dao.ContractPersonDAO;
import cz.vsb.resbill.exception.ContractPersonServiceException;
import cz.vsb.resbill.exception.ContractPersonServiceException.Reason;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.model.ContractPerson;
import cz.vsb.resbill.service.ContractPersonService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link ContractPersonService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class ContractPersonServiceImpl implements ContractPersonService {

	private static final Logger log = LoggerFactory.getLogger(ContractPersonServiceImpl.class);

	@Inject
	private ContractPersonDAO contractPersonDAO;

	@Inject
	private ContractDAO contractDAO;

	@Override
	public ContractPerson findContractPerson(Integer contractPersonId) throws ResBillException {
		try {
			return contractPersonDAO.findContractPerson(contractPersonId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding ContractPerson by id=" + contractPersonId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<ContractPerson> findContractPersons(ContractPersonCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return contractPersonDAO.findContractPersons(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for ContractPerson entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractPerson saveContractPerson(ContractPerson contractPerson) throws ContractPersonServiceException, ResBillException {
		try {
			// kontrola jedinecnosti prirazeni
			ContractPersonCriteria cpCriteria = new ContractPersonCriteria();
			cpCriteria.setContractId(contractPerson.getContract().getId());
			cpCriteria.setPersonId(contractPerson.getPerson().getId());
			List<ContractPerson> cps = contractPersonDAO.findContractPersons(cpCriteria, null, null);
			ContractPerson foundCP = DataAccessUtils.singleResult(cps);
			if (foundCP != null && !foundCP.getId().equals(contractPerson.getId())) {
				throw new ContractPersonServiceException(Reason.NONUNIQUE_CONTRACT_PERSON);
			}

			if (contractPerson.getId() != null) {
				// zjisteni kontraktu
				Contract contract = contractDAO.findContract(contractPerson.getContract().getId());
				// kontrola modifikace prirazeni
				ContractPerson origCP = contractPersonDAO.findContractPerson(contractPerson.getId());
				if (!origCP.getContract().equals(contract) || !origCP.getPerson().equals(contractPerson.getPerson())) {
					throw new ContractPersonServiceException(Reason.CONTRACT_PERSON_MODIFICATION);
				}
			}

			return contractPersonDAO.saveContractPerson(contractPerson);
		} catch (ContractPersonServiceException e) {
			throw e;
		} catch (Exception e) {
			log.error("An unexpected error occured while saving ContractPerson entity: " + contractPerson, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public ContractPerson deleteContractPerson(Integer contractPersonId) throws ContractPersonServiceException, ResBillException {
		try {
			ContractPerson contractPerson = contractPersonDAO.findContractPerson(contractPersonId);
			return contractPersonDAO.deleteContractPerson(contractPerson);
		} catch (Exception e) {
			log.error("An unexpected error occured while deleting ContractPerson with id=" + contractPersonId, e);
			throw new ResBillException(e);
		}
	}

}
