import * as h from "../basicHelpers.js"
import { getInstAppAccount } from "../helpers.js"

export const template = `
Filtre : <a href="" ng-click="showAccountFilters = !showAccountFilters">{{accountFilter.account || 'aucun'}}</a>

<div ng-if="showAccountFilters">
  <a class="btn btn-default" style="margin-top: 1em" ng-show="accountFilter.account" ng-click="setAppAccount({})"><span class="glyphicon glyphicon-remove"></span> Supprimer le filtre</a>

  <h4 style="margin-top: 2em">Choisissez un compte dans le tableau ci-dessous</h4>
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
</div>

<div style="margin-top: 1em" ng-show="groupedBy && !showAccountFilters">

<table class="table table-striped">
  <thead>
  <tr>
    <th>Date</th>
    <th>Nombre de SMS</th>
  </tr>
  </thead>
  <tbody ng-repeat="group in groupedBy">
    <tr>
      <td colspan="5"><b>{{group.institution}}</b> {{group.app}} {{group.account}}</td>
    </tr>
    <tr ng-repeat="e in group.list">
      <td>{{e.date | date:'short'}}</td>
      <td>{{e.nbSmsAndDetails}}</td>
    </tr>
  </tbody>
</table>

<!-- margin-bottom is needed: -->
<!-- * to make things more understable when the new results get displayed -->
<!-- * on mobile phones, the onscroll event can be buggy, this blank fixes it -->
<div style="margin-bottom: 8em" class="normalContent" when-scrolled="showMoreResults()" >
  <a href="" ng-hide="inProgress || noMoreResults" name="{{nbResults}}" ng-click="showMoreResults()">Voir plus</a>
  <div ng-show="inProgress">En cours...</div>
</div>

</div>

</div>
`

export default { template, controller: function($scope, restWsHelpers, $location, $route, h_summary_detailed_criteria) {
    $scope.initialNbResults = 50;
    $scope.nbResults = $scope.initialNbResults;
    $scope.accountFilter = $location.search();

    $scope.setAppAccount = function (e) {
	e = h.objectSlice(e, ['institution', 'app', 'account']); // all but hashKey
	$location.search(e);
	$route.reload();
    };

	var flatList = h_summary_detailed_criteria.map(function (e) {
	    return getInstAppAccount(e);
	});
	$scope.appAccountsTree = h.array2hashMulti(flatList, 'institution');

    $scope.inProgress = false;

    var nbSmsAndDetails = function (e) {
	var details = [];
	if (e.nbInProgress > 0) details.push(e.nbInProgress + " en cours");
	if (e.errors > 0) details.push(e.errors + " " + (e.errors > 1 ? "erreurs" : "erreur"));
	return e.nbSms + (details.length ? " dont " + details.join(" et ") : "");
    };

    var computeGroupedByRaw = function(flatList) {
	var groupedBy = [];
	var currentKey = null;
	var current;
	for (var i = 0; i < $scope.nbResults; i++) {
	    var e = flatList[i];
	    if (!e) break;
	    var key = e.appName + "+" + e.accountName;
	    if (currentKey !== key) {
		currentKey = key;
		current = $.extend({ list: [] }, getInstAppAccount(e));
		groupedBy.push(current);
	    }
	    current.list.push({ date: new Date(e.date), nbSmsAndDetails: nbSmsAndDetails(e) });
	}
	return groupedBy;
    };

    var computeGroupedBy = function() {
	if ($scope.inProgress) return;
	$scope.inProgress = true;
	var fullFilter = { maxResults: $scope.nbResults, ...$scope.accountFilter };
	restWsHelpers.simple('summary/detailed', fullFilter)
	    .then(function (flatList) {
		$scope.noMoreResults = flatList.length < fullFilter.maxResults;
		$scope.groupedBy = computeGroupedByRaw(flatList);
		$scope.inProgress = false;
	    }, function (resp) {
		// an error occured.
		if (resp && resp.status == 404 && $scope.accountFilter.account) {
		    // try removing accountFilter
		    $scope.setAppAccount({});
		}
	    });
    };

    $scope.showMoreResults = function () {
	if ($scope.noMoreResults) return;
	$scope.nbResults = $scope.nbResults + $scope.initialNbResults;
	computeGroupedBy();
    };

    computeGroupedBy();
  }
}