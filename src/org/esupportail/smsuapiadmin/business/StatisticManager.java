package org.esupportail.smsuapiadmin.business;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

		// on recupere le fichier .jasper (deja compile)
		final InputStream jasperFile = this.getClass().getResourceAsStream(
				"/properties/jasper/consolidated_report.jasper");
		// on recupere le logo pour l'ajouter au report
		final InputStream logoStream = this.getClass().getResourceAsStream(
				"/properties/jasper/Logo_Univ_Paris.gif");
		final Image logoImage = ImageIO.read(logoStream);

		// Les parametres
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
		// Gestion des criteres
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

		// la source de donnees
		final ConsolidatedSummaryDataSource dataSource = new ConsolidatedSummaryDataSource(
				data);

		// on remplit le report avec les parametres et la source de donnees
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

		// on recupere le fichier .jasper (deja compile)
		final InputStream jasperFile = this.getClass().getResourceAsStream(
				"/properties/jasper/detailed_report.jasper");
		// on recupere le logo pour l'ajouter au report
		final InputStream logoStream = this.getClass().getResourceAsStream(
				"/properties/jasper/Logo_Univ_Paris.gif");
		final Image logoImage = ImageIO.read(logoStream);

		// Les parametres
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
		final String smsStatusInProgress = getI18nService().getString(
				"SMS.STATUS.IN_PROGRESS.NAME");
		final String smsStatusDelivered = getI18nService().getString(
				"SMS.STATUS.DELIVERED.NAME");
		final String smsStatusErrors = getI18nService().getString(
				"SMS.STATUS.ERROR.NAME");

		final String criterias = getI18nService()
				.getString("SUMMARY.CRITERIAS");

		// Gestion des criteres
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

		parameters.put("SMS_STATUS_IN_PROGRESS", smsStatusInProgress);
		parameters.put("SMS_STATUS_DELIVERED", smsStatusDelivered);
		parameters.put("SMS_STATUS_ERRORS", smsStatusErrors);

		parameters.put("CRITERIAS", criterias);
		parameters.put("INSTITUTION_CRITERIA", instCriteria);
		parameters.put("APPLICATION_CRITERIA", appCriteria);
		parameters.put("ACCOUNT_CRITERIA", accCriteria);
		parameters.put("STARTDATE_CRITERIA", startDateCriteria);
		parameters.put("ENDDATE_CRITERIA", endDateCriteria);

		// la source de donnees
		final DetailedSummaryDataSource dataSource = new DetailedSummaryDataSource(
				data);

		// on remplit le report avec les parametres et la source de donnees
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
			result = makeConsolidatedReportXlsFile(data);

		} else if (format.equals(FormatReport.PDF)) {
			// on exporte en pdf
			result = JasperExportManager.exportReportToPdf(jasperPrint);
		}

		return result;
	}
	
	/**
	 * Makes an Excel file that presents consolidated report.
	 * @param data 
	 * 
	 * @return
	 */
	private byte[] makeConsolidatedReportXlsFile(final List<UIStatistic> statistics) {
		byte[] result = null;


		// on construit le document
		HSSFWorkbook workbook = new HSSFWorkbook();

		// le style du header
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);

		// on construit la feuille
		String nameSheet = getI18nService().getString("ACCOUNT.XLSFILE.SHEET");
		HSSFSheet sheet = workbook.createSheet(nameSheet);

		// on recupere les titres des colonnes
		String columnInstitution = getI18nService().getString("INSTITUTION.NAME");
		String columnApplication = getI18nService().getString("APPLICATION.NAME");
		String columnAccount = getI18nService().getString("ACCOUNT.NAME");
		String columnMonth = getI18nService().getString("MONTH.NAME");
		String columnSendedSms = getI18nService().getString("STATISTIC.SENDEDSMS");
		String columnReceivedSms = getI18nService().getString("STATISTIC.RECEIVEDSMS");
		String columnFailRate = getI18nService().getString("STATISTIC.FAILRATE");
		
		// on fait le row header
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell cellInstitutionHeader = headerRow.createCell(0);
		cellInstitutionHeader.setCellStyle(style);
		cellInstitutionHeader.setCellValue(new HSSFRichTextString(columnInstitution));
		HSSFCell cellApplicationHeader = headerRow.createCell(1);
		cellApplicationHeader.setCellStyle(style);
		cellApplicationHeader.setCellValue(new HSSFRichTextString(columnApplication));
		HSSFCell cellAccountHeader = headerRow.createCell(2);
		cellAccountHeader.setCellStyle(style);
		cellAccountHeader.setCellValue(new HSSFRichTextString(columnAccount));
		HSSFCell cellMonthHeader = headerRow.createCell(3);
		cellMonthHeader.setCellStyle(style);
		cellMonthHeader.setCellValue(new HSSFRichTextString(columnMonth));
		HSSFCell cellSendedSmsHeader = headerRow.createCell(4);
		cellSendedSmsHeader.setCellStyle(style);
		cellSendedSmsHeader.setCellValue(new HSSFRichTextString(columnSendedSms));
		HSSFCell cellReceivedSmsHeader = headerRow.createCell(5);
		cellReceivedSmsHeader.setCellStyle(style);
		cellReceivedSmsHeader.setCellValue(new HSSFRichTextString(columnReceivedSms));
		HSSFCell cellFailRateHeader = headerRow.createCell(6);
		cellFailRateHeader.setCellStyle(style);
		cellFailRateHeader.setCellValue(new HSSFRichTextString(columnFailRate));

		HSSFRow valueRow = null;
		int i = 1;
		for (UIStatistic stat : statistics) {
			// on recupere les valeurs du compte
			UIApplication uiApplication = stat.getApplication();
			String institution = uiApplication.getInstitution().getName();
			String application = uiApplication.getName();
			String account = stat.getAccount().getName();
			String month = stat.getFormattedMonth();
			String nbSendedSMS = stat.getNbSendedSMS();
			String nbReceivedSMS = stat.getNbReceivedSMS();
			String failRate = stat.getFailRate();

			// on cree une nouvelle ligne de valeurs
			valueRow = sheet.createRow(i);
			int j = 0;
			HSSFCell cellInstitutionValue = valueRow.createCell(j);
			cellInstitutionValue.setCellValue(new HSSFRichTextString(institution));
			j++;
			HSSFCell cellApplicationValue = valueRow.createCell(j);
			cellApplicationValue.setCellValue(new HSSFRichTextString(application));
			j++;
			HSSFCell cellAccountValue = valueRow.createCell(j);
			cellAccountValue.setCellValue(new HSSFRichTextString(account));
			j++;
			HSSFCell cellMonthValue = valueRow.createCell(j);
			cellMonthValue.setCellValue(new HSSFRichTextString(month));
			j++;
			HSSFCell cellSendedSmsValue = valueRow.createCell(j);
			cellSendedSmsValue.setCellValue(new HSSFRichTextString(nbSendedSMS));
			j++;
			HSSFCell cellReceivedSmsValue = valueRow.createCell(j);
			cellReceivedSmsValue.setCellValue(new HSSFRichTextString(nbReceivedSMS));
			j++;
			HSSFCell cellFailRateValue = valueRow.createCell(j);
			cellFailRateValue.setCellValue(new HSSFRichTextString(failRate));

			// on passe a la ligne suivante
			i++;
		}

		// On ouvre un flux d'ecriture sur le fichier resultat
		try {
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			workbook.write(fos);
			fos.flush();
			fos.close();
			result = fos.toByteArray();
		} catch (FileNotFoundException e) {
			logger
					.error(
							"Impossible d'ouvrir un flux en ecriture sur le fichier excel",
							e);
		} catch (IOException e) {
			logger.error("Impossible d'ecrire dans le fichier excel", e);
		}

		return result;
	}
	
	
	/**
	 * Makes an Excel file that presents informations about detailed summaries.
	 * @param data 
	 * 
	 * @return
	 */
	private byte[] makeDetailedReportXlsFile(final List<UIDetailedSummary> data) {
		byte[] result = null;


		// on construit le document
		HSSFWorkbook workbook = new HSSFWorkbook();

		// le style du header
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);

		// on construit la feuille
		String nameSheet = getI18nService().getString("ACCOUNT.XLSFILE.SHEET");
		HSSFSheet sheet = workbook.createSheet(nameSheet);

		// on recupere les titres des colonnes
		final String columnSending = getI18nService().getString("SUMMARY.SENDING");
		final String columnInstitution = getI18nService().getString("INSTITUTION.NAME");
		final String columnApplication = getI18nService().getString("APPLICATION.NAME");
		final String columnAccount = getI18nService().getString("ACCOUNT.NAME");
		final String columnSMSCount = getI18nService().getString("SUMMARY.SMSCOUNT");
		final String columnSmsStatusInProgress = getI18nService().getString("SMS.STATUS.IN_PROGRESS.NAME");
		final String columnSmsStatusDelivered = getI18nService().getString("SMS.STATUS.DELIVERED.NAME");
		final String columnSmsStatusError = getI18nService().getString("SMS.STATUS.ERROR.NAME");
		
		
		// on fait le row header
		HSSFRow headerRow = sheet.createRow(0);
		
		int columnIndex = 0;
		HSSFCell cellSendingHeader = headerRow.createCell(columnIndex);
		cellSendingHeader.setCellStyle(style);
		cellSendingHeader.setCellValue(new HSSFRichTextString(columnSending));
		columnIndex++;
		HSSFCell cellInstitutionHeader = headerRow.createCell(columnIndex);
		cellInstitutionHeader.setCellStyle(style);
		cellInstitutionHeader.setCellValue(new HSSFRichTextString(columnInstitution));
		columnIndex++;
		HSSFCell cellApplicationHeader = headerRow.createCell(columnIndex);
		cellApplicationHeader.setCellStyle(style);
		cellApplicationHeader.setCellValue(new HSSFRichTextString(columnApplication));
		columnIndex++;
		HSSFCell cellAccountHeader = headerRow.createCell(columnIndex);
		cellAccountHeader.setCellStyle(style);
		cellAccountHeader.setCellValue(new HSSFRichTextString(columnAccount));
		columnIndex++;
		HSSFCell cellSmsCountHeader = headerRow.createCell(columnIndex);
		cellSmsCountHeader.setCellStyle(style);
		cellSmsCountHeader.setCellValue(new HSSFRichTextString(columnSMSCount));
		columnIndex++;
		HSSFCell cellSmsStatusInProgressHeader = headerRow.createCell(columnIndex);
		cellSmsStatusInProgressHeader.setCellStyle(style);
		cellSmsStatusInProgressHeader.setCellValue(new HSSFRichTextString(columnSmsStatusInProgress));
		columnIndex++;
		HSSFCell cellSmsStatusDeliveredHeader = headerRow.createCell(columnIndex);
		cellSmsStatusDeliveredHeader.setCellStyle(style);
		cellSmsStatusDeliveredHeader.setCellValue(new HSSFRichTextString(columnSmsStatusDelivered));
		columnIndex++;
		HSSFCell cellSmsStatusErrorHeader = headerRow.createCell(columnIndex);
		cellSmsStatusErrorHeader.setCellStyle(style);
		cellSmsStatusErrorHeader.setCellValue(new HSSFRichTextString(columnSmsStatusError));
		
		HSSFRow valueRow = null;
		int i = 1;
		for (UIDetailedSummary summarry : data) {
			// on recupere les valeurs du compte
			UIApplication uiApplication = summarry.getApplication();
			String date = summarry.getFormattedDate();
			String institution = uiApplication.getInstitution().getName();
			String application = uiApplication.getName();
			String account = summarry.getAccount().getName();
			Integer smsCount = summarry.getSMSCount();
			Integer nbInProgress = summarry.getNbInProgress();
			Integer nbDelivered = summarry.getNbDelivered();
			String errors = summarry.getErrors();
			
			// on cree une nouvelle ligne de valeurs
			valueRow = sheet.createRow(i);
			int j = 0;
			HSSFCell cellDateValue = valueRow.createCell(j);
			cellDateValue.setCellValue(new HSSFRichTextString(date));
			j++;
			HSSFCell cellInstitutionValue = valueRow.createCell(j);
			cellInstitutionValue.setCellValue(new HSSFRichTextString(institution));
			j++;
			HSSFCell cellApplicationValue = valueRow.createCell(j);
			cellApplicationValue.setCellValue(new HSSFRichTextString(application));
			j++;
			HSSFCell cellAccountValue = valueRow.createCell(j);
			cellAccountValue.setCellValue(new HSSFRichTextString(account));
			j++;
			HSSFCell cellSmsCountValue = valueRow.createCell(j);
			cellSmsCountValue.setCellValue(smsCount);
			j++;
			HSSFCell cellNbInProgressValue = valueRow.createCell(j);
			cellNbInProgressValue.setCellValue(nbInProgress);
			j++;
			HSSFCell cellNbDeliveredValue = valueRow.createCell(j);
			cellNbDeliveredValue.setCellValue(nbDelivered);
			j++;
			HSSFCell cellNbErrorValue = valueRow.createCell(j);
			cellNbErrorValue.setCellValue(new HSSFRichTextString(errors));
			j++;

			// on passe a la ligne suivante
			i++;
		}

		// On ouvre un flux d'ecriture sur le fichier resultat
		try {
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			workbook.write(fos);
			fos.flush();
			fos.close();
			result = fos.toByteArray();
		} catch (FileNotFoundException e) {
			logger
					.error(
							"Impossible d'ouvrir un flux en ecriture sur le fichier excel",
							e);
		} catch (IOException e) {
			logger.error("Impossible d'ecrire dans le fichier excel", e);
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
			
			result = makeDetailedReportXlsFile(data);

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
