package cz.vsb.resbill.dao.impl;

import java.util.ArrayList;
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

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.model.Server;

@Repository
public class ServerDAOImpl implements ServerDAO {

  private static final Logger log = LoggerFactory.getLogger(ServerDAOImpl.class);

  @PersistenceContext
  private EntityManager       em;

  /**
	 * 
	 */
  @Override
  public Server findServer(Integer id) {
    return em.find(Server.class, id);
  }

  /**
	 * 
	 */
  @Override
  public Server findServer(String serverId) {
    StringBuilder jpql = new StringBuilder();
    jpql.append(" SELECT server ");
    jpql.append(" FROM Server AS server ");
    jpql.append(" WHERE server.serverId = :serverId ");

    TypedQuery<Server> query = em.createQuery(jpql.toString(), Server.class);

    query.setParameter("serverId", serverId);

    return DataAccessUtils.uniqueResult(query.getResultList());
  }

  /**
	 * 
	 */
  @Override
  public List<Server> findServers(ServerCriteria criteria, Integer offset, Integer limit) {
    StringBuilder jpql = new StringBuilder("SELECT server FROM Server AS server");
    // building query
    if (criteria != null) {
      // where
      Set<String> where = new LinkedHashSet<String>();
      if (StringUtils.isNotEmpty(criteria.getServerId())) {
        where.add("server.serverId = :serverId");
      }
      if (criteria.getServerIds() != null) {
        where.add("server.id IN (:serverIds)");
      }
      if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
        where.add("server.name LIKE :namePrefix");
      }

      // "Vlastnosti" kontraktu
      if (criteria.getFeatures() != null) {
        // Servery odebírající zdroje mimo období prirazeni ke kontraktu
        if (criteria.getFeatures().contains(ServerCriteria.Feature.DAILY_USAGE_OUT_OF_CONTRACT)) {
          StringBuilder subJpql = new StringBuilder();
          subJpql.append(" EXISTS ( ");
          subJpql.append("   SELECT dailyUsage ");
          subJpql.append("   FROM Server AS duServer ");
          subJpql.append("   JOIN duServer.dailyUsages AS dailyUsage ");
          subJpql.append("   JOIN dailyUsage.dailyImport AS dailyImport ");
          subJpql.append("   WHERE duServer = server ");
          subJpql.append("   AND NOT EXISTS ( ");
          subJpql.append("     SELECT contractServer ");
          subJpql.append("     FROM ContractServer AS contractServer ");
          subJpql.append("     WHERE contractServer.server = duServer ");
          subJpql.append("     AND contractServer.period.beginDate <= dailyImport.date ");
          subJpql.append("     AND (contractServer.period.endDate IS NULL OR contractServer.period.endDate >= dailyImport.date) ");
          subJpql.append("   ) ");
          subJpql.append(" ) ");

          where.add(subJpql.toString());
        }

        // Servery bez kontraktu
        if (criteria.getFeatures().contains(ServerCriteria.Feature.NO_CONTRACT)) {
          where.add("server.contractServers IS EMPTY");
        }

        // Servery bez denniho vyuziti zdroju
        if (criteria.getFeatures().contains(ServerCriteria.Feature.NO_DAILY_USAGE)) {
          where.add("server.dailyUsages IS EMPTY");
        }
      }

      if (!where.isEmpty()) {
        jpql.append(" WHERE ");
        jpql.append(StringUtils.join(where, " AND "));
      }

      // order by
      if (criteria.getOrderBy() != null && !criteria.getOrderBy().isEmpty()) {
        List<String> order = new ArrayList<String>();
        for (ServerCriteria.OrderBy orderBy : criteria.getOrderBy()) {
          switch (orderBy) {
          case SERVER_ID_ASC:
            order.add(" server.serverId ASC ");
            break;
          case SERVER_ID_DESC:
            order.add(" server.serverId DESC ");
            break;
          case NAME_ASC:
            order.add(" server.name ASC ");
            break;
          case NAME_DESC:
            order.add(" server.name DESC ");
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
    }
    TypedQuery<Server> query = em.createQuery(jpql.toString(), Server.class);
    if (offset != null) {
      query.setFirstResult(offset.intValue());
    }
    if (limit != null) {
      query.setMaxResults(limit.intValue());
    }

    // parameters
    if (criteria != null) {
      if (StringUtils.isNotEmpty(criteria.getServerId())) {
        query.setParameter("serverId", criteria.getServerId());
      }
      if (criteria.getServerIds() != null) {
        query.setParameter("serverIds", criteria.getServerIds());
      }
      if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
        query.setParameter("namePrefix", criteria.getNamePrefix() + "%");
      }
    }

    return query.getResultList();
  }

  /**
   * 
   */
  @Override
  public Server saveServer(Server server) {
    if (server.getId() == null) {
      em.persist(server);
    } else {
      server = em.merge(server);
    }
    em.flush();

    return server;
  }

  /**
   * 
   */
  @Override
  public Server deleteServer(Server server) {
    em.remove(server);
    em.flush();

    return server;
  }
}
