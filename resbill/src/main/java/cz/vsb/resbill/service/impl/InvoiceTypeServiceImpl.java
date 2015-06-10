package cz.vsb.resbill.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vsb.resbill.criteria.InvoiceTypeCriteria;
import cz.vsb.resbill.dao.InvoiceTypeDAO;
import cz.vsb.resbill.exception.ResBillException;
import cz.vsb.resbill.model.InvoiceType;
import cz.vsb.resbill.service.InvoiceTypeService;
import cz.vsb.resbill.service.ResBillService;

/**
 * An implementation of {@link InvoiceTypeService} interface.
 * 
 * @author HAL191
 *
 */
@ResBillService
public class InvoiceTypeServiceImpl implements InvoiceTypeService {

	private static final Logger log = LoggerFactory.getLogger(InvoiceTypeServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Inject
	private InvoiceTypeDAO invoiceTypeDAO;

	@Override
	public InvoiceType findInvoiceType(Integer invoiceTypeId) throws ResBillException {
		try {
			return invoiceTypeDAO.findInvoiceType(invoiceTypeId);
		} catch (Exception e) {
			log.error("An unexpected error occured while finding InvoiceType by id=" + invoiceTypeId, e);
			throw new ResBillException(e);
		}
	}

	@Override
	public List<InvoiceType> findInvoiceTypes(InvoiceTypeCriteria criteria, Integer offset, Integer limit) throws ResBillException {
		try {
			return invoiceTypeDAO.findInvoiceTypes(criteria, offset, limit);
		} catch (Exception e) {
			log.error("An unexpected error occured while searching for InvoiceType entities by criteria: " + criteria, e);
			throw new ResBillException(e);
		}
	}

}
