package cz.vsb.resbill.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.ContractServerCriteria;
import cz.vsb.resbill.dao.ContractServerDAO;
import cz.vsb.resbill.dao.ContractTariffDAO;
import cz.vsb.resbill.model.ContractServer;

/**
 * Implementation class of {@link ContractTariffDAO} interface.
 * 
 * @author HAL191
 *
 */
@Repository
public class ContractServerDAOImpl implements ContractServerDAO {

	private static final Logger log = LoggerFactory.getLogger(ContractServerDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public ContractServer findContractServer(Integer contractServerId, boolean fetchContract, boolean fetchServer) {
		ContractServer cs = em.find(ContractServer.class, contractServerId);
		if (cs != null) {
			if (fetchContract) {
				Hibernate.initialize(cs.getContract());
			}
			if (fetchServer) {
				Hibernate.initialize(cs.getServer());
			}
		}
		return cs;
	}

	@Override
	public ContractServer findCurrentContractServer(Integer serverId) {
		ContractServerCriteria criteria = new ContractServerCriteria();
		criteria.setServerId(serverId);
		criteria.setCurrentlyAssociated(Boolean.TRUE);
		criteria.setFetchContract(true);
		List<ContractServer> results = findContractServers(criteria, null, null);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public List<ContractServer> findContractServers(ContractServerCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT cs FROM ContractServer AS cs");
		// building query
		if (criteria != null) {
			// join
			if (criteria.isFetchContract()) {
				jpql.append(" LEFT JOIN FETCH cs.contract AS contract");
			}
			if (criteria.isFetchServer()) {
				jpql.append(" LEFT JOIN FETCH cs.server AS server");
			}
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (criteria.getContractId() != null) {
				where.add("cs.contract.id = :contractId");
			}
			if (criteria.getServerId() != null) {
				where.add("cs.server.id = :serverId");
			}
			if (criteria.getAssociatedFrom() != null) {
				where.add("(cs.period.endDate IS NULL OR cs.period.endDate >= :associatedFrom)");
			}
			if (criteria.getAssociatedTo() != null) {
				where.add("cs.period.beginDate <= :associatedTo");
			}
			if (criteria.getCurrentlyAssociated() != null) {
				String condition = "(cs.period.beginDate <= CURRENT_DATE AND (cs.period.endDate IS NULL OR cs.period.endDate >= CURRENT_DATE))";
				if (criteria.getCurrentlyAssociated()) {
					where.add(condition);
				} else {
					where.add("NOT " + condition);
				}
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}
			// order by
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case PERIOD:
					jpql.append(" ORDER BY cs.period.beginDate, cs.period.endDate");
					break;
				default:
					log.warn("Unsupported order by option: " + criteria.getOrderBy());
					break;
				}
			}
		}

		TypedQuery<ContractServer> query = em.createQuery(jpql.toString(), ContractServer.class);

		// parameters
		if (criteria != null) {
			if (criteria.getContractId() != null) {
				query.setParameter("contractId", criteria.getContractId());
			}
			if (criteria.getServerId() != null) {
				query.setParameter("serverId", criteria.getServerId());
			}
			if (criteria.getAssociatedFrom() != null) {
				query.setParameter("associatedFrom", criteria.getAssociatedFrom());
			}
			if (criteria.getAssociatedTo() != null) {
				query.setParameter("associatedTo", criteria.getAssociatedTo());
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
	public ContractServer saveContractServer(ContractServer contractServer) {
		if (contractServer.getId() == null) {
			em.persist(contractServer);
		} else {
			contractServer = em.merge(contractServer);
		}
		em.flush();

		return contractServer;
	}

	@Override
	public ContractServer deleteContractServer(ContractServer contractServer) {
		em.remove(contractServer);
		em.flush();

		return contractServer;
	}
}
