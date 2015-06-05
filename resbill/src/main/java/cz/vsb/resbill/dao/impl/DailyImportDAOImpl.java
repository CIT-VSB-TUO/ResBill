/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.DailyImportCriteria;
import cz.vsb.resbill.dao.DailyImportDAO;
import cz.vsb.resbill.model.DailyImport;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */

@Repository
public class DailyImportDAOImpl implements DailyImportDAO {

  private static final Logger log = LoggerFactory.getLogger(DailyImportDAOImpl.class);

  @PersistenceContext
  private EntityManager       em;

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
    // where
    Set<String> where = new LinkedHashSet<String>();

    if (criteria.getDailyImportIds() != null) {
      where.add("dailyImport.id IN (:dailyImportIds)");
    }
    if (criteria.getBeginDate() != null) {
      where.add("dailyImport.date >= :beginDate ");
    }
    if (criteria.getEndDate() != null) {
      where.add("dailyImport.date <= :endDate ");
    }
    if (criteria.getBeginImportEndDate() != null) {
      where.add("DATE(dailyImport.importEndTimestamp) >= :beginImportEndDate ");
    }
    if (criteria.getEndImportEndDate() != null) {
      where.add("DATE(dailyImport.importEndTimestamp) <= :endImportEndDate ");
    }
    // "Vlastnosti" faktury
    if (criteria.getFeatures() != null) {
      // Import ma alespon jeden radek s chybou
      if (criteria.getFeatures().contains(DailyImportCriteria.Feature.HAS_ERRORS)) {
        where.add("dailyImport.errorLines > 0 ");
      }
      // Import ma alespon jeden radek s varovanim
      if (criteria.getFeatures().contains(DailyImportCriteria.Feature.HAS_WARNS)) {
        where.add("dailyImport.warnLines > 0 ");
      }
    }

    if (!where.isEmpty()) {
      jpql.append(" WHERE ");
      jpql.append(StringUtils.join(where, " AND "));
    }

    // order by
    if (criteria.getOrderBy() != null && !criteria.getOrderBy().isEmpty()) {
      List<String> order = new ArrayList<String>();
      for (DailyImportCriteria.OrderBy orderBy : criteria.getOrderBy()) {
        switch (orderBy) {
        case DATE_ASC:
          order.add(" dailyImport.date ASC ");
          break;
        case DATE_DESC:
          order.add(" dailyImport.date DESC ");
          break;
        default:
          log.warn("Unsupported order by option: " + orderBy);
          break;
        }
      }

      if (!order.isEmpty()) {
        jpql.append(" ORDER BY ");
        jpql.append(StringUtils.join(order, ","));
      }
    }

    TypedQuery<DailyImport> query = em.createQuery(jpql.toString(), DailyImport.class);
    if (offset != null) {
      query.setFirstResult(offset);
    }
    if (limit != null) {
      query.setMaxResults(limit);
    }

    if (criteria.getDailyImportIds() != null) {
      query.setParameter("dailyImportIds", criteria.getDailyImportIds());
    }
    if (criteria.getBeginDate() != null) {
      query.setParameter("beginDate", criteria.getBeginDate());
    }
    if (criteria.getEndDate() != null) {
      query.setParameter("endDate", criteria.getEndDate());
    }
    if (criteria.getBeginImportEndDate() != null) {
      query.setParameter("beginImportEndDate", criteria.getBeginImportEndDate());
    }
    if (criteria.getEndImportEndDate() != null) {
      query.setParameter("endImportEndDate", criteria.getEndImportEndDate());
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
