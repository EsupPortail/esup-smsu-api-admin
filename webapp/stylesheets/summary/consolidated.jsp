<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="consolidatedSummary"
	locale="#{sessionController.locale}"
	authorized="#{consolidatedSummaryController.pageAuthorized}"
	downloadId="#{consolidatedSummaryController.downloadId}">
	<%@include file="../_navigation.jsp"%>

	<e:section value="#{msgs['SUMMARY.CONSOLIDATED.TITLE']}" />

	<h:panelGrid columns="1">

		<e:form id="consolidatedForm">

			<h:panelGrid columns="4">

				<h:outputText value="#{msgs['SUMMARY.LABEL.INSTITUTION']}" />

				<h:outputText value="#{msgs['SUMMARY.LABEL.ACCOUNT']}" />

				<h:outputText value="#{msgs['SUMMARY.LABEL.APPLICATION']}" />

				<h:outputText value="#{msgs['SUMMARY.LABEL.MONTH']}" />

				<h:selectOneMenu id="institution"
					value="#{consolidatedSummaryController.institutionId}">
					<f:selectItems value="#{summaryCriteriasController.institutions}" />
				</h:selectOneMenu>


				<h:selectOneMenu id="account"
					value="#{consolidatedSummaryController.accountId}">
					<f:selectItems value="#{summaryCriteriasController.accounts}" />
				</h:selectOneMenu>

				<h:selectOneMenu id="application"
					value="#{consolidatedSummaryController.applicationId}">
					<f:selectItems value="#{summaryCriteriasController.applications}" />
				</h:selectOneMenu>

				<h:selectOneMenu id="month"
					value="#{consolidatedSummaryController.month}">
					<f:selectItems value="#{summaryCriteriasController.months}" />
				</h:selectOneMenu>

			</h:panelGrid>

			<e:commandButton value="#{msgs['SUMMARY.SEARCH']}"
				action="#{consolidatedSummaryController.search}" />

		</e:form>

		<h:panelGroup rendered="#{consolidatedSummaryController.searchDone}">
			<e:subSection value="#{msgs['SUMMARY.AVAILABLE']}" />

			<h:panelGroup rendered="#{consolidatedSummaryController.results}">
				<e:form id="summaryForm">
					<e:dataTable
						rendered="#{not empty consolidatedSummaryController.paginator.visibleItems}"
						id="data" rowIndexVar="variable"
						value="#{consolidatedSummaryController.paginator.visibleItems}"
						var="statistic" cellpadding="5" cellspacing="3" width="100%">

						<f:facet name="header">
							<h:panelGroup>
								<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
									width="100%">
									<h:panelGroup>
										<e:text value="#{msgs['SUMMARY.CONSOLIDATED.TEXT.TITLE']}">
											<f:param
												value="#{consolidatedSummaryController.paginator.firstVisibleNumber + 1}" />
											<f:param
												value="#{consolidatedSummaryController.paginator.lastVisibleNumber + 1}" />
											<f:param
												value="#{consolidatedSummaryController.paginator.totalItemsCount}" />
										</e:text>
									</h:panelGroup>
									<h:panelGroup
										rendered="#{consolidatedSummaryController.paginator.lastPageNumber == 0}" />
									<h:panelGroup
										rendered="#{consolidatedSummaryController.paginator.lastPageNumber != 0}">
										<h:panelGroup
											rendered="#{not consolidatedSummaryController.paginator.firstPage}">
											<e:commandButton value="#{msgs['PAGINATION.BUTTON.FIRST']}"
												action="#{consolidatedSummaryController.paginator.gotoFirstPage}"
												image="/media/icons/control-stop-180.png" />
											<e:text value=" " />
											<e:commandButton
												value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
												action="#{consolidatedSummaryController.paginator.gotoPreviousPage}"
												image="/media/icons/control-180.png" />
										</h:panelGroup>
										<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
										<t:dataList
											value="#{consolidatedSummaryController.paginator.nearPages}"
											var="page">
											<e:text value=" " />
											<e:italic value="#{page + 1}"
												rendered="#{page == consolidatedSummaryController.paginator.currentPage}" />
											<h:commandLink value="#{page + 1}"
												rendered="#{page != consolidatedSummaryController.paginator.currentPage}">
												<t:updateActionListener value="#{page}"
													property="#{consolidatedSummaryController.paginator.currentPage}" />
											</h:commandLink>
											<e:text value=" " />
										</t:dataList>
										<h:panelGroup
											rendered="#{not consolidatedSummaryController.paginator.lastPage}">
											<e:commandButton value="#{msgs['PAGINATION.BUTTON.NEXT']}"
												action="#{consolidatedSummaryController.paginator.gotoNextPage}"
												image="/media/icons/control.png" />
											<e:text value=" " />
											<e:commandButton value="#{msgs['PAGINATION.BUTTON.LAST']}"
												action="#{consolidatedSummaryController.paginator.gotoLastPage}"
												image="/media/icons/control-stop.png" />
										</h:panelGroup>
									</h:panelGroup>
									<h:panelGroup>
										<e:text value="#{msgs['SUMMARY.TEXT.SUMMARIES_BY_PAGE']}" />
										<e:selectOneMenu
											onchange="javascript:{simulateLinkClick('summaryForm:data:changeButton');}"
											value="#{consolidatedSummaryController.paginator.pageSize}">
											<f:selectItems
												value="#{consolidatedSummaryController.paginator.pageSizeItems}" />
										</e:selectOneMenu>
										<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}"
											style="display:none;" id="changeButton"
											action="#{consolidatedSummaryController.paginator.forceReload}" />
									</h:panelGroup>
								</h:panelGrid>
							</h:panelGroup>
						</f:facet>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['INSTITUTION.NAME']}" />
							</f:facet>
							<e:text value="#{statistic.application.institution.name}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['APPLICATION.NAME']}" />
							</f:facet>
							<e:text value="#{statistic.application.name}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['ACCOUNT.NAME']}" />
							</f:facet>
							<e:text value="#{statistic.account.name}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['MONTH.NAME']}" />
							</f:facet>
							<e:text value="#{statistic.formattedMonth}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['STATISTIC.SENDEDSMS']}" />
							</f:facet>
							<e:text value="#{statistic.nbSendedSMS}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['STATISTIC.RECEIVEDSMS']}" />
							</f:facet>
							<e:text value="#{statistic.nbReceivedSMS}" />
						</t:column>

						<t:column sortable="true" defaultSorted="true">
							<f:facet name="header">
								<e:text value="#{msgs['STATISTIC.FAILRATE']}" />
							</f:facet>
							<e:text value="#{statistic.failRate}" />
						</t:column>

					</e:dataTable>
				</e:form>

				<h:form id="export" enctype="multipart/form-data">
					<h:panelGrid columns="3" cellspacing="2">
						<h:outputText value="#{msgs['SUMMARY.EXPORT.NAME']}" />
						<h:panelGrid columns="2" cellspacing="1">
							<e:commandButton value="#{msgs['SUMMARY.EXPORT.BUTTON']}"
								action="#{consolidatedSummaryController.downloadXLSReport}"
								image="/media/icons/icon_xls.gif" />
							<h:commandLink value="#{msgs['SUMMARY.EXPORT.XLS']}"
								action="#{consolidatedSummaryController.downloadXLSReport}" />
						</h:panelGrid>
						<h:panelGrid columns="2" cellspacing="1">
							<e:commandButton value="PDF"
								action="#{consolidatedSummaryController.downloadPDFReport}"
								image="/media/icons/icon_pdf.gif" />
							<h:commandLink value="#{msgs['SUMMARY.EXPORT.PDF']}"
								action="#{consolidatedSummaryController.downloadPDFReport}" />
						</h:panelGrid>
					</h:panelGrid>
				</h:form>

			</h:panelGroup>
			<h:panelGroup rendered="#{not consolidatedSummaryController.results}">
				<h:outputText value="#{msgs['STATISTIC.NORESULTS']}" />
			</h:panelGroup>
		</h:panelGroup>

	</h:panelGrid>



</e:page>