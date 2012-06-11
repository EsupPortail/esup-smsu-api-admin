<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="detailedSummary"
	locale="#{sessionController.locale}"
	authorized="#{detailedSummaryController.pageAuthorized}"
	downloadId="#{detailedSummaryController.downloadId}">
	<%@include file="../_navigation.jsp"%>

	<e:section value="#{msgs['SUMMARY.DETAILED.TITLE']}" />

	<h:panelGrid columns="1">

		<e:form id="detailedForm">

			<h:panelGrid columns="5">

				<h:outputText value="#{msgs['SUMMARY.LABEL.INSTITUTION']}" />

				<h:outputText value="#{msgs['SUMMARY.LABEL.ACCOUNT']}" />

				<h:outputText value="#{msgs['SUMMARY.LABEL.APPLICATION']}" />

				<h:outputText value="#{msgs['SUMMARY.LABEL.STARTDATE']}" />

				<h:outputText value="#{msgs['SUMMARY.LABEL.ENDDATE']}" />

				<h:selectOneMenu id="institution"
					value="#{detailedSummaryController.institutionId}">
					<f:selectItems value="#{summaryCriteriasController.institutions}" />
				</h:selectOneMenu>


				<h:selectOneMenu id="account"
					value="#{detailedSummaryController.accountId}">
					<f:selectItems value="#{summaryCriteriasController.accounts}" />
				</h:selectOneMenu>

				<h:selectOneMenu id="application"
					value="#{detailedSummaryController.applicationId}">
					<f:selectItems value="#{summaryCriteriasController.applications}" />
				</h:selectOneMenu>

				<h:inputText id="startDate" size="10"
					value="#{detailedSummaryController.startDate}">
					<f:convertDateTime locale="#{preferencesController.locale}"
						pattern="dd/MM/yyyy" timeZone="Europe/Paris" />
				</h:inputText>

				<h:inputText id="endDate" size="10"
					value="#{detailedSummaryController.endDate}">
					<f:convertDateTime locale="#{preferencesController.locale}"
						pattern="dd/MM/yyyy" timeZone="Europe/Paris" />
				</h:inputText>

			</h:panelGrid>
			<h:panelGrid columns="1">
				<h:panelGroup>
					<e:message for="startDate" />
					<e:message for="endDate" />
				</h:panelGroup>
				<e:commandButton value="#{msgs['SUMMARY.SEARCH']}"
					action="#{detailedSummaryController.search}" />
			</h:panelGrid>

		</e:form>

		<h:panelGroup rendered="#{detailedSummaryController.searchDone}">
			<e:subSection value="#{msgs['SUMMARY.AVAILABLE']}" />

			<h:panelGroup rendered="#{detailedSummaryController.results}">

				<e:messages globalOnly="true"/>

				<e:form id="summaryForm">
					<e:dataTable
						rendered="#{not empty detailedSummaryController.paginator.visibleItems}"
						id="data" rowIndexVar="variable"
						value="#{detailedSummaryController.paginator.visibleItems}"
						var="summary" cellpadding="5" cellspacing="3" width="100%">

						<f:facet name="header">
							<h:panelGroup>
								<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
									width="100%">
									<h:panelGroup>
										<e:text value="#{msgs['SUMMARY.DETAILED.TEXT.TITLE']}">
											<f:param
												value="#{detailedSummaryController.paginator.firstVisibleNumber + 1}" />
											<f:param
												value="#{detailedSummaryController.paginator.lastVisibleNumber + 1}" />
											<f:param
												value="#{detailedSummaryController.paginator.totalItemsCount}" />
										</e:text>
									</h:panelGroup>
									<h:panelGroup
										rendered="#{detailedSummaryController.paginator.lastPageNumber == 0}" />
									<h:panelGroup
										rendered="#{detailedSummaryController.paginator.lastPageNumber != 0}">
										<h:panelGroup
											rendered="#{not detailedSummaryController.paginator.firstPage}">
											<e:commandButton value="#{msgs['PAGINATION.BUTTON.FIRST']}"
												action="#{detailedSummaryController.paginator.gotoFirstPage}"
												image="/media/icons/control-stop-180.png" />
											<e:text value=" " />
											<e:commandButton
												value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
												action="#{detailedSummaryController.paginator.gotoPreviousPage}"
												image="/media/icons/control-180.png" />
										</h:panelGroup>
										<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
										<t:dataList
											value="#{detailedSummaryController.paginator.nearPages}"
											var="page">
											<e:text value=" " />
											<e:italic value="#{page + 1}"
												rendered="#{page == detailedSummaryController.paginator.currentPage}" />
											<h:commandLink value="#{page + 1}"
												rendered="#{page != detailedSummaryController.paginator.currentPage}">
												<t:updateActionListener value="#{page}"
													property="#{detailedSummaryController.paginator.currentPage}" />
											</h:commandLink>
											<e:text value=" " />
										</t:dataList>
										<h:panelGroup
											rendered="#{not detailedSummaryController.paginator.lastPage}">
											<e:commandButton value="#{msgs['PAGINATION.BUTTON.NEXT']}"
												action="#{detailedSummaryController.paginator.gotoNextPage}"
												image="/media/icons/control.png" />
											<e:text value=" " />
											<e:commandButton value="#{msgs['PAGINATION.BUTTON.LAST']}"
												action="#{detailedSummaryController.paginator.gotoLastPage}"
												image="/media/icons/control-stop.png" />
										</h:panelGroup>
									</h:panelGroup>
									<h:panelGroup>
										<e:text value="#{msgs['SUMMARY.TEXT.SUMMARIES_BY_PAGE']}" />
										<e:selectOneMenu
											onchange="javascript:{simulateLinkClick('summaryForm:data:changeButton');}"
											value="#{detailedSummaryController.paginator.pageSize}">
											<f:selectItems
												value="#{detailedSummaryController.paginator.pageSizeItems}" />
										</e:selectOneMenu>
										<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}"
											style="display:none;" id="changeButton"
											action="#{detailedSummaryController.paginator.forceReload}" />
									</h:panelGroup>
								</h:panelGrid>
							</h:panelGroup>
						</f:facet>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['INSTITUTION.NAME']}" />
							</f:facet>
							<e:text value="#{summary.application.institution.name}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['APPLICATION.NAME']}" />
							</f:facet>
							<e:text value="#{summary.application.name}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['ACCOUNT.NAME']}" />
							</f:facet>
							<e:text value="#{summary.account.name}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['DATE.NAME']}" />
							</f:facet>
							<e:text value="#{summary.formattedDate}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['SUMMARY.DETAILED.NUMBEROFSMS']}" />
							</f:facet>
							<e:text value="#{summary.SMSCount}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true"
							footertitle="#{msgs['SMS.STATUS.CREATED.DETAIL']}">
							<f:facet name="header">
								<e:text value="#{msgs['SMS.STATUS.CREATED.NAME']}" />
							</f:facet>
							<e:text value="#{summary.nbCreated}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true"
							footertitle="#{msgs['SMS.STATUS.DELIVERED.DETAIL']}">
							<f:facet name="header">
								<e:text value="#{msgs['SMS.STATUS.DELIVERED.NAME']}" />
							</f:facet>
							<e:text value="#{summary.nbDelivered}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true"
							footertitle="#{msgs['SMS.STATUS.ERROR.DETAIL']}">
							<f:facet name="header">
								<e:text value="#{msgs['SMS.STATUS.ERROR.NAME']}" />
							</f:facet>
							<e:text value="#{summary.nbError}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true"
							footertitle="#{msgs['SMS.STATUS.ERROR_PRE_BL.DETAIL']}">
							<f:facet name="header">
								<e:text value="#{msgs['SMS.STATUS.ERROR_PRE_BL.NAME']}" />
							</f:facet>
							<e:text value="#{summary.nbErrorPreBl}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true"
							footertitle="#{msgs['SMS.STATUS.ERROR_POST_BL.DETAIL']}">
							<f:facet name="header">
								<e:text value="#{msgs['SMS.STATUS.ERROR_POST_BL.NAME']}" />
							</f:facet>
							<e:text value="#{summary.nbErrorPostBl}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true"
							footertitle="#{msgs['SMS.STATUS.ERROR_QUOTA.DETAIL']}">
							<f:facet name="header">
								<e:text value="#{msgs['SMS.STATUS.ERROR_QUOTA.NAME']}" />
							</f:facet>
							<e:text value="#{summary.nbErrorQuota}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true"
							footertitle="#{msgs['SMS.STATUS.IN_PROGRESS.DETAIL']}">
							<f:facet name="header">
								<e:text value="#{msgs['SMS.STATUS.IN_PROGRESS.NAME']}" />
							</f:facet>
							<e:text value="#{summary.nbInProgress}" />
						</t:column>

					</e:dataTable>
				</e:form>

				<h:form id="export" enctype="multipart/form-data">
					<h:panelGrid columns="3" cellspacing="2">
						<h:outputText value="#{msgs['SUMMARY.EXPORT.NAME']}" />
						<h:panelGrid columns="2" cellspacing="1">
							<e:commandButton value="#{msgs['SUMMARY.EXPORT.BUTTON']}"
								action="#{detailedSummaryController.downloadXLSReport}"
								image="/media/icons/icon_xls.gif" />
							<h:commandLink value="#{msgs['SUMMARY.EXPORT.XLS']}"
								action="#{detailedSummaryController.downloadXLSReport}" />
						</h:panelGrid>
						<h:panelGrid columns="2" cellspacing="1">
							<e:commandButton value="PDF"
								action="#{detailedSummaryController.downloadPDFReport}"
								image="/media/icons/icon_pdf.gif" />
							<h:commandLink value="#{msgs['SUMMARY.EXPORT.PDF']}"
								action="#{detailedSummaryController.downloadPDFReport}" />
						</h:panelGrid>
					</h:panelGrid>
				</h:form>
			</h:panelGroup>
			<h:panelGroup rendered="#{not detailedSummaryController.results}">
				<h:outputText value="#{msgs['STATISTIC.NORESULTS']}" />
			</h:panelGroup>
		</h:panelGroup>

	</h:panelGrid>



</e:page>