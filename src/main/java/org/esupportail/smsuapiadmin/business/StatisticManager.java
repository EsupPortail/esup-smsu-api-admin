package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
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
	 * Searches sms that respect criterias and makes detailed summaries.
	 * 
	 * @param inst
	 * @param acc
	 * @param app
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<UIDetailedSummary> searchDetailedSummaries(
			final Institution inst, final Account acc, final Application app,
			final Date startDate, final Date endDate, int maxResults) {

		final List<UIDetailedSummary> result = new ArrayList<UIDetailedSummary>();

		// rappel : les releves detailles sont groupes par SMS_INITIAL_ID
		// les statistiques concernent ce groupe d'envoi.

		// on commence par recuperer tous les groupes d'envois correspondant
		// aux criteres
		final List<Map<String,?>> groupSms = daoService.searchGroupSms(inst, acc, app, startDate, endDate, maxResults);
		
		// pour chaque groupe d'envoi, on construit le releve detaille
		//for (final Integer smsInitialId : groupSms) {
		for (final Map<String,?> map : groupSms) {

			final Application application = (Application) map.get("application");
			final Integer smsInitialId = (Integer) map.get(Sms.PROP_INITIAL_ID);
			final List<Sms> smsList = daoService
					.getSmsByApplicationAndInitialId(application, smsInitialId);
			final UIDetailedSummary summary = new UIDetailedSummary(getI18nService());

			// on recupere les objets institution, application et account
			// identique pour tous les sms d'un meme groupe
			assert !smsList.isEmpty();
			final Sms firstSms = smsList.get(0);
			final String uiInst = dtoConverterService.convertToUI(firstSms.getApp().getInstitution());
			summary.setInstitution(uiInst);
			summary.setAppName(firstSms.getApp().getName());
			summary.setAccountName(firstSms.getAcc().getLabel());

			Date minDate = null;

			// le decompte
			final Map<SmsStatus, Integer> stats = new EnumMap<SmsStatus, Integer>(SmsStatus.class);
			for (SmsStatus status : SmsStatus.values()) stats.put(status, new Integer(0));

			// on parcourt les sms
			for (final Sms sms : smsList) {
				// gestion de la date
				final Date smsDate = sms.getDate();
				if (minDate == null || smsDate.before(minDate)) {
					minDate = smsDate;
				}
				// gestion du statut
				final SmsStatus status = SmsStatus.valueOf(sms.getState());
				stats.put(status, stats.get(status) + 1);
			}
			// la date
			summary.setDate(minDate);
			summary.setStatistics(stats);

			result.add(summary);
		}
		return result;
	}


}
