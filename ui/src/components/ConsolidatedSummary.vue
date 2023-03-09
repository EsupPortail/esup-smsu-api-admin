<template>
<div v-if="appAccount">

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
  <tr v-for="e in appAccount.data">
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
  <tbody v-for="(list, institution) in appAccountsTree">
    <tr v-for="e in list.slice(0,1)">
      <td rowspan="{{list.length}}">{{institution}}</td>
      <td>{{e.app}}</td>
      <td><a href="" @click.prevent="appAccount = e">{{e.account}}</a></td>
    </tr>
    <tr v-for="e in list.slice(1)">
      <td>{{e.app}}</td>
      <td><a href="" @click.prevent="appAccount = e">{{e.account}}</a></td>
    </tr>
  </tbody>
</table>

<a href="" @click="exportCSV($event)">
  Export vers tableur
</a>
</template>

<script lang="ts">
import * as Vue from 'vue'
import * as h from "../basicHelpers.js"
import { getInstAppAccount } from "../helpers.js"

export default { props: ['summary_consolidated'], setup: function(props) {
    let $scope = Vue.reactive({ appAccount: undefined })
    var computeTree = function () {
	var tree = {};
	for (const e of props.summary_consolidated) {
	    var key1 = e.institution;
	    var key2 = e.app + "+" + e.account;
	    if (!tree[key1]) 
		tree[key1] = {};
	    if (!tree[key1][key2]) 
		tree[key1][key2] = { data: [], ...h.objectSlice(e, ['institution', 'app', 'account']) };
	    tree[key1][key2].data.unshift(e);
	}
	h.simpleEach(tree, function (subtree, key1) {
	    tree[key1] = Object.keys(subtree).sort().map(k => subtree[k]);
	});
	return tree as { [x : string]: any[] };
    };

    for (const e of props.summary_consolidated) {
	    Object.assign(e, getInstAppAccount(e));
	    e.nbReceived = e.nbSendedSMS - e.nbSMSInError;
	    e.failureRate = Math.round(e.nbSMSInError / e.nbSendedSMS * 100) + "%";
    }
    const appAccountsTree = computeTree();

    function toStringListList(list, attrs) {
	return list.map(function (o) {
	    return attrs.map(function (attr) { return ""+o[attr]; });
	});
    }
    const exportCSV = function (event) {
	var rows = toStringListList(props.summary_consolidated, ['institution', 'app', 'account', 'month', 'nbSendedSMS', 'nbReceived']);
	h.exportCSV(event.target.parentElement, rows, "smsuapi-consolidated.csv");
    };
    return { ...Vue.toRefs($scope), appAccountsTree, exportCSV }
  }
}
</script>