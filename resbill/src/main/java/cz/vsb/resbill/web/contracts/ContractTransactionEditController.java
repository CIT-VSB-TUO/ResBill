/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.web.contracts;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.vsb.resbill.criteria.TransactionTypeCriteria;
import cz.vsb.resbill.dto.TransactionEditDTO;
import cz.vsb.resbill.exception.TransactionServiceException;
import cz.vsb.resbill.model.Invoice;
import cz.vsb.resbill.model.Transaction;
import cz.vsb.resbill.model.TransactionType;
import cz.vsb.resbill.service.ContractService;
import cz.vsb.resbill.service.TransactionService;
import cz.vsb.resbill.service.TransactionTypeService;
import cz.vsb.resbill.util.WebUtils;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Controller
@RequestMapping("/contracts/transactions/edit")
@SessionAttributes({ "transactionEditDTO", "transactionTypes", "contractEditDTO" })
public class ContractTransactionEditController {

  private static final Logger    log                                   = LoggerFactory.getLogger(ContractTransactionEditController.class);

  public static final String     MODEL_OBJECT_KEY_TRANSACTION_EDIT_DTO = "transactionEditDTO";

  public static final String     MODEL_OBJECT_KEY_TRANSACTION_TYPES    = "transactionTypes";

  @Inject
  private TransactionService     transactionService;

  @Inject
  private TransactionTypeService transactionTypeService;

  @Inject
  private ContractService        contractService;

  @InitBinder
  public void initBinder(WebDataBinder binder, Locale locale) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  /**
   * 
   * @param model
   * @param msgKey
   */
  protected static void addGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_TRANSACTION_EDIT_DTO, msgKey);
  }

  protected static void addTransactionTypesGlobalError(ModelMap model, String msgKey) {
    WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_TRANSACTION_TYPES, msgKey);
  }

  /**
   * 
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  public String view(@RequestParam(value = "transactionId", required = false) Integer transactionId, @RequestParam(value = "contractId", required = false) Integer contractId, ModelMap model) {
    loadTransactionEditDTO(transactionId, contractId, model);
    loadTransactionTypes(model);

    return "contracts/contractTransactionEdit";
  }

  /**
   * 
   * @param transactionId
   * @param model
   */
  private void loadTransactionEditDTO(Integer transactionId, Integer contractId, ModelMap model) {

    TransactionEditDTO transactionEditDTO = null;
    try {
      if (transactionId != null) {
        transactionEditDTO = new TransactionEditDTO(transactionService.findTransaction(transactionId));
      } else {
        transactionEditDTO = new TransactionEditDTO(new Transaction());
        transactionEditDTO.setContractId(contractId);
      }
      model.addAttribute(MODEL_OBJECT_KEY_TRANSACTION_EDIT_DTO, transactionEditDTO);
    } catch (Exception e) {
      log.error("Cannot load transaction with id: " + transactionId, e);

      model.addAttribute(MODEL_OBJECT_KEY_TRANSACTION_EDIT_DTO, transactionEditDTO);
      WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_TRANSACTION_EDIT_DTO, "error.load.contract.transaction");
    }
  }

  /**
   * 
   * @param transactionId
   * @param model
   */
  private void loadTransactionTypes(ModelMap model) {

    List<TransactionType> transactionTypes = null;
    try {
      TransactionTypeCriteria criteria = new TransactionTypeCriteria();
      criteria.setOrderBy(Arrays.asList(new TransactionTypeCriteria.OrderBy[] { TransactionTypeCriteria.OrderBy.ID_ASC }));

      transactionTypes = transactionTypeService.findTransactionTypes(criteria, null, null);
      model.addAttribute(MODEL_OBJECT_KEY_TRANSACTION_TYPES, transactionTypes);
    } catch (Exception e) {
      log.error("Cannot load transactionTypes.", e);

      model.addAttribute(MODEL_OBJECT_KEY_TRANSACTION_TYPES, transactionTypes);
      WebUtils.addGlobalError(model, MODEL_OBJECT_KEY_TRANSACTION_TYPES, "error.load.contract.transaction.transactionTypes");
    }
  }

  /**
   * 
   * @param transactionEditDTO
   * @param bindingResult
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.POST, params = "save")
  public String save(@Valid @ModelAttribute(MODEL_OBJECT_KEY_TRANSACTION_EDIT_DTO) TransactionEditDTO transactionEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

    if (!bindingResult.hasErrors()) {
      try {
        Transaction transaction = transactionEditDTO.getTransaction();
        transaction.setTransactionType(transactionTypeService.findTransactionType(transactionEditDTO.getTransactionTypeId()));
        transaction.setContract(contractService.findContract(transactionEditDTO.getContractId()));
        transactionService.saveTransaction(transaction);

        redirectAttributes.addAttribute("contractId", transaction.getContract().getId());
        return "redirect:/contracts/transactions";
      } catch (TransactionServiceException e) {
        switch (e.getReason()) {
        case EDIT_NOT_ALLOWED:
          bindingResult.reject("error.save.contract.transaction.editNotAllowed");
          break;
        case INVOICE_NOT_ALLOWED:
          bindingResult.reject("error.save.contract.transaction.invoiceNotAllowed");
          break;
        case INCOMING_PAYMENT_NO_POSITIVE:
          bindingResult.reject("error.save.contract.transaction.incomingPaymentNoPositive");
          break;
        default:
          log.warn("Unsupported reason: " + e);
          bindingResult.reject("error.save.contract.transaction");
          break;
        }
      } catch (Exception e) {
        log.error("Cannot save TransactionEditDTO: " + transactionEditDTO, e);
        bindingResult.reject("error.save.contract.transaction");
      }
    } else {
      bindingResult.reject("error.save.contract.transaction.validation");
    }

    return "contracts/contractTransactionEdit";
  }

  /**
   * 
   * @param dailyImportId
   * @param model
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.POST, params = "delete")
  public String delete(@ModelAttribute(MODEL_OBJECT_KEY_TRANSACTION_EDIT_DTO) TransactionEditDTO transactionEditDTO, ModelMap model, RedirectAttributes redirectAttributes) {

    try {
      Transaction transaction = transactionService.deleteTransaction(transactionEditDTO.getTransaction().getId());

      redirectAttributes.addAttribute("contractId", transaction.getContract().getId());
      return "redirect:/contracts/transactions";

    } catch (Exception exc) {
      log.error("Cannot delete transaction: " + transactionEditDTO.getTransaction(), exc);
      addGlobalError(model, "error.delete.transaction");
    }

    return "contracts/contractTransactionEdit";
  }
}
