import * as h from "../basicHelpers.js"

export const template = `
<div class="normalContent" ng-show="account">

 <div ng-show="isNew" class="alert alert-success">Le nouveau compte « {{account.name}} » vient d'être créé, vous pouvez maintenant le modifier.</div>

 <a class="btn btn-default" ng-hide="appDisabled" ng-click="disable()"><span class="glyphicon glyphicon-remove"></span> Désactiver le compte</a>

 <h4 style="margin-top: 2em">Modifier</h4>

 <form novalidate  name="myForm" ng-submit="submitted = 1; submit()" class="form-horizontal">

  <div class="form-group" ng-class="{'has-error': submitted && myForm.name.$invalid}">
    <label class="col-md-3 control-label" for="name">Nom</label>
    <div class="col-md-6">
      <input type="text" name="name" ng-model="account.name" my-validator="{ unique: checkUniqueName }" required maxlength="30" class="form-control">
      <span ng-show="submitted && myForm.name.$error.required" class="help-block">Required</span>
      <span ng-show="submitted && myForm.name.$error.unique" class="help-block">Already in use</span>
    </div>
  </div>

  <div class="form-group"
       ng-class="{'has-error': submitted && myForm.quota.$invalid}">
    <label class="col-md-3 control-label" for="quota">Quota</label>
    <div class="col-md-6">
      <input type="number" name="quota" ng-model="account.quota" min="0" integer class="form-control">
      <span ng-show="submitted && myForm.quota.$invalid" class="help-block">Nombre positif</span>
    </div>
  </div>

  <div class="form-group">
    <div class="col-md-offset-3 col-md-6">
      <button class="btn btn-primary" type="submit">
	<span class="glyphicon" ng-class="{'glyphicon-pencil': !account.isNew}"></span>
	{{account.isNew && "Créer" || "Enregistrer"}}</button>
    </div>
  </div>

 </form>

</div>
`
export default { template, controller: function($scope, restWsHelpers, $routeParams, $location, h_accounts) {
    var id = $routeParams.id;
    $scope.isNew = $routeParams.isNew;

    var updateCurrentTabTitle = function () {
	$scope.currentTab.text = $scope.account && $scope.account.name || ($scope.isNew ? 'Création' : 'Modification');
    };
    updateCurrentTabTitle();
    $scope.$watch('account.name', updateCurrentTabTitle);

    $scope.checkUniqueName = function (name) {
	var account = $scope.name2account[name];
	return !account || account === $scope.account;
    };

    var modify = function (method) {
	var account = h.cloneDeep($scope.account);
	restWsHelpers.action(method, 'accounts', account).then(function () {
	    $location.path('/accounts');
	});
    };    
    $scope.submit = function () {
	if (!$scope.myForm.$valid) return;
	modify('put');
    };
    $scope.disable = function () {
	$scope.account.quota = 0;
	modify('put');	
    };

    $scope.name2account = h.array2hash(h_accounts, 'name');
    var id2account = h.array2hash(h_accounts, 'id');

	if (id in id2account) {
	    $scope.accounts = h_accounts;
	    $scope.account = id2account[id];
	    $scope.appDisabled = $scope.account.quota == 0;
	} else {
	    alert("invalid account " + id);
	}
  }
}
