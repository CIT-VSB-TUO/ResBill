package cz.vsb.resbill.dao;

import java.util.List;

import cz.vsb.resbill.criteria.ContractPersonCriteria;
import cz.vsb.resbill.model.ContractPerson;

public interface ContractPersonDAO {

	ContractPerson findContractPerson(Integer contractPersonId);

	List<ContractPerson> findContractPersons(ContractPersonCriteria criteria, Integer offset, Integer limit);
}
