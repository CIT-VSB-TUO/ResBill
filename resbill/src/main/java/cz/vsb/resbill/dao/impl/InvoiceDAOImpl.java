/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.InvoiceCriteria;
import cz.vsb.resbill.dao.InvoiceDAO;
import cz.vsb.resbill.model.Invoice;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class InvoiceDAOImpl implements InvoiceDAO {

	@PersistenceContext
	private EntityManager em;

	/**
	 * 
	 */
	@Override
	public Invoice findInvoice(Integer id) {
		return em.find(Invoice.class, id);
	}

	/**
	 * 
	 */
	@Override
	public List<Invoice> findInvoices(InvoiceCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT invoice ");
		jpql.append(" FROM Invoice AS invoice ");
		if (criteria.getOrderBy() != null) {
			jpql.append(" ORDER BY ");
			if (criteria.getOrderBy() == InvoiceCriteria.OrderBy.DECISIVE_DATE) {
				jpql.append(" invoice.decisiveDate ");
			}
			if (criteria.isOrderDesc()) {
				jpql.append(" DESC ");
			}
		}

		TypedQuery<Invoice> query = em.createQuery(jpql.toString(), Invoice.class);
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (limit != null) {
			query.setMaxResults(limit);
		}

		return query.getResultList();
	}

	/**
   * 
   */
	@Override
	public Invoice saveInvoice(Invoice invoice) {
		if (invoice.getId() == null) {
			em.persist(invoice);
		} else {
			invoice = em.merge(invoice);
		}

		em.flush();

		return invoice;
	}

	/**
   * 
   */
	@Override
	public Invoice deleteInvoice(Invoice invoice) {
		em.remove(invoice);
		
		em.flush();
		
		return invoice;
	}

}
