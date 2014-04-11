package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Account;
import org.esupportail.smsuapiadmin.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dao.beans.Sms;
import org.esupportail.smsuapiadmin.dao.beans.SmsStatus;
import org.esupportail.smsuapiadmin.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedCriteria;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;

/**
 * StatisticManager is the business layer between the web and the database for
 * 'statistic' objects.
 * 
 * @author MZRL3760
 * 
 */
@SuppressWarnings("serial")
public class StatisticManager 
	extends	AbstractApplicationAwareBean {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link DTOConverterService}.
	 */
	private DTOConverterService dtoConverterService;

	/**
	 * constructor.
	 */
	public StatisticManager() {
		super();
	}

	/**
	 * Setter for 'dtoConverterService'.
	 * 
	 * @param dtoConverterService
	 */
	public void setDtoConverterService(
			final DTOConverterService dtoConverterService) {
		this.dtoConverterService = dtoConverterService;
	}

	/**
	 * Setter for 'daoService'.
	 * 
	 * @param daoService
	 *            the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * Retrieves all the accounts defined in database.
	 * 
	 * @return
	 */
	public List<Statistic> getAllStatistics() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve the statistics from the database");
		}
		final List<Statistic> allStatistics = daoService.getStatistics();
		return allStatistics;
	}

	/**
	 * Returns all statistics.
	 * 
	 * @return
	 */
	public List<UIStatistic> getAllUIStatistics() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieves the statistics from the database");
		}
		final List<UIStatistic> allUIStatistics = new ArrayList<UIStatistic>();
		final List<Statistic> allStatistics = daoService.getStatistics();

		for (final Statistic stat : allStatistics) {
			final UIStatistic ui = dtoConverterService.convertToUI(stat);
			allUIStatistics.add(ui);
		}
		return allUIStatistics;
	}

	/**
	 * Returns all statistics sorted
	 * 
	 * @return
	 */
	public List<UIStatistic> getStatisticsSorted() {

		final List<UIStatistic> result = new ArrayList<UIStatistic>();
		final List<Statistic> stats = daoService.getStatisticsSorted();

		for (final Statistic stat : stats) {
			final UIStatistic uiStat = dtoConverterService.convertToUI(stat);
			result.add(uiStat);
		}

		return result;
	}

	/**
	 * Returns accounts and applications for detailed statistics
	 * 
	 * @return
	 */
	public List<UIDetailedCriteria> getDetailedStatisticsCriteria() {
		final List<UIDetailedCriteria> result = new ArrayList<UIDetailedCriteria>();
		for (Map<String,?> map : daoService.getSmsAccountsAndApplications()) {			
			final Application app = (Application) map.get(Sms.PROP_APP);
			final Account acc = (Account) map.get(Sms.PROP_ACC);
			result.add(new UIDetailedCriteria(acc.getLabel(), app.getName(), app.getInstitution().getLabel()));
		}
		return result;
	}


	public List<UIDetailedSummary> searchDetailedSummaries(
			final String institution, final String accountLabel,
			final String applicationName, final Date startDate, final Date endDate, int maxResults) throws Exception {
		Institution inst = null;
		if (institution != null) {
			inst = daoService.getInstitutionByName(institution);
			if (inst == null) throw new NotFoundException("invalid institution " + institution);
		}
		Account acc = null;
		if (accountLabel != null) {
			acc = daoService.getAccountByName(accountLabel);
			if (acc == null) throw new NotFoundException("invalid account " + accountLabel);
		}
		Application app = null;
		if (applicationName != null) {
			app = daoService.getApplicationByName(applicationName);
			if (app == null) throw new NotFoundException("invalid application " + applicationName);
		}
		return searchDetailedSummaries(inst, acc, app, startDate, endDate, maxResults);
	}

	/**
	 * Searches sms that respect criterias and makes detailed summaries.
	 * 
	 * @param inst
	 * @param acc
	 * @param app
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<UIDetailedSummary> searchDetailedSummaries(
			final Institution inst, final Account acc, final Application app,
			final Date startDate, final Date endDate, int maxResults) {

		final List<UIDetailedSummary> result = new ArrayList<UIDetailedSummary>();

		// rappel : les releves detailles sont groupes par SMS_INITIAL_ID
		// les statistiques concernent ce groupe d'envoi.

		// on commence par recuperer tous les groupes d'envois correspondant
		// aux criteres et ayant un SMS_INITIAL_ID
		final List<List<?>> groupSms = daoService.searchGroupSmsWithInitialId(inst, acc, app, startDate, endDate, maxResults);
		
		// pour chaque groupe d'envoi, on construit le releve detaille
		for (final List<?> map : groupSms) {
			final Application application = (Application) map.get(0);
			final Integer smsInitialId = (Integer) map.get(1);
			
			final List<Sms> smsList = daoService
					.getSmsByApplicationAndInitialId(application, smsInitialId);
			result.add(computeDetailedSummary(smsList));
		}

		// BELOW IS DEPRECATED: smsuapi do not create sms entries with NULL SMS_INITIAL_ID
		// for migration, run the following mysql command:
		//   SELECT @n := MAX(sms_initial_id) FROM sms;
		//   UPDATE sms SET sms_initial_id = (@n := @n + 1) WHERE sms_initial_id IS NULL;		
		//
		// on recommence avec les envois n'ayant pas de SMS_INITIAL_ID
		List<Sms> smsNoInitialId = daoService.searchGroupSmsWithNullInitialId(inst, acc, app, startDate, endDate, maxResults);		
		for (Sms sms : smsNoInitialId) {
			result.add(computeDetailedSummary(Collections.singletonList(sms)));
		}
				
		// il faut retrier le tout, puis limiter le nombre de results a maxResults
		Collections.sort(result, Collections.reverseOrder());	
		return truncateList(result, maxResults);
	}

	private <T> List<T> truncateList(List<T> list, int nb) {
		try {
			return list.subList(0, nb);
		} catch (IndexOutOfBoundsException e) {
			// the list is smaller than maxResults, keeping result unchanged
			return list;
		}
	}

	private UIDetailedSummary computeDetailedSummary(final List<Sms> smsList) {
		assert !smsList.isEmpty();
		final Sms firstSms = smsList.get(0);
		final UIDetailedSummary summary = new UIDetailedSummary(getI18nService());

		// on recupere les objets institution application et account qui sont identiques pour tous les sms d'un meme groupe
		summary.setInstitution(firstSms.getApp().getInstitution().getLabel());
		summary.setAppName(firstSms.getApp().getName());
		summary.setAccountName(firstSms.getAcc().getLabel());

		computeMinDateAndStats(smsList, summary);

		return summary;
	}

	private void computeMinDateAndStats(final List<Sms> smsList, final UIDetailedSummary summary) {
		Date minDate = null;

		// le decompte
		final Map<SmsStatus, Integer> stats = new EnumMap<SmsStatus, Integer>(SmsStatus.class);
		for (SmsStatus status : SmsStatus.values()) stats.put(status, new Integer(0));

		for (final Sms sms : smsList) {
			final Date smsDate = sms.getDate();
			if (minDate == null || smsDate.before(minDate)) {
				minDate = smsDate;
			}
			final SmsStatus status = SmsStatus.valueOf(sms.getState());
			stats.put(status, stats.get(status) + 1);
		}
		summary.setDate(minDate);
		summary.setStatistics(stats);
	}


}
