package cz.vsb.resbill.dao.impl;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.dao.PriceListDAO;
import cz.vsb.resbill.model.DailyUsage;
import cz.vsb.resbill.model.PriceList;

/**
 * Implementation class of {@link PriceListDAO} interface.
 * 
 * @author HAL191
 *
 */
@Repository
public class PriceListDAOImpl implements PriceListDAO {

  @PersistenceContext
  private EntityManager em;

  @Override
  public PriceList findPriceList(Integer id) {
    return em.find(PriceList.class, id);
  }

  @Override
  public List<PriceList> findPriceLists(PriceListCriteria criteria, Integer offset, Integer limit) {
    StringBuilder jpql = new StringBuilder("SELECT pl FROM PriceList AS pl");
    if (criteria != null) {
      // building query
      Set<String> where = new LinkedHashSet<String>();
      if (criteria.getTariffId() != null) {
        where.add("pl.tariff.id = :tariffId");
      }
      if (criteria.getCurrentlyValid() != null) {
        String condition = "(pl.period.beginDate <= CURRENT_DATE AND (pl.period.endDate IS NULL OR pl.period.endDate >= CURRENT_DATE))";
        if (criteria.getCurrentlyValid()) {
          where.add(condition);
        } else {
          where.add("NOT " + condition);
        }
      }
      if (!where.isEmpty()) {
        jpql.append(" WHERE ");
        jpql.append(StringUtils.join(where, " AND "));
      }
    }
    // order by
    jpql.append(" ORDER BY pl.period.beginDate, pl.period.endDate");

    TypedQuery<PriceList> query = em.createQuery(jpql.toString(), PriceList.class);

    // parameters
    if (criteria != null) {
      if (criteria.getTariffId() != null) {
        query.setParameter("tariffId", criteria.getTariffId());
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

  /**
   * V ramci kontraktu nalezne cenik platny ke dni pro DailyUsage
   * 
   * @param contractId
   * @param dailyUsageId
   * @return
   */

  @Override
  public PriceList findContractDailyUsagePriceList(Integer contractId, Integer dailyUsageId) {
    DailyUsage dailyUsage = em.find(DailyUsage.class, dailyUsageId);
    Date day = dailyUsage.getDailyImport().getDate();

    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT priceList ");
    jpql.append(" FROM Contract AS contract ");
    jpql.append(" JOIN contract.contractTariffs AS contractTariff ");
    jpql.append(" JOIN contractTariff.tariff AS tariff");
    jpql.append(" JOIN tariff.prices AS priceList ");
    jpql.append(" WHERE contract.id = :contractId ");
    jpql.append(" AND contractTariff.period.beginDate <= :day ");
    jpql.append(" AND (contractTariff.period.endDate IS NULL OR contractTariff.period.endDate >= :day ) ");
    jpql.append(" AND priceList.period.beginDate <= :day ");
    jpql.append(" AND (priceList.period.endDate IS NULL OR priceList.period.endDate >= :day ) ");

    TypedQuery<PriceList> query = em.createQuery(jpql.toString(), PriceList.class);
    query.setParameter("contractId", contractId);
    query.setParameter("day", day);

    return DataAccessUtils.uniqueResult(query.getResultList());
  }

  @Override
  public PriceList savePriceList(PriceList priceList) {
    if (priceList.getId() == null) {
      em.persist(priceList);
    } else {
      priceList = em.merge(priceList);
    }
    em.flush();

    return priceList;
  }

  @Override
  public PriceList deletePriceList(PriceList priceList) {
    em.remove(priceList);
    em.flush();

    return priceList;
  }
}
