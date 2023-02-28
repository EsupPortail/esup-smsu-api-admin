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

<div class="normalContent">
   <a class="btn btn-default" href="#/applications/new"><span class="glyphicon glyphicon-plus"></span> Ajouter une application</a>
</div>

<p>

<div class="gridStyle" ng-grid="gridOptions" ng-class="{loading: !applications}"></div>
`

export default { template, controller: function($scope, h, h_applications) {
    $scope.applications = h_applications;
    $scope.warnConsumedRatio = 0.9;
    $scope.consumedRatio = function (account) {
	return account.consumedSms / account.quota;
    };
    $scope.gridOptions = { data: 'applications',
			   sortInfo: {fields: ['name'], directions: ['asc', 'desc']},
			   headerRowHeight: '50',
			   multiSelect: false,
			   columnDefs: [{field: 'name', displayName:"Application", width: '***', 
					   cellTemplate: '<div class="ngCellText"><a href="#/applications/{{row.entity.id}}">{{row.getProperty(col.field)}}</a></div>'},
					{field: 'quota', displayName: 'Quota', width: '*'},
					{field: 'consumedSms', displayName: 'Nombre de \nSMS consommés', width: '*',
					 cellTemplate: '<div ng-class="{highConsumedRatio: consumedRatio(row.entity) > warnConsumedRatio}"><div class="ngCellText">{{row.entity.consumedSms}}</div></div>'
					} ]
			 };

  }
}
