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

import cz.vsb.resbill.criteria.PriceListCriteria;
import cz.vsb.resbill.dao.PriceListDAO;
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
	public PriceList findLastValidPriceList(Integer tariffId) {
		PriceListCriteria criteria = new PriceListCriteria();
		criteria.setLastValid(Boolean.TRUE);
		criteria.setTariffId(tariffId);
		List<PriceList> results = findPriceLists(criteria, null, null);
		return DataAccessUtils.singleResult(results);
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
			if (criteria.getLastValid() != null) {
				if (criteria.getLastValid()) {
					where.add("pl.period.endDate IS NULL");
				} else {
					where.add("pl.period.endDate IS NOT NULL");
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
