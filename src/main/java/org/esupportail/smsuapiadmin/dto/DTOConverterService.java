package org.esupportail.smsuapiadmin.dto;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapi.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Fonction;
import org.esupportail.smsuapi.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapi.dao.beans.Sms;
import org.esupportail.smsuapi.dao.beans.Statistic;
import org.esupportail.smsuapi.dao.beans.StatisticPK;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UISms;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;
import org.esupportail.smsuapiadmin.business.NotFoundException;
import javax.inject.Inject;

public class DTOConverterService {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = Logger.getLogger(getClass());

	/**
	 * {@link DaoService}.
	 */
	@Inject private DaoService daoService;
	
	public UIAccount convertToUI(final Account acc) {
		UIAccount result = new UIAccount();

		result.id = acc.getId();
		result.name = acc.getLabel();
		result.quota = acc.getQuota();
		result.consumedSms = acc.getConsumedSms();

		return result;
	}

	
    public Account convertFromUI(final UIAccount acc, boolean isAddMode) {
		Account result = new Account();

		if (!isAddMode) {
			result.setId(Integer.valueOf(acc.id));
		}
		result.setLabel(acc.name.trim());
		result.setQuota(acc.quota);

		if (isAddMode) result.setConsumedSms(new Long(0));

		return result;
	}

	
	public UIApplication convertToUI(final Application app) {
		UIApplication result = new UIApplication();

		result.setId(app.getId());
		result.setName(app.getName());
		result.setPassword(app.getPassword());

		result.setAccountName(app.getAcc().getLabel());
		result.setInstitution(convertToUI(app.getIns()));
		result.setQuota(app.getQuota());
		result.setConsumedSms(app.getConsumedSms());
		result.setDeletable(isDeletable(app));

		return result;
	}

	
	public String convertToUI(final Institution inst) {
		return inst.getLabel();
	}

	/**
	 * Returns true the application is not referenced by other objects in
	 * database. False otherwise.
	 * 
	 * @param app
	 * @return
	 */
	private boolean isDeletable(final Application app) {
		boolean result = false;

		List<Sms> listSMS = daoService.getSMSByApplication(app);

		// aucun sms ne reference cette application
		if ((listSMS == null) || (listSMS.size() == 0)) {
			List<Statistic> listStatistic = daoService
					.getStatisticByApplication(app);
			// aucun sms ne reference cette application
			if ((listStatistic == null) || (listStatistic.size() == 0)) {
				result = true;
			}
		}

		return result;
	}

	
	public UIStatistic convertToUI(final Statistic stat) {
		UIStatistic result = new UIStatistic();

		StatisticPK id = stat.getId();
		result.setMonth(id.getMonth());

		result.setAccountName(id.getAcc().getLabel());
		result.setAppName(id.getApp().getName());
		result.setInstitution(id.getApp().getIns().getLabel());

		result.setNbSendedSMS(stat.getNbSms());
		result.setNbSMSInError(stat.getNbSmsInError());

		return result;
	}

	
	public UIUser convertToUI(final UserBoSmsu user) {
		UIUser result = new UIUser();

		result.id = user.getId() + "";
		result.login = user.getLogin();
		result.role = convertToEnum(user.getRole());

		return result;
	}

	
	public UIRole convertToUI(final Role role) {
		UIRole result = new UIRole();

		result.id = role.getId() + "";	
		result.role = EnumeratedRole.valueOf(role.getName());
		result.fonctions = convertToEnum(role.getFonctions());

		return result;
	}
	
	public EnumeratedRole convertToEnum(final Role role) {
		return EnumeratedRole.valueOf(role.getName());
	}

	
	public EnumeratedFunction convertToEnum(final Fonction fct) {
		return EnumeratedFunction.valueOf(fct.getName());
	}
	
	public Set<EnumeratedFunction> convertToEnum(final Set<Fonction> fcts) {
		Set<EnumeratedFunction> r = new java.util.TreeSet<>();
		for (Fonction fct : fcts) {
			r.add(convertToEnum(fct));
		}
		return r;
	}
	
	public Set<String> convert(final Set<Fonction> fcts) {
		Set<String> r = new java.util.TreeSet<>();
		for (Fonction fct : fcts) {
			r.add(fct.getName());
		}
		return r;
	}

    public Application convertFromUI(final UIApplication uiApp, boolean isAddMode) {
		final Application result = new Application();

		if (!isAddMode) {
			result.setId(Integer.valueOf(uiApp.getId()));
		}

		result.setName(uiApp.getName());
		result.setPassword(uiApp.getPassword());
		result.setQuota(uiApp.getQuota());

		// le compte d'imputation
		Account account = daoService.getAccountByName(uiApp.getAccountName());
		if (account == null) throw new NotFoundException("invalid account " + uiApp.getAccountName());
		result.setAcc(account);

		// l'etablissement
		result.setIns(getOrCreateInstitution(uiApp.getInstitution()));

		// le nombre de sms consomme est nul;
		result.setConsumedSms(new Long(0));

		return result;
	}

    private Institution getOrCreateInstitution(String institutionName) {
    
		Institution institution = daoService.getInstitutionByName(institutionName);
		if (institution == null) {
			institution = new Institution();
			institution.setLabel(institutionName);
			daoService.addInstitution(institution);
		}
        return institution;
    }
	
	public UISms convertToUI(final Sms sms) {
		UISms result = new UISms();
		result.id = sms.getId() + "";
		result.account = convertToUI(sms.getAcc());
		result.application = convertToUI(sms.getApp());
		result.date = sms.getDate();
		result.initialId = sms.getInitialId() + "";
		result.senderId = sms.getSenderId() + "";
		result.state = sms.getStateAsEnum();
		result.phone = sms.getPhone();

		return result;
	}

	
	public UserBoSmsu convertFromUI(final UIUser uiUser, boolean isAddMode) {
		final UserBoSmsu result = new UserBoSmsu();

		if (!isAddMode) {
			// l'id
			result.setId(Integer.valueOf(uiUser.id));
		}

		// le nom
		result.setLogin(uiUser.login);

		// le role
		Role role = daoService.getRoleByName(uiUser.role.toString());
		if (role == null) {
			logger.error("Aucun role d'identifiant " + uiUser.role + " n'existe en base.");
		}
		result.setRole(role);

		return result;
	}

}
