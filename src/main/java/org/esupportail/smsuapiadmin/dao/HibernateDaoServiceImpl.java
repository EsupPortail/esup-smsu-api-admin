/**
 * SMS-U - Copyright (c) 2009-2018 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapi.dao.beans.Application;
import org.esupportail.smsuapi.dao.beans.Institution;
import org.esupportail.smsuapi.dao.beans.Sms;
import org.esupportail.smsuapi.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.transform.Transformers;

/**
 * The Hibernate implementation of the DAO service interface.
 */
public class HibernateDaoServiceImpl implements DaoService {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = Logger.getLogger(getClass());

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Bean constructor.
	 */
	public HibernateDaoServiceImpl() {
		super();
	}

	/**
	 * Retrieves the current session.
	 * 
	 * @return
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	private <A> Criteria createCriteria(Class<A> clazz) {
		return getCurrentSession().createCriteria(clazz);
	}
	
	// //////////////////////////////////////////////////////////
	// User
	// //////////////////////////////////////////////////////////
	
	@Override
	public UserBoSmsu getUserById(Integer id) {
		// TODO Auto-generated method stub
		return getWithRestriction(UserBoSmsu.class, Restrictions.eq(UserBoSmsu.PROP_ID, id));
	}

	@Override
	public UserBoSmsu getUserByLogin(String login) {
		// TODO Auto-generated method stub
		return getWithRestriction(UserBoSmsu.class, Restrictions.eq(UserBoSmsu.PROP_LOGIN, login));
	}

	@Override
	public List<UserBoSmsu> getUsers() {
		// TODO Auto-generated method stub
		return findAll(UserBoSmsu.class);
	}

	@Override
	public void addUser(UserBoSmsu user) {
		// TODO Auto-generated method stub
		addObject(user);
	}

	@Override
	public void deleteUser(UserBoSmsu user) {
		// TODO Auto-generated method stub
		deleteObject(user);
	}

	@Override
	public void updateUser(UserBoSmsu user) {
		// TODO Auto-generated method stub
		updateObject(user);
	}

	// ////////////////////////////////////////////////////////////
	// Application
	// ////////////////////////////////////////////////////////////
	
	@Override
	public void addApplication(Application app) {
		// TODO Auto-generated method stub
		addObject(app);
	}

	@Override
	public void deleteApplication(Application app) {
		// TODO Auto-generated method stub
		deleteObject(app);
	}

	@Override
	public List<Application> getApplications() {
		// TODO Auto-generated method stub
		return findAll(Application.class);
	}

	@Override
	public void updateApplication(Application app) {
		// TODO Auto-generated method stub
		updateObject(app);
	}

	@Override
	public Application getApplicationById(int id) {
		// TODO Auto-generated method stub
		return getWithRestriction(Application.class, Restrictions.eq(Application.PROP_ID, id));
	}

	@Override
	public Application getApplicationByName(String name) {
		// TODO Auto-generated method stub
		return getWithRestriction(Application.class, Restrictions.eq(Application.PROP_NAME, name));
	}

	// ////////////////////////////////////////////////////////////
	// Account
	// ////////////////////////////////////////////////////////////
	
	@Override
	public Account getAccountByName(String name) {
		// TODO Auto-generated method stub
		return getWithRestriction(Account.class, Restrictions.eq(Account.PROP_LABEL, name));
	}

	@Override
	public void addAccount(Account account) {
		// TODO Auto-generated method stub
		addObject(account);
	}

	// ////////////////////////////////////////////////////////////
	// Institution
	// ////////////////////////////////////////////////////////////
	
	@Override
	public Institution getInstitutionByName(String name) {
		// TODO Auto-generated method stub
		return getWithRestriction(Institution.class, Restrictions.eq(Institution.PROP_LABEL, name));
	}

	@Override
	public void addInstitution(Institution institution) {
		// TODO Auto-generated method stub
		addObject(institution);
	}

	@Override
	public List<Sms> getSMSByApplication(Application app) {
		// TODO Auto-generated method stub
		return findWithRestriction(Sms.class, Restrictions.eq(Sms.PROP_APP, app));
	}

	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		return findAll(Account.class);
	}

	@Override
	public void updateAccount(Account account) {
		// TODO Auto-generated method stub
		updateObject(account);
	}

	@Override
	public List<Institution> getInstitutions() {
		// TODO Auto-generated method stub
		return findAll(Institution.class);
	}

	@Override
	public List<Statistic> getStatistics() {
		// TODO Auto-generated method stub
		return findAll(Statistic.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Statistic> getStatisticByApplication(final Application app) {
		String queryString = "FROM Statistic statistic "
		                     + "WHERE statistic.Id.App.Id=" + app.getId();
        return getCurrentSession().createQuery(queryString).list();
    }

	@Override
	public Account getAccountById(int id) {
		// TODO Auto-generated method stub
		return getWithRestriction(Account.class, Restrictions.eq(Account.PROP_ID, id));
	}

	@Override
	public Institution getInstitutionById(int id) {
		// TODO Auto-generated method stub
		return getWithRestriction(Institution.class, Restrictions.eq(Institution.PROP_ID, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Statistic> getStatisticsSorted() {
		// TODO Auto-generated method stub
		String select = "SELECT stat FROM Statistic stat";

		String orderBy = " ORDER BY stat.Id.App.Ins.Label,  "
			+ "stat.Id.App.Name, stat.Id.Acc.Label, " + "stat.Id.Month";

		return (List<Statistic>) getCurrentSession().createQuery(select + orderBy).list();
	}

	@Override
	public List<Sms> getSms() {
		// TODO Auto-generated method stub
		return findAll(Sms.class);
	}

	@Override
	public Sms getSmsById(Integer id) {
		// TODO Auto-generated method stub
		return getWithRestriction(Sms.class, Restrictions.eq(Sms.PROP_ID, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, ?>> getSmsAccountsAndApplications() {
		// TODO Auto-generated method stub
		Criteria criteria = createCriteria(Sms.class);

		criteria.setProjection(Projections.projectionList()
				.add( Projections.distinct(Projections.projectionList()
						.add(Projections.property(Sms.PROP_APP), Sms.PROP_APP)
						.add(Projections.property(Sms.PROP_ACC), Sms.PROP_ACC))));

		criteria.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<?>> searchGroupSmsWithInitialId(Institution inst, Account acc, Application app, Date startDate,
			Date endDate, int maxResults) {
		// TODO Auto-generated method stub
		Criteria criteria = criteriaSearchSms(inst, acc, app, startDate, endDate, maxResults);

		criteria.add(Restrictions.isNotNull(Sms.PROP_INITIAL_ID));

		criteria.setProjection(Projections.projectionList()
				.add( Projections.distinct(Projections.projectionList()
						.add(Projections.property(Sms.PROP_APP))
						.add(Projections.property(Sms.PROP_INITIAL_ID))
						.add(Projections.property(Sms.PROP_DATE)))));

		criteria.setResultTransformer(Transformers.TO_LIST);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sms> searchGroupSmsWithNullInitialId(Institution inst, Account acc, Application app, Date startDate,
			Date endDate, int maxResults) {
		// TODO Auto-generated method stub
		Criteria criteria = criteriaSearchSms(inst, acc, app, startDate, endDate, maxResults);

		criteria.add(Restrictions.isNull(Sms.PROP_INITIAL_ID));

		return (List<Sms>) criteria.list();
	}

	@Override
	public Role getRoleByName(String name) {
		// TODO Auto-generated method stub
		return getWithRestriction(Role.class, Restrictions.eq(Role.PROP_NAME, name));
	}

	@Override
	public List<Role> getRoles() {
		// TODO Auto-generated method stub
		return findAll(Role.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sms> getSmsByApplicationAndInitialId(Application application, Integer smsInitialId) {
		// TODO Auto-generated method stub
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

	@SuppressWarnings("unchecked")
	private <A> List<A> findAll(Class<A> clazz) {
		return createCriteria(clazz).list();
	}

	protected void addObject(final Object object) {
		if (logger.isDebugEnabled()) {
			logger.debug("adding " + object + "...");
		}
		getCurrentSession().save(object);
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
		Object merged = getCurrentSession().merge(object);
		if (logger.isDebugEnabled()) {
			logger.debug("done, updating " + merged + "...");
		}
		getCurrentSession().update(merged);
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
		Object merged = getCurrentSession().merge(object);
		if (logger.isDebugEnabled()) {
			logger.debug("done, deleting " + merged + "...");
		}
		getCurrentSession().delete(merged);
		if (logger.isDebugEnabled()) {
			logger.debug("done.");
		}
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
}
