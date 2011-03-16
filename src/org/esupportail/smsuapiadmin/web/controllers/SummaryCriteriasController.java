/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.faces.model.SelectItem;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIInstitution;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * accounts quota.
 * 
 * @author MZRL3760
 * 
 */
public class SummaryCriteriasController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -239570715531002003L;

	/**
	 * Log4j logger.
	 */
	@SuppressWarnings("unused")
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public SummaryCriteriasController() {
		super();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @see org.esupportail.smsuapiadmin.web.controllers.AbstractDomainAwareBean#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		enter();
	}

	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		return true;
	}

	/**
	 * JSF callback.
	 * 
	 * @return A navigation rule.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			addUnauthorizedActionMessage();
			return null;
		}
		init();
		return "accounts";
	}

	/**
	 * initialize the page.
	 */
	private void init() {
	}

	/**
	 * Returns a list for web containing all the institutions.
	 * 
	 * @return
	 */
	public List<SelectItem> getInstitutions() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		List<UIInstitution> institutions = getDomainService().getInstitutions();
		for (UIInstitution inst : institutions) {
			SelectItem item = new SelectItem(inst.getId(), inst.getName());
			result.add(item);
		}

		String all = getI18nService().getString("SELECT.ALL", getLocale());
		result.add(0, new SelectItem("-1", all));

		return result;
	}

	/**
	 * Returns a list for web containing all the accounts.
	 * 
	 * @return
	 */
	public List<SelectItem> getAccounts() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		List<UIAccount> accounts = getDomainService().getAccounts();
		for (UIAccount acc : accounts) {
			SelectItem item = new SelectItem(acc.getId(), acc.getName());
			result.add(item);
		}
		String all = getI18nService().getString("SELECT.ALL", getLocale());
		result.add(0, new SelectItem("-1", all));

		return result;
	}

	/**
	 * Returns a list for web containing all the applications.
	 * 
	 * @return
	 */
	public List<SelectItem> getApplications() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		List<UIApplication> apps = getDomainService().getApplications();
		for (UIApplication app : apps) {
			SelectItem item = new SelectItem(app.getId(), app.getName());
			result.add(item);
		}

		String all = getI18nService().getString("SELECT.ALL", getLocale());
		result.add(0, new SelectItem("-1", all));

		return result;
	}

	/**
	 * Returns a list for web containing all the months. The list is ordered
	 * (older -> most recent).
	 * 
	 * @return
	 */
	public List<SelectItem> getMonths() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		SortedSet<Date> months = getDomainService().getMonthsOfStatistics();
		for (Date month : months) {
			String pattern = "MMM yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, getLocale());
			String formattedDate = sdf.format(month);
			SelectItem item = new SelectItem(month.getTime() + "", formattedDate);
			result.add(item);
		}

		String all = getI18nService().getString("SELECT.ALL", getLocale());
		result.add(0, new SelectItem("-1", all));

		return result;
	}

}
