<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="accounts"
	locale="#{sessionController.locale}"
	authorized="#{accountsController.pageAuthorized}"
	downloadId="#{accountsController.downloadId}">
	<%@include file="../_navigation.jsp"%>

	<e:section value="#{msgs['ACCOUNT.MANAGEMENT.TITLE']}" />

	<h:panelGrid columns="1">
		<e:form id="accountsForm">
			<e:dataTable
				rendered="#{not empty accountsController.paginator.visibleItems}"
				id="data" rowIndexVar="variable"
				value="#{accountsController.paginator.visibleItems}" var="account"
				cellpadding="5" cellspacing="3">

				<f:facet name="header">
					<h:panelGroup>
						<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
							width="100%">
							<h:panelGroup>
								<e:text value="#{msgs['ACCOUNT.TEXT.TITLE']}">
									<f:param
										value="#{accountsController.paginator.firstVisibleNumber + 1}" />
									<f:param
										value="#{accountsController.paginator.lastVisibleNumber + 1}" />
									<f:param
										value="#{accountsController.paginator.totalItemsCount}" />
								</e:text>
							</h:panelGroup>
							<h:panelGroup
								rendered="#{accountsController.paginator.lastPageNumber == 0}" />
							<h:panelGroup
								rendered="#{accountsController.paginator.lastPageNumber != 0}">
								<h:panelGroup
									rendered="#{not accountsController.paginator.firstPage}">
									<e:commandButton value="#{msgs['PAGINATION.BUTTON.FIRST']}"
										action="#{accountsController.paginator.gotoFirstPage}"
										image="/media/icons/control-stop-180.png" />
									<e:text value=" " />
									<e:commandButton value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
										action="#{accountsController.paginator.gotoPreviousPage}"
										image="/media/icons/control-180.png" />
								</h:panelGroup>
								<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
								<t:dataList value="#{accountsController.paginator.nearPages}"
									var="page">
									<e:text value=" " />
									<e:italic value="#{page + 1}"
										rendered="#{page == accountsController.paginator.currentPage}" />
									<h:commandLink value="#{page + 1}"
										rendered="#{page != accountsController.paginator.currentPage}">
										<t:updateActionListener value="#{page}"
											property="#{accountsController.paginator.currentPage}" />
									</h:commandLink>
									<e:text value=" " />
								</t:dataList>
								<h:panelGroup
									rendered="#{not accountsController.paginator.lastPage}">
									<e:commandButton value="#{msgs['PAGINATION.BUTTON.NEXT']}"
										action="#{accountsController.paginator.gotoNextPage}"
										image="/media/icons/control.png" />
									<e:text value=" " />
									<e:commandButton value="#{msgs['PAGINATION.BUTTON.LAST']}"
										action="#{accountsController.paginator.gotoLastPage}"
										image="/media/icons/control-stop.png" />
								</h:panelGroup>
							</h:panelGroup>
							<h:panelGroup>
								<e:text value="#{msgs['ACCOUNT.TEXT.ACCOUNTS_BY_PAGE']}" />
								<e:selectOneMenu
									onchange="javascript:{simulateLinkClick('accountsForm:data:changeButton');}"
									value="#{accountsController.paginator.pageSize}">
									<f:selectItems
										value="#{accountsController.paginator.pageSizeItems}" />
								</e:selectOneMenu>
								<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}"
									style="display:none;" id="changeButton"
									action="#{accountsController.paginator.forceReload}" />
							</h:panelGroup>
						</h:panelGrid>
					</h:panelGroup>
				</f:facet>

				<t:column sortable="true" defaultSorted="true">
					<f:facet name="header">
						<e:text value="#{msgs['ACCOUNT.NAME']}" />
					</f:facet>
					<e:text value="#{account.name}" />
				</t:column>

				<t:column sortable="true" defaultSorted="true">
					<f:facet name="header">
						<e:text value="#{msgs['ACCOUNT.QUOTA']}" />
					</f:facet>
					<e:text value="#{account.quota}" />
				</t:column>

				<t:column sortable="true" defaultSorted="true">
					<f:facet name="header">
						<e:text value="#{msgs['ACCOUNT.CONS_SMS']}" />
					</f:facet>
					<e:text value="#{account.consumedSms}" />
				</t:column>

			</e:dataTable>
		</e:form>

		<h:form id="export" enctype="multipart/form-data">
			<h:panelGrid columns="3" cellspacing="2">
				<h:outputText value="#{msgs['ACCOUNT.EXPORT.NAME']}" />
				<h:panelGrid columns="2" cellspacing="1">
					<e:commandButton value="#{msgs['ACCOUNT.EXPORT.BUTTON']}"
						action="#{accountsController.downloadXLSReport}"
						image="/media/icons/icon_xls.gif" />
					<h:commandLink value="#{msgs['ACCOUNT.EXPORT.XLS']}"
						action="#{accountsController.downloadXLSReport}" />
				</h:panelGrid>
				<h:panelGrid columns="2" cellspacing="1">
					<e:commandButton value="PDF"
						action="#{accountsController.downloadPDFReport}"
						image="/media/icons/icon_pdf.gif" />
					<h:commandLink value="#{msgs['ACCOUNT.EXPORT.PDF']}"
						action="#{accountsController.downloadPDFReport}" />
				</h:panelGrid>
			</h:panelGrid>
		</h:form>
	</h:panelGrid>

	<e:subSection value="#{msgs['ACCOUNT.IMPORT.NAME']}" />

	<h:form id="import" enctype="multipart/form-data">
		<e:panelGrid columns="2">
			<e:outputLabel value="#{msgs['ACCOUNT.IMPORT.XLSFILE']}" />
			<h:panelGroup>
				<h:panelGrid columns="1">
					<h:panelGroup>
						<t:inputFileUpload id="importFile"
							value="#{accountsController.xlsFile}" storage="memory"
							required="true">
						</t:inputFileUpload>
						<e:message for="importFile" />
					</h:panelGroup>
				</h:panelGrid>
			</h:panelGroup>
		</e:panelGrid>
		<e:commandButton value="#{msgs['ACCOUNT.IMPORT.BUTTON']}"
			action="#{accountsController.importFile}" />
	</h:form>

</e:page>