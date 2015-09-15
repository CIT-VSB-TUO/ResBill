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

  /**
   * Pro zadany kontrakt najde pres vsechny servery kontraktu vsechny DailyUsage, ktere doposud nebyly fakturovany a jsou nejpozdeji v pozadovanem dni. Navic pripoji cenik prislusejici DailyUsage (dle
   * data importu).
   * 
   * Server musi byt kontraktu prirazen take nejpozdeji v pozadovanem dni.
   * 
   * @param lastDay
   * @param contractId
   * @return
   */
  @Override
  public List<Object[]> findUninvoicedDailyUsages(Date lastDay, Integer contractId) {
    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT dailyUsage ");
    jpql.append(" , (SELECT priceList ");
    jpql.append(" FROM Contract AS contract ");
    jpql.append(" JOIN contract.contractTariffs AS contractTariff ");
    jpql.append(" JOIN contractTariff.tariff AS tariff");
    jpql.append(" JOIN tariff.prices AS priceList ");
    jpql.append(" WHERE contract.id = :contractId ");
    jpql.append(" AND contractTariff.period.beginDate <= dailyImport.date ");
    jpql.append(" AND (contractTariff.period.endDate IS NULL OR contractTariff.period.endDate >= dailyImport.date ) ");
    jpql.append(" AND priceList.period.beginDate <= dailyImport.date ");
    jpql.append(" AND (priceList.period.endDate IS NULL OR priceList.period.endDate >= dailyImport.date ) ");
    jpql.append(" ) ");
    jpql.append(" FROM Contract AS contract ");
    jpql.append(" JOIN contract.contractServers AS contractServer ");
    jpql.append(" JOIN contractServer.server AS server ");
    jpql.append(" JOIN server.dailyUsages AS dailyUsage ");
    jpql.append(" JOIN dailyUsage.dailyImport AS dailyImport ");
    jpql.append(" WHERE contract.id = :contractId ");
    jpql.append(" AND contractServer.period.beginDate <= :lastDay ");
    jpql.append(" AND contractServer.period.beginDate <= dailyImport.date ");
    jpql.append(" AND (contractServer.period.endDate IS NULL OR contractServer.period.endDate >= dailyImport.date) ");
    jpql.append(" AND dailyImport.date <= :lastDay ");
    jpql.append(" AND dailyUsage NOT IN ( ");
    jpql.append("   SELECT invoiceDailyUsage.dailyUsage ");
    jpql.append("   FROM InvoiceDailyUsage AS invoiceDailyUsage ");
    jpql.append(" ) ");
    jpql.append(" ORDER BY server.serverId, dailyImport.date ASC ");

    TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class);
    query.setParameter("contractId", contractId);
    query.setParameter("lastDay", lastDay);

    return query.getResultList();
  }

  // /**
  // * Pro zadany kontrakt najde pres vsechny servery kontraktu vsechny DailyUsage, ktere doposud nebyly fakturovany a jsou nejpozdeji v pozadovanem dni.
  // *
  // * Server musi byt kontraktu prirazen take nejpozdeji v pozadovanem dni.
  // *
  // * @param lastDay
  // * @param contractId
  // * @return
  // */
  // @Override
  // public List<DailyUsage> findUninvoicedDailyUsages(Date lastDay, Integer contractId) {
  // StringBuilder jpql = new StringBuilder();
  // jpql.append(" SELECT dailyUsage ");
  // jpql.append(" FROM Contract AS contract ");
  // jpql.append(" JOIN contract.contractServers AS contractServer ");
  // jpql.append(" JOIN contractServer.server AS server ");
  // jpql.append(" JOIN server.dailyUsages AS dailyUsage ");
  // jpql.append(" JOIN FETCH dailyUsage.dailyImport AS dailyImport ");
  // jpql.append(" WHERE contract.id = :contractId ");
  // jpql.append(" AND contractServer.period.beginDate <= :lastDay ");
  // jpql.append(" AND contractServer.period.beginDate <= dailyImport.date ");
  // jpql.append(" AND (contractServer.period.endDate IS NULL OR contractServer.period.endDate >= dailyImport.date) ");
  // jpql.append(" AND dailyImport.date <= :lastDay ");
  // jpql.append(" AND dailyUsage NOT IN ( ");
  // jpql.append("   SELECT invoiceDailyUsage.dailyUsage ");
  // jpql.append("   FROM InvoiceDailyUsage AS invoiceDailyUsage ");
  // jpql.append(" ) ");
  // jpql.append(" ORDER BY server.serverId, dailyImport.date ASC ");
  //
  // TypedQuery<DailyUsage> query = em.createQuery(jpql.toString(), DailyUsage.class);
  // query.setParameter("contractId", contractId);
  // query.setParameter("lastDay", lastDay);
  //
  // return query.getResultList();
  // }

  /**
   * Najde posledni zname vyuziti zdroju serveru
   * 
   * @return
   */
  @Override
  public DailyUsage findServerLastDailyUsage(Integer serverId) {
    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT dailyUsage ");
    jpql.append(" FROM DailyUsage AS dailyUsage ");
    jpql.append(" JOIN dailyUsage.dailyImport AS dailyImport ");
    jpql.append(" WHERE dailyUsage.server.id = :serverId ");
    jpql.append(" ORDER BY dailyImport.date DESC ");

    TypedQuery<DailyUsage> query = em.createQuery(jpql.toString(), DailyUsage.class);
    query.setMaxResults(1);
    query.setParameter("serverId", serverId);

    return DataAccessUtils.uniqueResult(query.getResultList());
  }
}
