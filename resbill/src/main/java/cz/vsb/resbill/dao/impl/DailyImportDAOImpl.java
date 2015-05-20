/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.dao.DailyImportDAO;
import cz.vsb.resbill.model.DailyImport;

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
	public DailyImport saveDailyImport(DailyImport dailyImport) {
		if (dailyImport.getId() == null) {
			em.persist(dailyImport);
		} else {
			dailyImport = em.merge(dailyImport);
		}

		em.flush();

		return dailyImport;
	}

}
