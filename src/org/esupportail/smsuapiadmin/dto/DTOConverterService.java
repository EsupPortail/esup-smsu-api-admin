package org.esupportail.smsuapiadmin.dto;

import org.esupportail.smsuapiadmin.dao.beans.Account;
import org.esupportail.smsuapiadmin.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Fonction;
import org.esupportail.smsuapiadmin.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapiadmin.dao.beans.Sms;
import org.esupportail.smsuapiadmin.dao.beans.Statistic;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIFonction;
import org.esupportail.smsuapiadmin.dto.beans.UIInstitution;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UISms;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * Interface 'DTOConverterService' purposes services to convert business objects
 * to UI objects and inversely.
 * 
 * @author MZRL3760
 * 
 */
public interface DTOConverterService {

	/**
	 * Makes an UIAccount from an Account.
	 * 
	 * @param acc
	 * @return
	 */
	UIAccount convertToUI(Account acc);

	/**
	 * Makes an UIApplication from an Application.
	 * 
	 * @param app
	 * @return
	 */
	UIApplication convertToUI(Application app);

	/**
	 * Makes an UIAccount from an Account.
	 * 
	 * @param acc
	 * @return
	 */
	UIStatistic convertToUI(Statistic stat);

	/**
	 * Makes an Application from an UIApplication.
	 * 
	 * @param uiApp
	 * @return
	 */
	Application convertFromUI(UIApplication uiApp);

	/**
	 * Makes an UIInstitution from an Institution.
	 * 
	 * @param inst
	 * @return
	 */
	UIInstitution convertToUI(Institution inst);

	/**
	 * Makes an UIUser from a UserBoSmsu.
	 * 
	 * @param user
	 * @return
	 */
	UIUser convertToUI(UserBoSmsu user);

	/**
	 * Makes an UIFonction from a Fonction.
	 * 
	 * @param user
	 * @return
	 */
	UIFonction convertToUI(Fonction fct);

	/**
	 * Makes an UIRole from a Role.
	 * 
	 * @param user
	 * @return
	 */
	UIRole convertToUI(Role role);

	/**
	 * Makes an UISms from a Sms.
	 * 
	 * @param user
	 * @return
	 */
	UISms convertToUI(Sms sms);

	/**
	 * Makes a UserBoSmsu from a UIUser.
	 * 
	 * @param uiUser
	 * @return
	 */
	UserBoSmsu convertFromUI(UIUser uiUser);

}
