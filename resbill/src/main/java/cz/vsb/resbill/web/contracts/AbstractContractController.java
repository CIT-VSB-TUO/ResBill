package cz.vsb.resbill.web.contracts;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import cz.vsb.resbill.dto.contract.ContractHeaderDTO;
import cz.vsb.resbill.service.ContractService;

public abstract class AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(AbstractContractController.class);

	protected static final String CONTRACT_HEADER_DTO_MODEL_KEY = "contractHeaderDTO";

	@Inject
	protected ContractService contractService;

	@ModelAttribute(CONTRACT_HEADER_DTO_MODEL_KEY)
	public ContractHeaderDTO getContractHeaderDTO(Integer contractId) {
		if (contractId != null) {
			try {
				return contractService.findContractHeaderDTO(contractId);
			} catch (Exception e) {
				log.error("Cannot load ContractHeaderDTO with id: " + contractId, e);
			}
		}
		return null;
	}

}
