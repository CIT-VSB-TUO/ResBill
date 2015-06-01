package cz.vsb.resbill.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.InvoicePriceListCriteria;
import cz.vsb.resbill.dao.InvoicePriceListDAO;
import cz.vsb.resbill.model.InvoicePriceList;

/**
 * Implementation class of {@link InvoicePriceListDAO} interface.
 * 
 * @author HAL191
 *
 */
@Repository
public class InvoicePriceListDAOImpl implements InvoicePriceListDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public InvoicePriceList findInvoicePriceList(Integer invoicePriceListId) {
		return em.find(InvoicePriceList.class, invoicePriceListId);
	}

	@Override
	public List<InvoicePriceList> findInvoicePriceLists(InvoicePriceListCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT ipl FROM InvoicePriceList AS ipl");
		// building query
		if (criteria != null) {
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (criteria.getInvoiceId() != null) {
				where.add("ipl.invoice.id = :invoiceId");
			}
			if (criteria.getPriceListId() != null) {
				where.add("ipl.priceList.id = :priceListId");
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}
		}

		TypedQuery<InvoicePriceList> query = em.createQuery(jpql.toString(), InvoicePriceList.class);

		// parameters
		if (criteria != null) {
			if (criteria.getInvoiceId() != null) {
				query.setParameter("invoiceId", criteria.getInvoiceId());
			}
			if (criteria.getPriceListId() != null) {
				query.setParameter("priceListId", criteria.getPriceListId());
			}
		}
		if (offset != null) {
			query.setFirstResult(offset.intValue());
		}
		if (limit != null) {
			query.setMaxResults(limit.intValue());
		}
		return query.getResultList();
	}
}
