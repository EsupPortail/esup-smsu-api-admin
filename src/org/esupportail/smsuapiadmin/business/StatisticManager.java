package org.esupportail.smsuapiadmin.business;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

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
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
import org.esupportail.smsuapiadmin.dto.beans.UIInstitution;
import org.esupportail.smsuapiadmin.dto.beans.UISms;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;

/**
 * StatisticManager is the business layer between the web and the database for
 * 'statistic' objects.
 * 
 * @author MZRL3760
 * 
 */
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
	 * Returns months that appears in database.
	 * 
	 * @return
	 */
	public SortedSet<Date> getMonthsOfStatistics() {
		final SortedSet<Date> result = new TreeSet<Date>();
		final List<Statistic> allStatistics = getAllStatistics();

		for (final Statistic stat : allStatistics) {
			result.add(stat.getId().getMonth());
		}

		return result;
	}

	/**
	 * Searches statistics that respect criterias.
	 * 
	 * @param institutionId
	 * @param accountId
	 * @param applicationId
	 * @param month
	 * @return
	 */
	public List<UIStatistic> searchStatistics(final Institution institution,
			final Account account, final Application application,
			final Date month) {

		final List<UIStatistic> result = new ArrayList<UIStatistic>();
		final List<Statistic> stats = daoService.searchStatistics(institution,
				account, application, month);

		for (final Statistic stat : stats) {
			final UIStatistic uiStat = dtoConverterService.convertToUI(stat);
			result.add(uiStat);
		}

		return result;
	}

	/**
	 * Makes a JasperPrint for consolidated report.
	 * 
	 * @param data
	 * @param inst
	 *            Insitution criteria
	 * @param app
	 *            Application criteria
	 * @param acc
	 *            Account criteria
	 * @param month
	 *            Month criteria
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	private JasperPrint makeConsolidatedJasperPrint(
			final List<UIStatistic> data, final Institution inst,
			final Application app, final Account acc, final Date month)
			throws IOException, JRException {
		JasperPrint result = null;

		// on r�cup�re le fichier .jasper (d�j� compil�)
		final InputStream jasperFile = this.getClass().getResourceAsStream(
				"/properties/jasper/consolidated_report.jasper");
		// on r�cup�re le logo pour l'ajouter au report
		final InputStream logoStream = this.getClass().getResourceAsStream(
				"/properties/jasper/Logo_Univ_Paris.gif");
		final Image logoImage = ImageIO.read(logoStream);

		// Les param�tres
		final Map<String, Object> parameters = new HashMap<String, Object>();
		// on recupere les chaines i18n
		final String softName = getI18nService().getString("SOFTWARE.NAME");
		final String reportTitle = getI18nService().getString(
				"SUMMARY.CONSOLIDATED.REPORT.TITLE");
		final String columnInstitution = getI18nService().getString(
				"INSTITUTION.NAME");
		final String columnApplication = getI18nService().getString(
				"APPLICATION.NAME");
		final String columnAccount = getI18nService().getString("ACCOUNT.NAME");
		final String columnMonth = getI18nService().getString("MONTH.NAME");
		final String columnSendedSMS = getI18nService().getString(
				"STATISTIC.SENDEDSMS");
		final String columnReceivedSMS = getI18nService().getString(
				"STATISTIC.RECEIVEDSMS");
		final String columnFailRate = getI18nService().getString(
				"STATISTIC.FAILRATE");
		final String criterias = getI18nService()
				.getString("SUMMARY.CRITERIAS");
		// Gestion des crit�res
		String instCriteria = "";
		final String i18nSELECTALL = "SELECT.ALL";
		if (inst != null) {
			instCriteria = inst.getLabel();
		} else {
			instCriteria = getI18nService().getString(i18nSELECTALL);
		}
		String appCriteria = "";
		if (app != null) {
			appCriteria = app.getName();
		} else {
			appCriteria = getI18nService().getString(i18nSELECTALL);
		}
		String accCriteria = "";
		if (acc != null) {
			accCriteria = acc.getLabel();
		} else {
			accCriteria = getI18nService().getString(i18nSELECTALL);
		}
		String monthCriteria = "";
		if (month != null) {
			final String pattern = "MMM yyyy";
			final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			monthCriteria = sdf.format(month);
		} else {
			monthCriteria = getI18nService().getString(i18nSELECTALL);
		}

		parameters.put("LOGO_UNIV_PARIS", logoImage);
		parameters.put("SOFTWARE_NAME", softName);
		parameters.put("REPORT_TITLE", reportTitle);
		parameters.put("COLUMN_INSTITUTION", columnInstitution);
		parameters.put("COLUMN_APPLICATION", columnApplication);
		parameters.put("COLUMN_ACCOUNT", columnAccount);
		parameters.put("COLUMN_MONTH", columnMonth);
		parameters.put("COLUMN_SENDEDSMS", columnSendedSMS);
		parameters.put("COLUMN_RECEIVEDSMS", columnReceivedSMS);
		parameters.put("COLUMN_FAILRATE", columnFailRate);
		parameters.put("CRITERIAS", criterias);
		parameters.put("INSTITUTION_CRITERIA", instCriteria);
		parameters.put("APPLICATION_CRITERIA", appCriteria);
		parameters.put("ACCOUNT_CRITERIA", accCriteria);
		parameters.put("MONTH_CRITERIA", monthCriteria);

		// la source de donn�es
		final ConsolidatedSummaryDataSource dataSource = new ConsolidatedSummaryDataSource(
				data);

		// on remplit le report avec les param�tres et la source de donn�es
		result = JasperFillManager.fillReport(jasperFile, parameters,
				dataSource);

		return result;
	}

	/**
	 * Makes a JasperPrint for detailed report.
	 * 
	 * @param data
	 * @param inst
	 * @param app
	 * @param acc
	 * @param month
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	private JasperPrint makeDetailedJasperPrint(
			final List<UIDetailedSummary> data, final Institution inst,
			final Application app, final Account acc, final Date startDate,
			final Date endDate) throws IOException, JRException {
		JasperPrint result = null;

		// on r�cup�re le fichier .jasper (d�j� compil�)
		final InputStream jasperFile = this.getClass().getResourceAsStream(
				"/properties/jasper/detailed_report.jasper");
		// on r�cup�re le logo pour l'ajouter au report
		final InputStream logoStream = this.getClass().getResourceAsStream(
				"/properties/jasper/Logo_Univ_Paris.gif");
		final Image logoImage = ImageIO.read(logoStream);

		// Les param�tres
		final Map<String, Object> parameters = new HashMap<String, Object>();
		// on recupere les chaines i18n
		final String softName = getI18nService().getString("SOFTWARE.NAME");
		final String reportTitle = getI18nService().getString(
				"SUMMARY.DETAILED.REPORT.TITLE");
		final String labelInstitution = getI18nService().getString(
				"INSTITUTION.NAME");
		final String labelApplication = getI18nService().getString(
				"APPLICATION.NAME");
		final String labelAccount = getI18nService().getString("ACCOUNT.NAME");
		final String labelDate = getI18nService().getString("DATE.NAME");
		final String labelStartDate = getI18nService().getString(
				"STARTDATE.NAME");
		final String labelEndDate = getI18nService().getString("ENDDATE.NAME");
		final String labelSending = getI18nService().getString(
				"SUMMARY.SENDING");
		final String labelCaracteristics = getI18nService().getString(
				"SUMMARY.CARACTERISTICS.NAME");
		final String labelCount = getI18nService().getString(
				"SUMMARY.COUNT.NAME");
		final String labelSMSCount = getI18nService().getString(
				"SUMMARY.SMSCOUNT");
		final String smsStatusCreated = getI18nService().getString(
				"SMS.STATUS.CREATED.NAME");
		final String smsStatusInProgress = getI18nService().getString(
				"SMS.STATUS.IN_PROGRESS.NAME");
		final String smsStatusDelivered = getI18nService().getString(
				"SMS.STATUS.DELIVERED.NAME");
		final String smsStatusErrorQuota = getI18nService().getString(
				"SMS.STATUS.ERROR_QUOTA.NAME");
		final String smsStatusErrorPreBl = getI18nService().getString(
				"SMS.STATUS.ERROR_PRE_BL.NAME");
		final String smsStatusErrorPostBl = getI18nService().getString(
				"SMS.STATUS.ERROR_POST_BL.NAME");
		final String smsStatusError = getI18nService().getString(
				"SMS.STATUS.ERROR.NAME");

		final String criterias = getI18nService()
				.getString("SUMMARY.CRITERIAS");

		// Gestion des crit�res
		String instCriteria = "";
		if (inst != null) {
			instCriteria = inst.getLabel();
		} else {
			instCriteria = getI18nService().getString("SELECT.ALL");
		}

		String appCriteria = "";
		if (app != null) {
			appCriteria = app.getName();
		} else {
			appCriteria = getI18nService().getString("SELECT.ALL");
		}

		String accCriteria = "";
		if (acc != null) {
			accCriteria = acc.getLabel();
		} else {
			accCriteria = getI18nService().getString("SELECT.ALL");
		}

		String startDateCriteria = "";
		if (startDate != null) {
			final String pattern = "dd MMM yyyy";
			final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			startDateCriteria = sdf.format(startDate);
		} else {
			startDateCriteria = getI18nService().getString("UNDEFINED.NAME");
		}

		String endDateCriteria = "";
		if (endDate != null) {
			final String pattern = "dd MMM yyyy";
			final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			endDateCriteria = sdf.format(endDate);
		} else {
			endDateCriteria = getI18nService().getString("UNDEFINED.NAME");
		}

		parameters.put("LOGO_UNIV_PARIS", logoImage);
		parameters.put("SOFTWARE_NAME", softName);
		parameters.put("REPORT_TITLE", reportTitle);
		parameters.put("INSTITUTION_LABEL", labelInstitution);
		parameters.put("APPLICATION_LABEL", labelApplication);
		parameters.put("ACCOUNT_LABEL", labelAccount);
		parameters.put("DATE_LABEL", labelDate);
		parameters.put("STARTDATE_LABEL", labelStartDate);
		parameters.put("ENDDATE_LABEL", labelEndDate);
		parameters.put("TITLE_SENDING", labelSending);
		parameters.put("CARACTERISTICS", labelCaracteristics);
		parameters.put("COUNT", labelCount);
		parameters.put("SMS_COUNT_LABEL", labelSMSCount);

		parameters.put("SMS_STATUS_CREATED", smsStatusCreated);
		parameters.put("SMS_STATUS_IN_PROGRESS", smsStatusInProgress);
		parameters.put("SMS_STATUS_DELIVERED", smsStatusDelivered);
		parameters.put("SMS_STATUS_ERROR_PRE_BL", smsStatusErrorPreBl);
		parameters.put("SMS_STATUS_ERROR_POST_BL", smsStatusErrorPostBl);
		parameters.put("SMS_STATUS_ERROR_QUOTA", smsStatusErrorQuota);
		parameters.put("SMS_STATUS_ERROR", smsStatusError);

		parameters.put("CRITERIAS", criterias);
		parameters.put("INSTITUTION_CRITERIA", instCriteria);
		parameters.put("APPLICATION_CRITERIA", appCriteria);
		parameters.put("ACCOUNT_CRITERIA", accCriteria);
		parameters.put("STARTDATE_CRITERIA", startDateCriteria);
		parameters.put("ENDDATE_CRITERIA", endDateCriteria);

		// la source de donn�es
		final DetailedSummaryDataSource dataSource = new DetailedSummaryDataSource(
				data);

		// on remplit le report avec les param�tres et la source de donn�es
		result = JasperFillManager.fillReport(jasperFile, parameters,
				dataSource);

		return result;
	}

	/**
	 * Makes a report.
	 * 
	 * @param data
	 * @param inst
	 * @param app
	 * @param acc
	 * @param month
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	public byte[] makeReportForConsolidatedSummaries(final FormatReport format,
			final List<UIStatistic> data, final Institution inst,
			final Application app, final Account acc, final Date month)
			throws IOException, JRException {
		byte[] result = null;

		// on construit le jasper print
		final JasperPrint jasperPrint = makeConsolidatedJasperPrint(data, inst,
				app, acc, month);

		if (format.equals(FormatReport.XLS)) {
			// on l'exporte en Excel
			final ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			final JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRXlsAbstractExporterParameter.JASPER_PRINT,
					jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();

			result = xlsReport.toByteArray();

		} else if (format.equals(FormatReport.PDF)) {
			// on exporte en pdf
			result = JasperExportManager.exportReportToPdf(jasperPrint);
		}

		return result;
	}

	/**
	 * 
	 * @param format
	 * @param data
	 * @param inst
	 * @param app
	 * @param acc
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public byte[] makeReportForDetailedSummaries(final FormatReport format,
			final List<UIDetailedSummary> data, final Institution inst,
			final Application app, final Account acc, final Date startDate,
			final Date endDate) throws IOException, JRException {
		byte[] result = null;

		// on construit le jasper print
		final JasperPrint jasperPrint = makeDetailedJasperPrint(data, inst,
				app, acc, startDate, endDate);

		if (format.equals(FormatReport.XLS)) {
			// on l'exporte en Excel
			final ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			final JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRXlsAbstractExporterParameter.JASPER_PRINT,
					jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporter.exportReport();

			result = xlsReport.toByteArray();

		} else if (format.equals(FormatReport.PDF)) {
			// on exporte en pdf
			result = JasperExportManager.exportReportToPdf(jasperPrint);
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
			final Date startDate, final Date endDate) {

		final List<UIDetailedSummary> result = new ArrayList<UIDetailedSummary>();

		// rappel : les relev�s d�taill�s sont group�s par SMS_INITIAL_ID
		// les statistiques concernent ce groupe d'envoi.

		// on commence par r�cup�rer tous les groupes d'envois correspondant
		// aux crit�res
		final List<Integer> groupSms = searchGroupSms(inst, acc, app,
				startDate, endDate);

		// pour chaque groupe d'envoi, on construit le relev� d�taill�
		for (final Integer smsInitialId : groupSms) {
			final List<Sms> smsList = daoService
					.getSmsByInitialId(smsInitialId);

			final UIDetailedSummary summary = new UIDetailedSummary();

			// on r�cup�re les objets institution, application et account
			// identique pour tous les sms d'un m�me groupe
			assert !smsList.isEmpty();
			final Sms firstSms = smsList.get(0);
			final UIInstitution uiInst = dtoConverterService
					.convertToUI(firstSms.getApp().getInstitution());
			final UIApplication uiApp = dtoConverterService
					.convertToUI(firstSms.getApp());
			final UIAccount uiAcc = dtoConverterService.convertToUI(firstSms
					.getAcc());
			summary.setInstitution(uiInst);
			summary.setApplication(uiApp);
			summary.setAccount(uiAcc);

			Date minDate = null;

			int cptCREATED = 0;
			int cptINPROGRESS = 0;
			int cptDELIVERED = 0;
			int cptERRORQUOTA = 0;
			int cptERRORPREBL = 0;
			int cptERRORPOSTBL = 0;
			int cptERROR = 0;
			// on parcourt les sms
			for (final Sms sms : smsList) {
				final UISms uiSms = dtoConverterService.convertToUI(sms);
				// gestion de la date
				final Date smsDate = uiSms.getDate();
				if (minDate == null) {
					minDate = smsDate;
				} else {
					if (smsDate.before(minDate)) {
						minDate = smsDate;
					}
				}
				// gestion du statut
				final SmsStatus status = uiSms.getState();
				if (status.equals(SmsStatus.CREATED)) {
					cptCREATED++;
				} else if (status.equals(SmsStatus.DELIVERED)) {
					cptDELIVERED++;
				} else if (status.equals(SmsStatus.ERROR)) {
					cptERROR++;
				} else if (status.equals(SmsStatus.ERROR_POST_BL)) {
					cptERRORPOSTBL++;
				} else if (status.equals(SmsStatus.ERROR_PRE_BL)) {
					cptERRORPREBL++;
				} else if (status.equals(SmsStatus.ERROR_QUOTA)) {
					cptERRORQUOTA++;
				} else if (status.equals(SmsStatus.IN_PROGRESS)) {
					cptINPROGRESS++;
				}
			}
			// la date
			summary.setDate(minDate);
			// le d�compte
			final Map<SmsStatus, Integer> stats = new HashMap<SmsStatus, Integer>();
			stats.put(SmsStatus.CREATED, cptCREATED);
			stats.put(SmsStatus.DELIVERED, cptDELIVERED);
			stats.put(SmsStatus.ERROR, cptERROR);
			stats.put(SmsStatus.ERROR_POST_BL, cptERRORPOSTBL);
			stats.put(SmsStatus.ERROR_PRE_BL, cptERRORPREBL);
			stats.put(SmsStatus.ERROR_QUOTA, cptERRORQUOTA);
			stats.put(SmsStatus.IN_PROGRESS, cptINPROGRESS);
			summary.setStatistics(stats);

			result.add(summary);
		}
		return result;
	}

	/**
	 * Returns identifiers of all sms' group matching criterias.
	 * 
	 * @param inst
	 * @param acc
	 * @param app
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Integer> searchGroupSms(final Institution inst,
			final Account acc, final Application app, final Date startDate,
			final Date endDate) {
		return daoService.searchGroupSms(inst, acc, app, startDate, endDate);
	}

}
