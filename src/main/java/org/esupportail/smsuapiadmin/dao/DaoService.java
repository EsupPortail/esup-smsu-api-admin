/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.apache.log4j.Logger;
import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapi.dao.beans.Application;
import org.esupportail.smsuapi.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapi.dao.beans.Sms;
import org.esupportail.smsuapi.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.hibernate.classic.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.InitializingBean;

/**
 * The Hibernate implementation of the DAO service.
 */
public class DaoService extends
HibernateDaoSupport implements
InitializingBean {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3152554337896617315L;

	/**
	 * The name of the 'id' attribute.
	 */
	@SuppressWarnings("unused")
	private static final String ID_ATTRIBUTE = "id";

	// ////////////////////////////////////////////////////////////
	// User
	// ////////////////////////////////////////////////////////////



	public UserBoSmsu getUserById(final Integer id) {
		return getWithRestriction(UserBoSmsu.class, Restrictions.eq(UserBoSmsu.PROP_ID, id)); 
	}


	public UserBoSmsu getUserByLogin(final String login) {
		return getWithRestriction(UserBoSmsu.class, Restrictions.eq(UserBoSmsu.PROP_LOGIN, login)); 
	}

	/**
	 * @see org.esupportail.smsuapiadmin.dao.DaoService#getUsers()
	 */
	public List<UserBoSmsu> getUsers() {
		return getHibernateTemplate().loadAll(UserBoSmsu.class);
	}

	/**
	 * 
	 */
	public void addUser(final UserBoSmsu user) {
		addObject(user);
	}

	public void deleteUser(final UserBoSmsu user) {
		deleteObject(user);
	}

	public void updateUser(final UserBoSmsu user) {
		updateObject(user);
	}

	// ////////////////////////////////////////////////////////////
	// Application
	// ////////////////////////////////////////////////////////////


	public void addApplication(final Application app) {
		addObject(app);
	}


	public void deleteApplication(final Application app) {
		deleteObject(app);
	}


	public List<Application> getApplications() {
		return getHibernateTemplate().loadAll(Application.class);
	}


	public void updateApplication(final Application app) {
		updateObject(app);
	}


	public Application getApplicationById(final int id) {
		return getWithRestriction(Application.class, Restrictions.eq(Application.PROP_ID, id)); 
	}

	public Application getApplicationByName(String name) {
		return getWithRestriction(Application.class, Restrictions.eq(Application.PROP_NAME, name)); 
	}

	// ////////////////////////////////////////////////////////////
	// Account
	// ////////////////////////////////////////////////////////////


	public Account getAccountByName(final String name) {
		return getWithRestriction(Account.class, Restrictions.eq(Account.PROP_LABEL, name)); 
	}

	public void addAccount(final Account account) {
		addObject(account);
	}

	// ////////////////////////////////////////////////////////////
	// Institution
	// ////////////////////////////////////////////////////////////

	public Institution getInstitutionByName(final String name) {
		return getWithRestriction(Institution.class, Restrictions.eq(Institution.PROP_LABEL, name));
	}


	public void addInstitution(final Institution institution) {
		addObject(institution);
	}

	/**
	 * Retrieves the current session.
	 * 
	 * @return
	 */
	private Session getCurrentSession() {
		return getHibernateTemplate().getSessionFactory().getCurrentSession();
	}

	private <A> Criteria createCriteria(Class<A> clazz) {
		return getCurrentSession().createCriteria(clazz);
	}

	public List<Sms> getSMSByApplication(final Application app) {
		return findWithRestriction(Sms.class, Restrictions.eq(Sms.PROP_APP, app));
	}


	@SuppressWarnings("unchecked")
	public List<Statistic> getStatisticByApplication(final Application app) {
		List<Statistic> result = null;
		String queryString = "FROM Statistic statistic "
			+ "WHERE statistic.Id.App.Id=" + app.getId();

		result = getHibernateTemplate().find(queryString);

		return result;
	}


	public List<Account> getAccounts() {
		return getHibernateTemplate().loadAll(Account.class);
	}


	public void updateAccount(final Account account) {
		updateObject(account);
	}


	public List<Institution> getInstitutions() {
		return getHibernateTemplate().loadAll(Institution.class);
	}

	public List<Statistic> getStatistics() {
		return getHibernateTemplate().loadAll(Statistic.class);
	}

	public Account getAccountById(final int id) {
		return getWithRestriction(Account.class, Restrictions.eq(Account.PROP_ID, id));
	}

	public Institution getInstitutionById(final int id) {
		return getWithRestriction(Institution.class, Restrictions.eq(Institution.PROP_ID, id));
	}


	@SuppressWarnings("unchecked")
	public List<Statistic> getStatisticsSorted() {
		String select = "SELECT stat FROM Statistic stat";

		String orderBy = " ORDER BY stat.Id.App.Ins.Label,  "
			+ "stat.Id.App.Name, stat.Id.Acc.Label, " + "stat.Id.Month";

		return getHibernateTemplate().find(select + orderBy);
	}


	public List<Sms> getSms() {
		return getHibernateTemplate().loadAll(Sms.class);
	}

	public Sms getSmsById(Integer id) {
		return getWithRestriction(Sms.class, Restrictions.eq(Sms.PROP_ID, id));
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,?>> getSmsAccountsAndApplications() {

		Criteria criteria = createCriteria(Sms.class);

		criteria.setProjection(Projections.projectionList()
				.add( Projections.distinct(Projections.projectionList()
						.add(Projections.property(Sms.PROP_APP), Sms.PROP_APP)
						.add(Projections.property(Sms.PROP_ACC), Sms.PROP_ACC))));

		criteria.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<List<?>> searchGroupSmsWithInitialId(final Institution inst,
			final Account acc, final Application app, final Date startDate,
			final Date endDate, int maxResults) {

		Criteria criteria = criteriaSearchSms(inst, acc, app, startDate, endDate, maxResults);

		criteria.add(Restrictions.isNotNull(Sms.PROP_INITIAL_ID));

		criteria.setProjection(Projections.projectionList()
				.add( Projections.distinct(Projections.projectionList()
						.add(Projections.property(Sms.PROP_APP))
						.add(Projections.property(Sms.PROP_INITIAL_ID)))));

		criteria.setResultTransformer(Transformers.TO_LIST);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Sms> searchGroupSmsWithNullInitialId(final Institution inst,
			final Account acc, final Application app, final Date startDate,
			final Date endDate, int maxResults) {

		Criteria criteria = criteriaSearchSms(inst, acc, app, startDate, endDate, maxResults);

		criteria.add(Restrictions.isNull(Sms.PROP_INITIAL_ID));

		return (List<Sms>) criteria.list();
	}

	private Criteria criteriaSearchSms(final Institution inst,
			final Account acc, final Application app, final Date startDate,
			final Date endDate, int maxResults) {

		Criteria criteria = createCriteria(Sms.class);
		
		if (inst != null) {
			criteria.createCriteria(Sms.PROP_APP).add(Restrictions.eq(Application.PROP_INS, inst));
		}

		if (app != null) {
			criteria.add(Restrictions.eq(Sms.PROP_APP, app));
		}

		if (acc != null) {
			criteria.add(Restrictions.eq(Sms.PROP_ACC, acc));
		}

		if (startDate != null) {
			long startDateLong = startDate.getTime();
			java.sql.Date startDateSQL = new java.sql.Date(startDateLong);
			criteria.add(Restrictions.ge(Sms.PROP_DATE, startDateSQL));
		}

		if (endDate != null) {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(endDate);
			gregorianCalendar.add(Calendar.DAY_OF_YEAR,1); 
			Date newDate = gregorianCalendar.getTime();

			long endDateLong = newDate.getTime();
			java.sql.Date endDateSQL = new java.sql.Date(endDateLong);

			criteria.add(Restrictions.lt(Sms.PROP_DATE, endDateSQL));
		}

		criteria.addOrder(Order.desc(Sms.PROP_DATE));
		if (maxResults > 0) criteria.setMaxResults(maxResults);
		
		return criteria;
	}

	public Role getRoleByName(final String name) {
		return getWithRestriction(Role.class, Restrictions.eq(Role.PROP_NAME, name));
	}

	public List<Role> getRoles() {
		return getHibernateTemplate().loadAll(Role.class);
	}

	@SuppressWarnings("unchecked")
	public List<Sms> getSmsByApplicationAndInitialId(final Application application,
			final Integer smsInitialId) {

		Criteria criteria = createCriteria(Sms.class);
		criteria.add(Restrictions.eq(Sms.PROP_APP, application));
		if (smsInitialId != null) {
			criteria.add(Restrictions.eq(Sms.PROP_INITIAL_ID, smsInitialId));
		} else {
			criteria.add(Restrictions.isNull(Sms.PROP_INITIAL_ID));
		}
		return criteria.list();
	}


	@SuppressWarnings("unchecked")
	private <A> A getWithRestriction(Class<A> clazz, SimpleExpression restriction) {
		return (A) createCriteria(clazz).add(restriction).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	private <A> List<A> findWithRestriction(Class<A> clazz,
			SimpleExpression restriction) {
		return createCriteria(clazz).add(restriction).list();
	}

	protected void addObject(final Object object) {
		if (logger.isDebugEnabled()) {
			logger.debug("adding " + object + "...");
		}
		getCurrentSession().beginTransaction();
		getHibernateTemplate().save(object);
		getCurrentSession().getTransaction().commit();
		if (logger.isDebugEnabled()) {
			logger.debug("done.");
		}
	}

	/**
	 * Update an object in the database.
	 * @param object
	 */
	protected void updateObject(final Object object) {
		if (logger.isDebugEnabled()) {
			logger.debug("merging " + object + "...");
		}
		getCurrentSession().beginTransaction();
		Object merged = getHibernateTemplate().merge(object);
		if (logger.isDebugEnabled()) {
			logger.debug("done, updating " + merged + "...");
		}
		getHibernateTemplate().update(merged);
		getCurrentSession().getTransaction().commit();
		if (logger.isDebugEnabled()) {
			logger.debug("done.");
		}
	}

	/**
	 * Delete an object from the database.
	 * @param object
	 */
	protected void deleteObject(final Object object) {
		if (logger.isDebugEnabled()) {
			logger.debug("merging " + object + "...");
		}
		getCurrentSession().beginTransaction();
		Object merged = getHibernateTemplate().merge(object);
		if (logger.isDebugEnabled()) {
			logger.debug("done, deleting " + merged + "...");
		}
		getHibernateTemplate().delete(merged);
                getCurrentSession().getTransaction().commit();
		if (logger.isDebugEnabled()) {
			logger.debug("done.");
		}
	}

}
