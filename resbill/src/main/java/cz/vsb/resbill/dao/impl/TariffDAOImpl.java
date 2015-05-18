package cz.vsb.resbill.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.TariffCriteria;
import cz.vsb.resbill.dao.TariffDAO;
import cz.vsb.resbill.model.Tariff;

@Repository
public class TariffDAOImpl implements TariffDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Tariff findTariff(Integer id) {
		return em.find(Tariff.class, id);
	}

	@Override
	public List<Tariff> findTariffs(TariffCriteria criteria) {
		StringBuilder jpql = new StringBuilder("SELECT t FROM Tariff AS t");
		// building query
		if (criteria != null) {
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
				where.add("t.name LIKE :namePrefix");
			}
			if (criteria.getValid() != null) {
				where.add("t.name = :valid");
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}

			// order by
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case NAME:
					jpql.append(" ORDER BY t.name");
					break;
				case VALIDITY:
					jpql.append(" ORDER BY t.valid");
					break;
				}
			}
		}

		TypedQuery<Tariff> query = em.createQuery(jpql.toString(), Tariff.class);

		// parameters
		if (criteria != null) {
			if (StringUtils.isNotEmpty(criteria.getNamePrefix())) {
				query.setParameter("namePrefix", criteria.getNamePrefix() + "%");
			}
			if (criteria.getValid() != null) {
				query.setParameter("valid", criteria.getValid());
			}
		}
		return query.getResultList();
	}

}
