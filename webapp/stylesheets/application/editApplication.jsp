<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="editApplication"
	locale="#{sessionController.locale}"
	authorized="#{applicationsController.pageAuthorized}">
	<%@include file="../_navigation.jsp"%>

	<e:section value="#{msgs['APPLICATION.CREATE.TITLE']}"
		rendered="#{applicationsController.application.addMode}" />
	<e:section value="#{msgs['APPLICATION.MODIFY.TITLE']}"
		rendered="#{not applicationsController.application.addMode}" />

	<h:form id="editApplication" enctype="multipart/form-data">
		<e:panelGrid columns="2">

			<e:outputLabel value="#{msgs['APPLICATION.LABEL.NAME']}" />
			<h:panelGroup>
				<e:inputText id="name"
					value="#{applicationsController.application.name}" maxlength="30"
					validator="#{applicationsController.validateName}">
				</e:inputText>
				<e:message for="name" />
			</h:panelGroup>

			<e:outputLabel value="#{msgs['APPLICATION.LABEL.CERTIFICATE']}" />
			<h:panelGroup>
				<h:panelGrid columns="1">
					<h:panelGroup>
						<t:inputFileUpload id="certificate"
							value="#{applicationsController.application.certificateFile}"
							storage="memory">
						</t:inputFileUpload>
						<e:message for="certificate" />
					</h:panelGroup>
					<h:panelGroup
						rendered="#{applicationsController.application.certificateFile!=null or applicationsController.application.certificate!=null}">
						<t:graphicImage url="/media/icons/certificat[20x15].png" />
						<e:outputLabel value="#{msgs['APPLICATION.CERTIFICATEDEFINED']}" />
					</h:panelGroup>
				</h:panelGrid>
			</h:panelGroup>

			<e:outputLabel value="#{msgs['APPLICATION.LABEL.INSTITUTION']}" />
			<h:panelGroup>
				<e:inputText id="institution"
					value="#{applicationsController.application.institution.name}"
					maxlength="30"
					validator="#{applicationsController.validateInstitution}">
				</e:inputText>
				<e:message for="institution" />
			</h:panelGroup>

			<e:outputLabel value="#{msgs['APPLICATION.LABEL.ACCOUNT']}" />
			<h:panelGroup>
				<e:inputText id="account"
					value="#{applicationsController.application.account.name}"
					maxlength="30"
					validator="#{applicationsController.validateAccount}">
				</e:inputText>
				<e:message for="account" />
			</h:panelGroup>

			<e:outputLabel value="#{msgs['APPLICATION.LABEL.QUOTA']}" />
			<h:panelGroup>
				<e:inputText id="quota"
					value="#{applicationsController.application.quota}" maxlength="30"
					validator="#{applicationsController.validateQuota}">
				</e:inputText>
				<e:message for="quota" />
			</h:panelGroup>

		</e:panelGrid>

		<f:verbatim>
			<br />
			<br />
		</f:verbatim>
		<h:panelGrid columns="2">
			<e:commandButton value="#{msgs['APPLICATION.SAVE']}"
				action="#{applicationsController.save}" />
			<e:commandButton value="#{msgs['APPLICATION.PAGE.RETOUR.LISTE']}"
				action="applications" immediate="true" />
		</h:panelGrid>

	</h:form>

</e:page>