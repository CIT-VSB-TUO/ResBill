package cz.vsb.resbill.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.ServerCriteria;
import cz.vsb.resbill.dao.ServerDAO;
import cz.vsb.resbill.model.Server;

@Repository
public class ServerDAOImpl implements ServerDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Server findServer(Integer id) {
		return em.find(Server.class, id);
	}

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

	@Override
	public List<Server> findServers(ServerCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT s FROM Server AS s");
		// building query
		if (criteria != null) {
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (StringUtils.isNotEmpty(criteria.getServerId())) {
				where.add("s.serverId = :serverId");
			}
			if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
				where.add("s.name LIKE :namePrefix");
			}
			if (criteria.getUsed() != null) {
				if (criteria.getUsed()) {
					where.add("s.dailyUsages IS NOT EMPTY");
				} else {
					where.add("s.dailyUsages IS EMPTY");
				}
			}
			if (criteria.getInContract() != null) {
				if (criteria.getInContract()) {
					where.add("s.contractServers IS NOT EMPTY");
				} else {
					where.add("s.contractServers IS EMPTY");
				}
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}

			// order by
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case SERVER_ID:
					jpql.append(" ORDER BY s.serverId");
					break;
				case NAME:
					jpql.append(" ORDER BY s.name, s.serverId");
					break;
				}
			}
		}
		TypedQuery<Server> query = em.createQuery(jpql.toString(), Server.class);

		// parameters
		if (criteria != null) {
			if (StringUtils.isNotEmpty(criteria.getServerId())) {
				query.setParameter("serverId", criteria.getServerId());
			}
			if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
				query.setParameter("namePrefix", criteria.getNamePrefix() + "%");
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

	@Override
	public Server deleteServer(Server server) {
		em.remove(server);
		em.flush();

		return server;
	}
}
