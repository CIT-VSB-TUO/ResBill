/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.dao.ProductionLevelDAO;
import cz.vsb.resbill.model.ProductionLevel;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
@Repository
public class ProductionLevelDAOImpl implements ProductionLevelDAO {

	@PersistenceContext
	private EntityManager em;

	/**
	 * 
	 */
	@Override
	public ProductionLevel findProductionLevel(Integer productionLevelId) {
		return em.find(ProductionLevel.class, productionLevelId);
	}

	/**
	 * 
	 */
	@Override
	public ProductionLevel findProductionLevel(String productionLevelCode) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT prodLev ");
		jpql.append(" FROM ProductionLevel AS prodLev ");
		jpql.append(" WHERE prodLev.code = :code ");

		TypedQuery<ProductionLevel> query = em.createQuery(jpql.toString(), ProductionLevel.class);

		query.setParameter("code", productionLevelCode);

		return DataAccessUtils.uniqueResult(query.getResultList());
	}

}
