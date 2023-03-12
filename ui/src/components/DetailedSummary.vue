<template>
Filtre : <a href="" @click.prevent="showAccountFilters = !showAccountFilters">{{accountFilter.account || 'aucun'}}</a>

<div v-if="showAccountFilters">
  <a class="btn btn-default" style="margin-top: 1em" v-if="accountFilter.account" @click.prevent="setAppAccount({})"><span class="glyphicon glyphicon-remove"></span> Supprimer le filtre</a>

  <h4 style="margin-top: 2em">Choisissez un compte dans le tableau ci-dessous</h4>
  <table class="table table-bordered">
    <thead>
    <tr>
      <th>Etablissement</th>
      <th>Application</th>
      <th>Compte</th>
    </tr>
    </thead>
    <tbody v-for="(list, institution) in appAccountsTree">
      <tr v-for="e in list.slice(0,1)">
        <td rowspan="{{list.length}}">{{institution}}</td>
        <td>{{e.app}}</td>
        <td><a href="" @click.prevent="setAppAccount(e)">{{e.account}}</a></td>
      </tr>
      <tr v-for="e in list.slice(1)">
        <td>{{e.app}}</td>
        <td><a href="" @click.prevent="setAppAccount(e)">{{e.account}}</a></td>
      </tr>
    </tbody>
  </table>
</div>
<div style="margin-top: 1em" v-if="!showAccountFilters">

<table class="table table-striped" v-if="groupedBy">
  <thead>
  <tr>
    <th>Date</th>
    <th>Nombre de SMS</th>
  </tr>
  </thead>
  <tbody v-for="group in groupedBy">
    <tr>
      <td colspan="5"><b>{{group.institution}}</b> {{group.app}} {{group.account}}</td>
    </tr>
    <tr v-for="e in group.list">
      <td>{{formatDate(e.date, 'dd/MM/yyyy')}}</td>
      <td>{{e.nbSmsAndDetails}}</td>
    </tr>
  </tbody>
</table>

<!-- margin-bottom is needed: -->
<!-- * to make things more understable when the new results get displayed -->
<div style="margin-bottom: 8em" class="normalContent">
  <a href="" v-show="groupedBy && !(inProgress || noMoreResults)" name="{{nbResults}}" v-whenVisible="showMoreResults" @click.prevent="showMoreResults()">Voir plus</a>
  <div v-if="inProgress">En cours...</div>
</div>
</div>
</template>

<script lang="ts">
import { computed, reactive, toRefs, watch } from 'vue'
import * as h from "../basicHelpers.js"
import * as restWsHelpers from '../restWsHelpers.js'
import router, { hash_params } from '../routes.js'
import { getInstAppAccount } from "../helpers.js"
import { whenVisible } from "../directives.js"

export default { directives: { whenVisible }, props: ['summary_detailed_criteria'], setup: function(props) {
    const initialNbResults = 50;
    const flatList = props.summary_detailed_criteria.map(getInstAppAccount);
    let $scope = reactive({ 
        showAccountFilters: false,
        groupedBy: undefined, 
        noMoreResults: undefined,
        formatDate: h.formatDate,
        nbResults: initialNbResults,
        accountFilter: computed(hash_params),
        appAccountsTree: h.array2hashMulti(flatList, 'institution'),
        inProgress: false,
    })

    const setAppAccount = function (e) {
	e = h.objectSlice(e, ['institution', 'app', 'account']); // all but hashKey
    $scope.showAccountFilters = false
    $scope.groupedBy = undefined
	router.push({ hash: "#" + new URLSearchParams(e) });
    };



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
		current = { list: [], ...getInstAppAccount(e) };
		groupedBy.push(current);
	    }
	    current.list.push({ date: new Date(e.date), nbSmsAndDetails: nbSmsAndDetails(e) });
	}
	return groupedBy;
    };

    watch(
      () => ({ maxResults: $scope.nbResults, ...$scope.accountFilter }),
      function(fullFilter) {
	if ($scope.inProgress) return;
	$scope.inProgress = true;
	restWsHelpers.simple('summary/detailed', fullFilter)
	    .then(function (flatList) {
		$scope.noMoreResults = flatList.length < fullFilter.maxResults;
		$scope.groupedBy = computeGroupedByRaw(flatList);
		$scope.inProgress = false;
	    }, function (resp) {
		// an error occured.
		if (resp && resp.status == 404 && $scope.accountFilter.account) {
		    // try removing accountFilter
		    setAppAccount({});
		}
	    });
    }, { immediate: true });

    const showMoreResults = function () {
	if ($scope.noMoreResults) return;
	$scope.nbResults = $scope.nbResults + initialNbResults;
    };

    return { ...toRefs($scope), setAppAccount, showMoreResults }
  }
}
</script>