<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="editApplication"
	locale="#{sessionController.locale}"
	authorized="#{applicationsController.pageAuthorized}">
	<%@include file="../_navigation.jsp"%>
<script type="text/javascript">
	function selectAccountFromAvailable(value) {
		document.getElementById("editApplication:account").value = value;
	}
</script>
	<e:section value="#{msgs['APPLICATION.CREATE.TITLE']}"
		rendered="#{applicationsController.application.addMode}" />
	<e:section value="#{msgs['APPLICATION.MODIFY.TITLE']}"
		rendered="#{not applicationsController.application.addMode}" />

	<h:form id="editApplication" enctype="multipart/form-data">
		<e:panelGrid columns="4">

			<e:outputLabel value="#{msgs['APPLICATION.LABEL.NAME']}" for="name"/>
			<h:panelGroup>
				<e:inputText id="name"
					value="#{applicationsController.application.name}" maxlength="30"
					validator="#{applicationsController.validateName}">
				</e:inputText>
				<e:message for="name" />
			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
			</h:panelGroup>


			<e:selectOneMenu id="selectTypeRecipient" onchange="submit();"
		           value="#{applicationsController.isCertificate}">
			       <f:selectItems value="#{applicationsController.certificateOrPasswordOptions}" />
		        </e:selectOneMenu>

			<h:panelGroup>
				<h:panelGrid columns="1" rendered="#{applicationsController.isCertificate}">
					<h:panelGroup>
						<t:inputFileUpload id="certificate"
							value="#{applicationsController.application.certificateFile}"
							storage="memory">
						</t:inputFileUpload>
						<e:message for="certificate" />
					</h:panelGroup>
					<h:panelGroup
						rendered="#{applicationsController.application.certificateFile!=null or applicationsController.application.certificate!=null}">
						<t:graphicImage id="cer" url="/media/icons/certificat[20x15].png" />
						<e:outputLabel value="#{msgs['APPLICATION.CERTIFICATEDEFINED']}" for="cer"/>
					</h:panelGroup>
				</h:panelGrid>

			        <e:inputText id="password"
			        		 rendered="#{!applicationsController.isCertificate}"
			        		 value="#{applicationsController.application.password}" maxlength="30">
			        </e:inputText>
			        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
			        <e:message for="password" />

			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
			</h:panelGroup>

			<e:outputLabel value="#{msgs['APPLICATION.LABEL.INSTITUTION']}" for="institution"/>
			<h:panelGroup>
				<e:inputText id="institution"
					value="#{applicationsController.application.institution.name}"
					maxlength="30"
					validator="#{applicationsController.validateInstitution}">
				</e:inputText>
				<e:message for="institution" />
			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
			</h:panelGroup>

			<e:outputLabel for="account" value="#{msgs['APPLICATION.LABEL.ACCOUNT']}" />
			<h:panelGroup>
				<e:inputText id="account"
					value="#{applicationsController.application.account.name}"
					maxlength="30"
					validator="#{applicationsController.validateAccount}">
				</e:inputText>
				<e:message for="account" />
			</h:panelGroup>

			<e:outputLabel for="availableAccounts"
				value="#{msgs['APPLICATION.LABEL.AVAILABLEACCOUNTS']}"
				rendered="#{not empty applicationsController.availableAccounts}" />
			<h:selectOneListbox id="availableAccounts"
				rendered="#{not empty applicationsController.availableAccounts}"
				onclick="selectAccountFromAvailable(this.options[this.selectedIndex].text)">
				<f:selectItems value="#{applicationsController.availableAccounts}" />
			</h:selectOneListbox>


			<e:outputLabel for="quota" value="#{msgs['APPLICATION.LABEL.QUOTA']}" />
			<h:panelGroup>
				<e:inputText id="quota"
					value="#{applicationsController.application.quota}" maxlength="30"
					validator="#{applicationsController.validateQuota}">
				</e:inputText>
				<e:message for="quota" />
			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
			</h:panelGroup>
			<h:panelGroup>
				<f:verbatim> </f:verbatim>
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