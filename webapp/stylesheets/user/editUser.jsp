<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="editUser"
	locale="#{sessionController.locale}"
	authorized="#{usersController.pageAuthorized}">
	<%@include file="../_navigation.jsp"%>

	<e:section value="#{msgs['USER.CREATE.TITLE']}"
		rendered="#{usersController.user.addMode}" />
	<e:section value="#{msgs['USER.MODIFY.TITLE']}"
		rendered="#{not usersController.user.addMode}" />

	<h:form id="editUser">
		<e:panelGrid columns="2">

			<e:outputLabel value="#{msgs['USER.LOGIN.NAME']}" />
			<h:panelGroup>
				<e:inputText id="login" value="#{usersController.user.login}"
					maxlength="30" required="true"
					validator="#{usersController.validateLogin}">
				</e:inputText>
				<e:message for="login" />
			</h:panelGroup>

			<e:outputLabel value="#{msgs['ROLE.NAME']}" />
			<h:panelGroup>
				<h:selectOneMenu id="role" value="#{usersController.user.role.id}">
					<f:selectItems value="#{usersController.roles}" />
				</h:selectOneMenu>
				<e:message for="role" />
			</h:panelGroup>

		</e:panelGrid>

		<f:verbatim>
			<br />
			<br />
		</f:verbatim>
		<h:panelGrid columns="2">
			<e:commandButton value="#{msgs['USER.SAVE']}"
				action="#{usersController.save}" />
			<e:commandButton value="#{msgs['USER.PAGE.RETOUR.LISTE']}"
				action="users" immediate="true" />
		</h:panelGrid>

	</h:form>

</e:page>