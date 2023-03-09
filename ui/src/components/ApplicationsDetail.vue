<template>
<div class="normalContent" v-if="app">

 <a class="btn btn-default" v-if="!app.isNew && app.deletable" @click="deleteApp()"><span class="glyphicon glyphicon-remove"></span> Supprimer l'application</a>

 <h4 style="margin-top: 2em" v-if="!app.isNew">Modifier</h4>


 <form @submit.prevent="submitted = true; submit()" class="form-horizontal">

  <div class="form-group" :class="{'has-error': submitted && !name_unique}">
    <label class="col-md-3 control-label" for="name">Nom</label>
    <div class="col-md-6">
      <input type="text" name="name" v-model="app.name" required maxlength="30" class="form-control">
      <span v-if="submitted && !name_unique" class="help-block">Already in use</span>
    </div>
  </div>

  <div class="form-group">
    <label class="col-md-3 control-label" for="password">Mot de passe</label>
    <div class="col-md-6">
      <input type="text" v-model="app.password" required maxlength="1024" class="form-control">
    </div>
  </div>

 <div class="form-group">
    <label class="col-md-3 control-label" for="institution">Etablissement</label>
    <div class="col-md-6">
      <input type="text" name="institution" v-model="app.institution" maxlength="30" class="form-control" required>
    </div>
  </div>

  <div class="form-group">
    <label class="col-md-3 control-label" for="account">Compte d'imputation</label>
    <div class="col-md-6">
      <select name="account" v-model="app.accountName" class="form-control">
	<option value="">Nouveau compte</option>
    <option v-for="acc in accounts" :value="acc.name">{{acc.name}}</option>
      </select>
    </div>  
  </div>

  <div class="form-group">
    <label class="col-md-3 control-label" for="quota">Quota</label>
    <div class="col-md-6">
      <input type="number" name="quota" v-model="app.quota" min="0" integer class="form-control">
    </div>
  </div>

  <div class="form-group">
    <div class="col-md-offset-3 col-md-6">
      <button class="btn btn-primary" type="submit">
	<span class="glyphicon" :class="{'glyphicon-plus': app.isNew, 'glyphicon-pencil': !app.isNew}"></span>
	{{app.isNew && "Créer" || "Enregistrer"}}</button>
    </div>
  </div>

 </form>

</div>
</template>

<script lang="ts">
import * as Vue from 'vue'
import * as h from "../basicHelpers.js"
import * as restWsHelpers from '../restWsHelpers.js'
import router, { currentRoutePath } from '../routes.js'
import { createEmptyAccount } from "../helpers.js"
import { $rootScope } from '../globals.js'

export default { props: ['applications', 'accounts', 'id'], setup: function(props) {
    let $scope = Vue.reactive({
        app: undefined,
        submitted: false,
    })

    const our_path = currentRoutePath() // we want it to be static
    Vue.watchEffect(() => {
	$rootScope.currentTab_text[our_path] = $scope.app?.name || (props.id === 'new' ? 'Création' : 'Modification');
    });

    let orig_app = Vue.computed(() => props.applications.find(app => app.id == props.id))
    let name2app = Vue.computed(() => h.array2hash(props.applications, 'name'))
    const checkUnique = function (name) {
	var app = name2app.value[name];
	return !app || name === orig_app.value?.name;
    };
    const name_unique = Vue.computed(() => checkUnique($scope.app?.name))

    var modify = function (method, then = null) {
	var app = h.cloneDeep($scope.app);
	delete app.isNew;
	restWsHelpers.action(method, 'applications', app).then(function () {
	    router.push(then || { path: '/applications' });
	});
    };    
    const submit = function () {
        if (!name_unique.value) return;
	var method = $scope.app.isNew ? 'post' : 'put';
	if ($scope.app.accountName === '') {
	    // must first create the account
	    var accountNameSuggestion = $scope.app.name;
	    createEmptyAccount(restWsHelpers, accountNameSuggestion, props.accounts).then(function (account) {
		$scope.app.accountName = account.name;
  		modify(method, { path: '/accounts/' + account.id, query: { isNew: null } });
	    });
	} else {
	    modify(method);
	}
    };
    const deleteApp = function () {
	modify('delete');
    };

    Vue.watchEffect(() => {
	if (props.id === "new") {
	    $scope.app = { isNew: true, accountName: '', quota: 0 };
	} else if (orig_app.value) {
		$scope.app = { ...orig_app.value };
	} else {
		alert("invalid application " + props.id);
	}
	})
    return { ...Vue.toRefs($scope), name_unique, submit, deleteApp }
  }
}
</script>