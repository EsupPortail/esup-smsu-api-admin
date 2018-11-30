package org.esupportail.smsuapiadmin.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapi.dao.beans.Application;
import org.esupportail.smsuapi.dao.beans.Institution;
import org.esupportail.smsuapi.dao.beans.Sms;
import org.esupportail.smsuapi.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;

/**
 * The DAO service interface.
 *
 */
public interface DaoService {
	
	public UserBoSmsu getUserById(final Integer id);

	public UserBoSmsu getUserByLogin(final String login);

	public List<UserBoSmsu> getUsers();

	public void addUser(final UserBoSmsu user);

	public void deleteUser(final UserBoSmsu user);

	public void updateUser(final UserBoSmsu user);

	public void addApplication(final Application app);

	public void deleteApplication(final Application app);

	public List<Application> getApplications();

	public void updateApplication(final Application app);

	public Application getApplicationById(final int id);

	public Application getApplicationByName(String name);
	
	public Account getAccountByName(final String name);

	public void addAccount(final Account account);
	
	public Institution getInstitutionByName(final String name);

	public void addInstitution(final Institution institution);
	
	public List<Sms> getSMSByApplication(final Application app);

	public List<Account> getAccounts();

	public void updateAccount(final Account account);

	public List<Institution> getInstitutions();

	public List<Statistic> getStatistics();
	
	public List<Statistic> getStatisticByApplication(final Application app);

	public Account getAccountById(final int id);
	
	public Institution getInstitutionById(final int id);

	public List<Statistic> getStatisticsSorted();

	public List<Sms> getSms();

	public Sms getSmsById(Integer id);

	public List<Map<String,?>> getSmsAccountsAndApplications();
	
	public List<List<?>> searchGroupSmsWithInitialId(final Institution inst,
			final Account acc, final Application app, final Date startDate,
			final Date endDate, int maxResults);
	
	public List<Sms> searchGroupSmsWithNullInitialId(final Institution inst,
			final Account acc, final Application app, final Date startDate,
			final Date endDate, int maxResults);
	
	public Role getRoleByName(final String name);

	public List<Role> getRoles();

	public List<Sms> getSmsByApplicationAndInitialId(final Application application,
			final Integer smsInitialId);
		
}
