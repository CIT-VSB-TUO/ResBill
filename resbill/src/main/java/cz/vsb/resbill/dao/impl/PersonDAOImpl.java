package cz.vsb.resbill.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cz.vsb.resbill.criteria.PersonCriteria;
import cz.vsb.resbill.dao.PersonDAO;
import cz.vsb.resbill.model.Person;

/**
 * Implementation class of PersonDAO interface.
 * 
 * @author HAL191
 *
 */
@Repository
public class PersonDAOImpl implements PersonDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Person findPerson(Integer personId) {
		return em.find(Person.class, personId);
	}

	@Override
	public List<Person> findPersons(PersonCriteria criteria, Integer offset, Integer limit) {
		StringBuilder jpql = new StringBuilder("SELECT p FROM Person AS p");
		// building query
		if (criteria != null) {
			// where
			Set<String> where = new LinkedHashSet<String>();
			if (StringUtils.isNotEmpty(criteria.getEmail())) {
				where.add("p.email = :email");
			}
			if (StringUtils.isNoneEmpty(criteria.getFirstName())) {
				where.add("p.firstName = :firstName");
			}
			if (StringUtils.isNoneEmpty(criteria.getSecondName())) {
				where.add("p.secondName = :secondName");
			}
			if (!where.isEmpty()) {
				jpql.append(" WHERE ");
				jpql.append(StringUtils.join(where, " AND "));
			}

			// order by
			if (criteria.getOrderBy() != null) {
				switch (criteria.getOrderBy()) {
				case EMAIL:
					jpql.append(" ORDER BY p.email");
					break;
				case NAME:
					jpql.append(" ORDER BY p.secondName, p.firstName, p.email");
					break;
				}
			}
		}
		TypedQuery<Person> query = em.createQuery(jpql.toString(), Person.class);

		// parameters
		if (criteria != null) {
			if (StringUtils.isNotEmpty(criteria.getEmail())) {
				query.setParameter("email", criteria.getEmail());
			}
			if (StringUtils.isNoneEmpty(criteria.getFirstName())) {
				query.setParameter("firstName", criteria.getFirstName());
			}
			if (StringUtils.isNoneEmpty(criteria.getSecondName())) {
				query.setParameter("secondName", criteria.getSecondName());
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
	public Person savePerson(Person person) {
		if (person.getId() == null) {
			em.persist(person);
		} else {
			person = em.merge(person);
		}
		em.flush();

		return person;
	}

	@Override
	public Person deletePerson(Person person) {
		em.remove(person);
		em.flush();
		return person;
	}
}
