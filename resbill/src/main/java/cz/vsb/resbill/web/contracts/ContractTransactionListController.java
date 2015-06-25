/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.contracts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.vsb.resbill.criteria.TransactionCriteria;
import cz.vsb.resbill.dto.TransactionDTO;
import cz.vsb.resbill.service.TransactionService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/contracts/transactions")
public class ContractTransactionListController extends AbstractContractController {

	private static final Logger log = LoggerFactory.getLogger(ContractTransactionListController.class);

	public static final String MODEL_OBJECT_KEY_TRANSACTIONS = "transactions";

	@Inject
	private TransactionService transactionService;

	/**
	 * 
	 * @param model
	 * @param msgKey
	 */
	protected static void addGlobalError(ModelMap model, String msgKey) {
		WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_TRANSACTIONS, msgKey);
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String view(@RequestParam(value = "contractId", required = true) Integer contractId, ModelMap model) {
		loadTransactionDTOs(contractId, model);

		return "contracts/contractTransactionList";
	}

	/**
	 * 
	 * @return
	 */
	protected List<TransactionDTO> loadTransactionDTOs(Integer contractId, ModelMap model) {

		List<TransactionDTO> transactionDTOs = null;

		try {
			List<TransactionCriteria.OrderBy> orderBy = new ArrayList<TransactionCriteria.OrderBy>();
			orderBy.add(TransactionCriteria.OrderBy.ORDER_DESC);

			TransactionCriteria criteria = new TransactionCriteria();
			criteria.setOrderBy(orderBy);
			criteria.setContractId(contractId);

			transactionDTOs = transactionService.findTransactionDTOs(criteria, null, null);

			model.addAttribute(MODEL_OBJECT_KEY_TRANSACTIONS, transactionDTOs);
		} catch (Exception exc) {
			log.error("Cannot load contract transactions.", exc);

			transactionDTOs = null;

			model.addAttribute(MODEL_OBJECT_KEY_TRANSACTIONS, transactionDTOs);
			addGlobalError(model, "error.load.contract.transactions");
		}

		return transactionDTOs;
	}
}
