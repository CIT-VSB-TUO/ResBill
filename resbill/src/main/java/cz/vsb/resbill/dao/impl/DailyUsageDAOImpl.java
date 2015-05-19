/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cz.vsb.resbill.dao.DailyUsageDAO;
import cz.vsb.resbill.model.DailyUsage;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Repository
public class DailyUsageDAOImpl implements DailyUsageDAO {

	@PersistenceContext
	private EntityManager em;

	/**
	 * 
	 */
	@Override
	public DailyUsage saveDailyUsage(DailyUsage dailyUsage) {
		if (dailyUsage.getId() == null) {
			em.persist(dailyUsage);
		} else {
			dailyUsage = em.merge(dailyUsage);
		}

		em.flush();

		return dailyUsage;
	}

}
