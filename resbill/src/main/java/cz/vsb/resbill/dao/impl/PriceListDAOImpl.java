package cz.vsb.resbill.dao.impl;

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

	private static final Logger log = LoggerFactory.getLogger(PriceListDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public PriceList findPriceList(Integer id) {
		return em.find(PriceList.class, id);
	}

	@Override
	public PriceList findFirstPriceList(Integer tariffId) {
		PriceListCriteria criteria = new PriceListCriteria();
		criteria.setFirst(Boolean.TRUE);
		criteria.setTariffId(tariffId);
		List<PriceList> results = findPriceLists(criteria, null, null);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public PriceList findLastPriceList(Integer tariffId) {
		PriceListCriteria criteria = new PriceListCriteria();
		criteria.setLast(Boolean.TRUE);
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
			if (criteria.getFirst() != null) {
				if (criteria.getFirst()) {
					where.add("pl.previous IS NULL");
				} else {
					where.add("pl.previous IS NOT NULL");
				}
			}
			if (criteria.getLast() != null) {
				if (criteria.getLast()) {
					where.add("pl.period.endDate IS NULL");
				} else {
					where.add("pl.period.endDate IS NOT NULL");
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
					jpql.append(" ORDER BY pl.period.beginDate, pl.period.endDate");
					break;
				default:
					log.warn("Unsupported order by option: " + criteria.getOrderBy());
					break;
				}
			}
		}

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

	// /**
	// * V ramci kontraktu nalezne cenik platny ke dni pro DailyUsage
	// *
	// * @param contractId
	// * @param dailyUsageId
	// * @return
	// */
	//
	// @Override
	// public PriceList findContractDailyUsagePriceList(Integer contractId, DailyUsage dailyUsage) {
	// log.debug("1");
	// // DailyUsage dailyUsage = em.find(DailyUsage.class, dailyUsageId);
	// log.debug("2");
	// Date day = dailyUsage.getDailyImport().getDate();
	//
	// log.debug("3");
	// StringBuilder jpql = new StringBuilder();
	// jpql.append(" SELECT priceList ");
	// jpql.append(" FROM Contract AS contract ");
	// jpql.append(" JOIN contract.contractTariffs AS contractTariff ");
	// jpql.append(" JOIN contractTariff.tariff AS tariff");
	// jpql.append(" JOIN tariff.prices AS priceList ");
	// jpql.append(" WHERE contract.id = :contractId ");
	// jpql.append(" AND contractTariff.period.beginDate <= :day ");
	// jpql.append(" AND (contractTariff.period.endDate IS NULL OR contractTariff.period.endDate >= :day ) ");
	// jpql.append(" AND priceList.period.beginDate <= :day ");
	// jpql.append(" AND (priceList.period.endDate IS NULL OR priceList.period.endDate >= :day ) ");
	//
	// TypedQuery<PriceList> query = em.createQuery(jpql.toString(), PriceList.class);
	// query.setParameter("contractId", contractId);
	// query.setParameter("day", day);
	//
	// PriceList priceList = DataAccessUtils.uniqueResult(query.getResultList());
	//
	// log.debug("4");
	//
	// return priceList;
	// }

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
