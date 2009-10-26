<%@include file="_include.jsp"%>
<e:menuItem id="welcome" value="#{msgs['NAVIGATION.TEXT.WELCOME']}"
	action="#{welcomeController.enter}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.WELCOME']}" />
<e:menuItem id="applications" 
	value="#{msgs['APPLICATION.MANAGEMENT.TITLE']}"
    action="#{applicationsController.enter}"
    rendered="#{applicationsController.pageAuthorized}" />
<e:menuItem id="accounts" 
	value="#{msgs['ACCOUNT.MANAGEMENT.TITLE']}"
    action="#{accountsController.enter}"
    rendered="#{accountsController.pageAuthorized}" />
<e:menuItem id="consolidatedSummary" 
	value="#{msgs['SUMMARY.CONSOLIDATED.TITLE']}"
    action="#{consolidatedSummaryController.enter}"
    rendered="#{consolidatedSummaryController.pageAuthorized}" />
<e:menuItem id="detailedSummary" 
	value="#{msgs['SUMMARY.DETAILED.TITLE']}"
    action="#{detailedSummaryController.enter}"
    rendered="#{detailedSummaryController.pageAuthorized}" />
<e:menuItem id="user" 
	value="#{msgs['USER.MANAGEMENT.TITLE']}"
    action="#{usersController.enter}"
    rendered="#{usersController.pageAuthorized}" />
<e:menuItem id="login" action="casLogin"
	value="#{msgs['NAVIGATION.TEXT.LOGIN']}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.LOGIN']}"
	rendered="#{sessionController.printLogin}" />
<e:menuItem id="logout" action="#{sessionController.logout}"
	value="#{msgs['NAVIGATION.TEXT.LOGOUT']}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.LOGOUT']}"
	rendered="#{sessionController.printLogout}" />
<e:menuItem id="about" value="#{msgs['NAVIGATION.TEXT.ABOUT']}"
	accesskey="#{msgs['NAVIGATION.ACCESSKEY.ABOUT']}"
	action="#{aboutController.enter}" />