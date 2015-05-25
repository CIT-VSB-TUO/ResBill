/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.dao.DailyImportDAO;
import cz.vsb.resbill.model.DailyImport;
import cz.vsb.resbill.model.Person;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class DailyImportDAOImpl implements DailyImportDAO {

	@PersistenceContext
	private EntityManager em;

	/**
	 * 
	 */
	@Override
	public DailyImport findDailyImport(Integer id) {
		return em.find(DailyImport.class, id);
	}

	/**
	 * 
	 */
	@Override
	public DailyImport findDailyImport(Date date) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT dailyImport ");
		jpql.append(" FROM DailyImport AS dailyImport ");
		jpql.append(" WHERE dailyImport.date = :date ");

		TypedQuery<DailyImport> query = em.createQuery(jpql.toString(), DailyImport.class);

		query.setParameter("date", date);

		return DataAccessUtils.uniqueResult(query.getResultList());
	}

	/**
	 * 
	 */
	@Override
	public List<DailyImport> findDailyImports(DailyImportCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT dailyImport ");
		jpql.append(" FROM DailyImport AS dailyImport ");
		if (criteria.getOrderBy() != null) {
			jpql.append(" ORDER BY ");
			if (criteria.getOrderBy() == DailyImportCriteria.OrderBy.DATE) {
				jpql.append(" dailyImport.date ");
			}
			if (criteria.isOrderDesc()) {
				jpql.append(" DESC ");
			}
		}

		TypedQuery<DailyImport> query = em.createQuery(jpql.toString(), DailyImport.class);
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
	public DailyImport saveDailyImport(DailyImport dailyImport) {
		if (dailyImport.getId() == null) {
			em.persist(dailyImport);
		} else {
			dailyImport = em.merge(dailyImport);
		}

		em.flush();

		return dailyImport;
	}

	/**
	 * 
	 */
	@Override
	public DailyImport deleteDailyImport(DailyImport dailyImport) {
		em.remove(dailyImport);
		em.flush();
		return dailyImport;
	}

}
