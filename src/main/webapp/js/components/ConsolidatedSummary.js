export const template = `
<div ng-show="appAccount">

  <dl style="margin-top: 2em" class="dl-horizontal">
    <dt>Etablissement</dt><dd>{{appAccount.institution}}</dd>
    <dt>Application</dt><dd>{{appAccount.app}}</dd>
    <dt>Compte d'imputation</dt><dd>{{appAccount.account}}</dd>
  </dl>

  <table class="table table-striped">
  <tr>
    <th>Mois</th>
    <th>Nombre de SMS envoyés</th>
    <th>Nombre de SMS reçus</th>
    <th>Taux d'échec</th>
  </tr>
  <tr ng-repeat="e in appAccount.data">
    <td>{{e.month}}</td>
    <td>{{e.nbSendedSMS}}</td>
    <td>{{e.nbReceived}}</td>
    <td>{{e.failureRate}}</td>
  </tr>
</table>

</div>


<table class="table table-bordered">
  <thead>
  <tr>
    <th>Etablissement</th>
    <th>Application</th>
    <th>Compte</th>
  </tr>
  </thead>
  <tbody ng-repeat="(institution, list) in appAccountsTree">
    <tr ng-repeat="e in list.slice(0,1)">
      <td rowspan="{{list.length}}">{{institution}}</td>
      <td>{{e.app}}</td>
      <td><a href="" ng-click="setAppAccount(e)">{{e.account}}</a></td>
    </tr>
    <tr ng-repeat="e in list.slice(1)">
      <td>{{e.app}}</td>
      <td><a href="" ng-click="setAppAccount(e)">{{e.account}}</a></td>
    </tr>
  </tbody>
</table>

<a href="" ng-click="exportCSV($event)">
  Export vers tableur
</a>
`

export default { template, controller: function($scope, h, h_summary_consolidated) {
    var computeTree = function () {
	var tree = {};
	for (const e of h_summary_consolidated) {
	    var key1 = e.institution;
	    var key2 = e.app + "+" + e.account;
	    if (!tree[key1]) 
		tree[key1] = {};
	    if (!tree[key1][key2]) 
		tree[key1][key2] = $.extend({ data: [] }, h.objectSlice(e, ['institution', 'app', 'account']));
	    tree[key1][key2].data.unshift(e);
	}
	h.simpleEach(tree, function (subtree, key1) {
	    tree[key1] = $.map(Object.keys(subtree).sort(), function (k) { return subtree[k]; });
	});
	return tree;
    };

    for (const e of h_summary_consolidated) {
	    $.extend(e, h.getInstAppAccount(e));
	    e.nbReceived = e.nbSendedSMS - e.nbSMSInError;
	    e.failureRate = Math.round(e.nbSMSInError / e.nbSendedSMS * 100) + "%";
    }
    $scope.flatList = h_summary_consolidated;
    $scope.appAccountsTree = computeTree();

    $scope.setAppAccount = function(e) {
	$scope.appAccount = e;
    };

    function toStringListList(list, attrs) {
	return list.map(function (o) {
	    return attrs.map(function (attr) { return ""+o[attr]; });
	});
    }
    $scope.exportCSV = function (event) {
	var rows = toStringListList($scope.flatList, ['institution', 'app', 'account', 'month', 'nbSendedSMS', 'nbReceived']);
	h.exportCSV(event.target.parentElement, rows, "smsuapi-consolidated.csv");
    };
  }
}