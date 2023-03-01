import * as h from "../basicHelpers.js"
import { createEmptyAccount } from "../helpers.js"

export const template = `
<div class="normalContent" ng-show="app">

 <a class="btn btn-default" ng-hide="app.isNew || !app.deletable" ng-click="delete()"><span class="glyphicon glyphicon-remove"></span> Supprimer l'application</a>

 <h4 style="margin-top: 2em" ng-hide="app.isNew">Modifier</h4>


 <form novalidate  name="myForm" ng-submit="submitted = 1; submit()" class="form-horizontal">

  <div class="form-group" ng-class="{'has-error': submitted && myForm.name.$invalid}">
    <label class="col-md-3 control-label" for="name">Nom</label>
    <div class="col-md-6">
      <input type="text" name="name" ng-model="app.name" my-validator="{ unique: checkUniqueName }" required maxlength="30" class="form-control">
      <span ng-show="submitted && myForm.name.$error.required" class="help-block">Required</span>
      <span ng-show="submitted && myForm.name.$error.unique" class="help-block">Already in use</span>
    </div>
  </div>

  <div class="form-group"
       ng-class="{'has-error': submitted && myForm.password.$invalid}">
    <label class="col-md-3 control-label" for="password">Mot de passe</label>
    <div class="col-md-6">
      <input type="text" name="password" ng-model="app.password" required maxlength="1024" class="form-control">
      <span ng-show="submitted && myForm.password.$error.required" class="help-block">Required</span>
    </div>
  </div>

 <div class="form-group" ng-class="{'has-error': submitted && myForm.institution.$invalid}">
    <label class="col-md-3 control-label" for="institution">Etablissement</label>
    <div class="col-md-6">
      <input type="text" name="institution" ng-model="app.institution" maxlength="30" class="form-control" required>
      <span ng-show="submitted && myForm.institution.$error.required" class="help-block">Required</span>
    </div>
  </div>

  <div class="form-group">
    <label class="col-md-3 control-label" for="account">Compte d'imputation</label>
    <div class="col-md-6">
      <select name="account" ng-model="app.accountName" ng-options="acc.name as acc.name for acc in accounts" class="form-control">
	<option value="" ng-selected="selected">Nouveau compte</option>
      </select>
    </div>  
  </div>

  <div class="form-group"
       ng-class="{'has-error': submitted && myForm.quota.$invalid}">
    <label class="col-md-3 control-label" for="quota">Quota</label>
    <div class="col-md-6">
      <input type="number" name="quota" ng-model="app.quota" min="0" integer class="form-control">
      <span ng-show="submitted && myForm.quota.$invalid" class="help-block">Nombre positif</span>
    </div>
  </div>

  <div class="form-group">
    <div class="col-md-offset-3 col-md-6">
      <button class="btn btn-primary" type="submit">
	<span class="glyphicon" ng-class="{'glyphicon-plus': app.isNew, 'glyphicon-pencil': !app.isNew}"></span>
	{{app.isNew && "Créer" || "Enregistrer"}}</button>
    </div>
  </div>

 </form>

</div>
`

export default { template, controller: function($rootScope, $scope, restWsHelpers, $routeParams, $location, h_accounts, h_applications) {
    var id = $routeParams.id;

    $scope.accounts = h_accounts;

    var updateCurrentTabTitle = function () {
	$scope.currentTab.text = $scope.app && $scope.app.name || (id === 'new' ? 'Création' : 'Modification');
    };
    updateCurrentTabTitle();
    $scope.$watch('app.name', updateCurrentTabTitle);

    $scope.checkUniqueName = function (name) {
	var app = $scope.name2app[name];
	return !app || app === $scope.app;
    };

    var modify = function (method, then) {
	var app = h.cloneDeep($scope.app);
	delete app.isNew;
	restWsHelpers.action(method, 'applications', app).then(function () {
	    $location.url(then || '/applications');
	});
    };    
    $scope.submit = function () {
	if (!$scope.myForm.$valid) return;
	var method = $scope.app.isNew ? 'post' : 'put';
	if ($scope.app.accountName == null) {
	    // must first create the account
	    var accountNameSuggestion = $scope.app.name;
	    createEmptyAccount(restWsHelpers, accountNameSuggestion, $scope.accounts).then(function (account) {
		$scope.app.accountName = account.name;
  		modify(method, '/accounts/' + account.id + '?isNew');
	    });
	} else {
	    modify(method);
	}
    };
    $scope["delete"] = function () {
	modify('delete');
    };

    $scope.name2app = h.array2hash(h_applications, 'name');

	if (id === "new") {
	    $scope.app = { isNew: true, accountName: null, quota: 0 };
	} else {
	    var id2app = h.array2hash(h_applications, 'id');

	    if (id in id2app) {
		$scope.app = id2app[id];
	    } else {
		alert("invalid application " + id);
	    }
	}
  }
}
