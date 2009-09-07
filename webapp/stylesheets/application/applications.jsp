<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="AppliManagement"
	locale="#{sessionController.locale}"
	authorized="#{applicationsController.pageAuthorized}">
	<%@include file="../_navigation.jsp"%>

	<e:section value="#{msgs['APPLICATION.LIST.TITLE']}" />

	<e:form id="applicationsForm">
		<e:dataTable
			rendered="#{not empty applicationsController.paginator.visibleItems}"
			id="data" rowIndexVar="variable"
			value="#{applicationsController.paginator.visibleItems}"
			var="application" cellpadding="5" cellspacing="3" width="60%">

			<f:facet name="header">
				<h:panelGroup>
					<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
						width="100%">
						<h:panelGroup>
							<e:text value="#{msgs['APPLICATION.TEXT.TITLE']}">
								<f:param
									value="#{applicationsController.paginator.firstVisibleNumber + 1}" />
								<f:param
									value="#{applicationsController.paginator.lastVisibleNumber + 1}" />
								<f:param
									value="#{applicationsController.paginator.totalItemsCount}" />
							</e:text>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{applicationsController.paginator.lastPageNumber == 0}" />
						<h:panelGroup
							rendered="#{applicationsController.paginator.lastPageNumber != 0}">
							<h:panelGroup
								rendered="#{not applicationsController.paginator.firstPage}">
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.FIRST']}"
									action="#{applicationsController.paginator.gotoFirstPage}"
									image="/media/icons/control-stop-180.png" />
								<e:text value=" " />
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
									action="#{applicationsController.paginator.gotoPreviousPage}"
									image="/media/icons/control-180.png" />
							</h:panelGroup>
							<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
							<t:dataList value="#{applicationsController.paginator.nearPages}"
								var="page">
								<e:text value=" " />
								<e:italic value="#{page + 1}"
									rendered="#{page == applicationsController.paginator.currentPage}" />
								<h:commandLink value="#{page + 1}"
									rendered="#{page != applicationsController.paginator.currentPage}">
									<t:updateActionListener value="#{page}"
										property="#{applicationsController.paginator.currentPage}" />
								</h:commandLink>
								<e:text value=" " />
							</t:dataList>
							<h:panelGroup
								rendered="#{not applicationsController.paginator.lastPage}">
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.NEXT']}"
									action="#{applicationsController.paginator.gotoNextPage}"
									image="/media/icons/control.png" />
								<e:text value=" " />
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.LAST']}"
									action="#{applicationsController.paginator.gotoLastPage}"
									image="/media/icons/control-stop.png" />
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup>
							<e:text value="#{msgs['APPLICATION.TEXT.APPLICATIONS_BY_PAGE']}" />
							<e:selectOneMenu
								onchange="javascript:{simulateLinkClick('applicationsForm:data:changeButton');}"
								value="#{applicationsController.paginator.pageSize}">
								<f:selectItems
									value="#{applicationsController.paginator.pageSizeItems}" />
							</e:selectOneMenu>
							<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}"
								style="display:none;" id="changeButton"
								action="#{applicationsController.paginator.forceReload}" />
						</h:panelGroup>
					</h:panelGrid>

				</h:panelGroup>
			</f:facet>

			<t:column sortable="true" defaultSorted="true">
				<f:facet name="header">
					<e:text value="#{msgs['APPLICATION.NAME']}" />
				</f:facet>
				<e:text value="#{application.name}" />

			</t:column>

			<t:column width="20px;">
				<f:facet name="header">
					<e:text value="" />
				</f:facet>
				<e:commandButton id="modify" value="#{msgs['APPLICATION.MODIFY']}"
					action="#{applicationsController.modify}"
					image="/media/icons/pencil.png"
					title="#{msgs['APPLICATION.MODIFY']}">
					<t:updateActionListener value="#{application}"
						property="#{applicationsController.application}" />
				</e:commandButton>
			</t:column>

			<t:column width="20px;">
				<f:facet name="header">
					<e:text value="" />
				</f:facet>
				<e:commandButton id="delete" value="#{msgs['APPLICATION.DELETE']}"
					action="#{applicationsController.delete}"
					image="/media/icons/minus-circle-frame.png"
					title="#{msgs['APPLICATION.DELETE']}"
					rendered="#{application.deletable}">
					<t:updateActionListener value="#{application}"
						property="#{applicationsController.application}" />
				</e:commandButton>
			</t:column>

		</e:dataTable>

		<f:verbatim>
			<br />
			<br />
		</f:verbatim>
		<e:paragraph value="#{msgs['APPLICATION.ERROR']}"
			rendered="#{empty applicationsController.paginator.visibleItems}"
			style="color:#f00;" />
	</e:form>

	<e:form>
		<e:commandButton id="createPage" value="#{msgs['APPLICATION.CREATE']}"
			action="#{applicationsController.create}" />
	</e:form>

</e:page>
