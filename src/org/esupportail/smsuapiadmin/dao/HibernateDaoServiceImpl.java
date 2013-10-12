/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.dao.AbstractJdbcJndiHibernateDaoService;
import org.esupportail.commons.services.application.UninitializedDatabaseException;
import org.esupportail.commons.services.application.VersionningUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.beans.Account;
import org.esupportail.smsuapiadmin.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapiadmin.dao.beans.Sms;
import org.esupportail.smsuapiadmin.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.hibernate.classic.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.BadSqlGrammarException;

/**
 * The Hibernate implementation of the DAO service.
 */
@SuppressWarnings({"unchecked"})
public class HibernateDaoServiceImpl extends
AbstractJdbcJndiHibernateDaoService implements DaoService,
InitializingBean {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3152554337896617315L;

	/**
	 * The name of the 'id' attribute.
	 */
	@SuppressWarnings("unused")
	private static final String ID_ATTRIBUTE = "id";

	/**
	 * The name of the 'admin' attribute.
	 */
	private static final String ADMIN_ATTRIBUTE = "admin";

	/**
	 * Bean constructor.
	 */
	public HibernateDaoServiceImpl() {
		super();
	}

	// ////////////////////////////////////////////////////////////
	// User
	// ////////////////////////////////////////////////////////////



	public UserBoSmsu getUserById(final Integer id) {
		String queryString = "FROM UserBoSmsu user " + "WHERE user.Id="
		+ id + "";

		List<UserBoSmsu> users = getHibernateTemplate().find(queryString);
		if (users.size() == 1) {
			return users.get(0);
		}
		return null;
	}


	public UserBoSmsu getUserByLogin(final String login) {
		String queryString = "FROM UserBoSmsu user " + "WHERE user.Login='"
		+ login + "'";

		List<UserBoSmsu> users = getHibernateTemplate().find(queryString);
		if (users.size() == 1) {
			return (UserBoSmsu) users.get(0);
		}
		return null;
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
		String queryString = "FROM Application app " + "WHERE app.Id=" + id;

		List<Application> apps = getHibernateTemplate().find(queryString);
		if (apps.size() == 1) {
			return apps.get(0);
		}
		return null;
	}

	// ////////////////////////////////////////////////////////////
	// Account
	// ////////////////////////////////////////////////////////////


	public Account getAccountByName(final String name) {
		String queryString = "FROM Account account 	" + "WHERE account.Label='"
		+ name + "'";

		List<Account> accounts = getHibernateTemplate().find(queryString);
		if (accounts.size() == 1) {
			return accounts.get(0);
		}
		return null;
	}

	public void addAccount(final Account account) {
		addObject(account);
	}

	// ////////////////////////////////////////////////////////////
	// Institution
	// ////////////////////////////////////////////////////////////

	public Institution getInstitutionByName(final String name) {
		String queryString = "FROM Institution institution 	"
			+ "WHERE institution.Label='" + name + "'";

		List<Institution> institutions = getHibernateTemplate().find(queryString);
		if (institutions.size() == 1) {
			return institutions.get(0);
		}
		return null;
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


	public List<Sms> getSMSByApplication(final Application app) {
		List<Sms> result = null;
		String queryString = "FROM Sms sms " + "WHERE sms.App.Id="
		+ app.getId();

		result = getHibernateTemplate().find(queryString);

		return result;
	}


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
		String queryString = "FROM Account acc " + "WHERE acc.Id=" + id;

		List<Account> accs = getHibernateTemplate().find(queryString);
		if (accs.size() == 1) {
			return accs.get(0);
		}
		return null;
	}


	public Institution getInstitutionById(final int id) {
		String queryString = "FROM Institution inst " + "WHERE inst.Id=" + id;

		List<Institution> institutions = getHibernateTemplate().find(queryString);
		if (institutions.size() == 1) {
			return institutions.get(0);
		}
		return null;
	}


	public List<Statistic> searchStatistics(final Institution institution,
			final Account account, final Application application,
			final Date month) {

		List<Statistic> result = null;

		// SELECT
		StringBuffer select = new StringBuffer();
		select.append("SELECT stat ");

		// FROM
		StringBuffer from = new StringBuffer();
		from.append(" FROM Statistic stat ");
		// on fait une jointure si le critere 'institution' est renseigne
		if (institution != null) {
			from.append(", Application app ");
		}

		// WHERE
		StringBuffer where = new StringBuffer();
		where.append(" WHERE ");
		boolean whereClause = false;
		String keywordAND = " AND ";
		if (institution != null) {
			where.append(" app.Institution.Id=" + institution.getId()
					+ keywordAND);
			where.append(" stat.Id.App.Id=app.Id" + keywordAND);
			whereClause = true;
		}
		if (application != null) {
			where.append(" stat.Id.App.Id=" + application.getId() + keywordAND);
			whereClause = true;
		}
		if (account != null) {
			where.append(" stat.Id.Acc.Id=" + account.getId() + keywordAND);
			whereClause = true;
		}
		if (month != null) {
			long monthLong = month.getTime();
			java.sql.Date monthSQL = new java.sql.Date(monthLong);
			where.append(" stat.Id.Month='" + monthSQL + "'" + keywordAND);
			whereClause = true;
		}

		// ORDER BY
		String orderBy = " ORDER BY stat.Id.App.Institution.Label,  "
			+ "stat.Id.App.Name, stat.Id.Acc.Label, " + "stat.Id.Month";

		String queryString = "";

		StringBuffer selectFrom = select.append(from.toString());

		if (whereClause) {
			final int sizeAND = 5;
			String whereStr = where.substring(0, where.length() - sizeAND);
			queryString = selectFrom.append(whereStr).toString();
		} else {
			queryString = selectFrom.toString();
		}

		queryString = queryString + orderBy;

		result = getHibernateTemplate().find(queryString);

		return result;
	}


	public List<Sms> getSms() {
		return getHibernateTemplate().loadAll(Sms.class);
	}


	public List<Map<String,?>> searchGroupSms(final Institution inst,
			final Account acc, final Application app, final Date startDate,
			final Date endDate, int maxResults) {

		Criteria criteria = getCurrentSession().createCriteria(Sms.class);

		criteria.setProjection(Projections.projectionList()
				.add( Projections.distinct(Projections.projectionList()
						.add(Projections.property(Sms.PROP_APP), "application")
						.add(Projections.property(Sms.PROP_INITIAL_ID), Sms.PROP_INITIAL_ID))));

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

		criteria.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

		return criteria.list();
	}

	public Role getRoleById(final String id) {
		Role result = null;
		String queryString = "FROM Role role " + "WHERE role.Id=" + id;

		List roles = getHibernateTemplate().find(queryString);
		if (roles.size() == 1) {
			result = (Role) roles.get(0);
		} else {
			// TODO
		}
		return result;
	}


	public List<Role> getRoles() {
		return getHibernateTemplate().loadAll(Role.class);
	}

	public List<Sms> getSmsByApplicationAndInitialId(final Application application,
			final Integer smsInitialId) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Sms.class);
		criteria.add(Restrictions.eq(Sms.PROP_APP, application));
		if (smsInitialId != null) {
			criteria.add(Restrictions.eq(Sms.PROP_INITIAL_ID, smsInitialId));
		} else {
			criteria.add(Restrictions.isNull(Sms.PROP_INITIAL_ID));
		}
		return getHibernateTemplate().findByCriteria(criteria);
	}

}
