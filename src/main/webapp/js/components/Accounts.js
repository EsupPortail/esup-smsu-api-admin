export const template = `
<style>
.ngHeaderText {
  white-space: normal;
}
.gridStyle.loading {
  opacity: 0.5;
}
.highConsumedRatio {
  color: #f00;
}
</style>

<div class="gridStyle" ng-grid="gridOptions" ng-class="{loading: !accounts}"></div>
`
export default { template, controller: function($scope, h, h_accounts) {
    $scope.accounts = h_accounts;
    $scope.warnConsumedRatio = 0.9;
    $scope.consumedRatio = function (account) {
	return account.consumedSms / account.quota;
    };
    $scope.gridOptions = { data: 'accounts',
			   sortInfo: {fields: ['name'], directions: ['asc', 'desc']},
			   headerRowHeight: '50',
			   multiSelect: false,
			   columnDefs: [{field: 'name', displayName:"Compte d'imputation", width: '****', 
					   cellTemplate: '<div class="ngCellText"><a href="#/accounts/{{row.entity.id}}">{{row.getProperty(col.field)}}</a></div>'},
					{field: 'quota', displayName: 'Quota', width: '**'},
					{field: 'consumedSms', displayName: 'Nombre de SMS consommés', width: '**',
					 cellTemplate: '<div ng-class="{highConsumedRatio: consumedRatio(row.entity) > warnConsumedRatio}"><div class="ngCellText">{{row.entity.consumedSms}}</div></div>'
					}]
			 };

  }
}
