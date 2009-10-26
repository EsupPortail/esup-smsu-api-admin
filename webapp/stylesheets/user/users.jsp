<%@include file="../_include.jsp"%>
<e:page stringsVar="msgs" menuItem="UserManagement"
	locale="#{sessionController.locale}"
	authorized="#{usersController.pageAuthorized}">
	<%@include file="../_navigation.jsp"%>

	<e:section value="#{msgs['USER.LIST.TITLE']}" />

	<e:form id="usersForm">
		<e:dataTable
			rendered="#{not empty usersController.paginator.visibleItems}"
			id="data" rowIndexVar="variable"
			value="#{usersController.paginator.visibleItems}"
			var="user" cellpadding="5" cellspacing="3" width="100%">

			<f:facet name="header">
				<h:panelGroup>
					<h:panelGrid columns="3" columnClasses="colLeft,,colRight"
						width="100%">
						<h:panelGroup>
							<e:text value="#{msgs['USER.TEXT.TITLE']}">
								<f:param
									value="#{usersController.paginator.firstVisibleNumber + 1}" />
								<f:param
									value="#{usersController.paginator.lastVisibleNumber + 1}" />
								<f:param
									value="#{usersController.paginator.totalItemsCount}" />
							</e:text>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{usersController.paginator.lastPageNumber == 0}" />
						<h:panelGroup
							rendered="#{usersController.paginator.lastPageNumber != 0}">
							<h:panelGroup
								rendered="#{not usersController.paginator.firstPage}">
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.FIRST']}"
									action="#{usersController.paginator.gotoFirstPage}"
									image="/media/icons/control-stop-180.png" />
								<e:text value=" " />
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.PREVIOUS']}"
									action="#{usersController.paginator.gotoPreviousPage}"
									image="/media/icons/control-180.png" />
							</h:panelGroup>
							<e:text value=" #{msgs['PAGINATION.TEXT.PAGES']} " />
							<t:dataList value="#{usersController.paginator.nearPages}"
								var="page">
								<e:text value=" " />
								<e:italic value="#{page + 1}"
									rendered="#{page == usersController.paginator.currentPage}" />
								<h:commandLink value="#{page + 1}"
									rendered="#{page != usersController.paginator.currentPage}">
									<t:updateActionListener value="#{page}"
										property="#{usersController.paginator.currentPage}" />
								</h:commandLink>
								<e:text value=" " />
							</t:dataList>
							<h:panelGroup
								rendered="#{not usersController.paginator.lastPage}">
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.NEXT']}"
									action="#{usersController.paginator.gotoNextPage}"
									image="/media/icons/control.png" />
								<e:text value=" " />
								<e:commandButton value="#{msgs['PAGINATION.BUTTON.LAST']}"
									action="#{usersController.paginator.gotoLastPage}"
									image="/media/icons/control-stop.png" />
							</h:panelGroup>
						</h:panelGroup>
						<h:panelGroup>
							<e:text value="#{msgs['USER.TEXT.USERS_BY_PAGE']}" />
							<e:selectOneMenu
								onchange="javascript:{simulateLinkClick('usersForm:data:changeButton');}"
								value="#{usersController.paginator.pageSize}">
								<f:selectItems
									value="#{usersController.paginator.pageSizeItems}" />
							</e:selectOneMenu>
							<e:commandButton value="#{msgs['_.BUTTON.CHANGE']}"
								style="display:none;" id="changeButton"
								action="#{usersController.paginator.forceReload}" />
						</h:panelGroup>
					</h:panelGrid>

				</h:panelGroup>
			</f:facet>

			<t:column sortable="true" defaultSorted="true">
				<f:facet name="header">
					<e:text value="#{msgs['USER.LOGIN.NAME']}" />
				</f:facet>
				<e:text value="#{user.login}" />
			</t:column>
			
			<t:column sortable="true" defaultSorted="true">
				<f:facet name="header">
					<e:text value="#{msgs['ROLE.NAME']}" />
				</f:facet>
				<e:text value="#{msgs[user.role.i18nKey]}" />
			</t:column>

			<t:column width="20px;">
				<f:facet name="header">
					<e:text value="" />
				</f:facet>
				<e:commandButton id="modify" value="#{msgs['USER.MODIFY']}"
					action="#{usersController.modify}"
					image="/media/icons/pencil.png"
					title="#{msgs['USER.MODIFY']}"
					rendered="#{currentUser.login!=user.login}" >
					<t:updateActionListener value="#{user}"
						property="#{usersController.user}" />
				</e:commandButton>
			</t:column>

			<t:column width="20px;">
				<f:facet name="header">
					<e:text value="" />
				</f:facet>
				<e:commandButton id="delete" value="#{msgs['USER.DELETE']}"
					action="#{usersController.delete}"
					image="/media/icons/minus-circle-frame.png"
					title="#{msgs['USER.DELETE']}"
					rendered="#{currentUser.login!=user.login}">
					<t:updateActionListener value="#{user}"
						property="#{usersController.user}" />
				</e:commandButton>
			</t:column>

		</e:dataTable>

		<f:verbatim>
			<br />
			<br />
		</f:verbatim>
		<e:paragraph value="#{msgs['USER.ERROR']}"
			rendered="#{empty usersController.paginator.visibleItems}"
			style="color:#f00;" />
	</e:form>

	<e:form>
		<e:commandButton id="createPage" value="#{msgs['USER.CREATE']}"
			action="#{usersController.create}" />
	</e:form>

</e:page>
