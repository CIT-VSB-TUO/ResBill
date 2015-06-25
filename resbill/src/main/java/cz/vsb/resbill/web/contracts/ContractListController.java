/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.contracts;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.vsb.resbill.criteria.ContractCriteria;
import cz.vsb.resbill.dto.contract.ContractListDTO;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/contracts")
public class ContractListController {

	private static final Logger log = LoggerFactory.getLogger(ContractListController.class);

	public static final String MODEL_OBJECT_KEY_CONTRACTS = "contracts";

	@Inject
	private ContractService contractService;

	/**
	 * 
	 * @param model
	 * @param msgKey
	 */
	protected static void addGlobalError(ModelMap model, String msgKey) {
		WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_CONTRACTS, msgKey);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(ModelMap model) {
		loadContractListDTOs(model);

		return "contracts/contractList";
	}

	private void loadContractListDTOs(ModelMap model) {
		List<ContractListDTO> dtos = null;

		try {
			ContractCriteria criteria = new ContractCriteria();
			criteria.setOrderBy(Collections.singletonList(ContractCriteria.OrderBy.EVIDENCE_NUMBER_ASC));

			dtos = contractService.findContractListDTOs(criteria, null, null);

			model.addAttribute(MODEL_OBJECT_KEY_CONTRACTS, dtos);
		} catch (Exception exc) {
			log.error("Cannot load Contracts.", exc);

			model.addAttribute(MODEL_OBJECT_KEY_CONTRACTS, dtos);
			addGlobalError(model, "error.load.contracts");
		}
		if (log.isDebugEnabled()) {
			log.debug("Loaded list of contracts size: " + (dtos != null ? dtos.size() : null));
		}
	}

}
