import * as h from "../basicHelpers.js"

export const template = `
<div class="normalContent" ng-show="user">

 <a class="btn btn-default" ng-hide="user.isNew" ng-click="delete()"><span class="glyphicon glyphicon-remove"></span> Supprimer l'utilisateur</a>

 <h4 style="margin-top: 2em" ng-hide="user.isNew">Modifier</h4>

 <form novalidate name="myForm" class="form-horizontal" ng-submit="submitted = 1; submit()" class="form-horizontal">

  <div class="form-group" ng-class="{'has-error': submitted && myForm.name.$invalid}">
    <label class="col-md-3 control-label" for="name">Nom</label>
    <div class="col-md-6">
      <input type="text" name="name" ng-model="user.login" my-validator="{ unique: checkUniqueName }" required maxlength="30" class="form-control">
      <span display-required-if-needed input-name="myForm.name"></span>
      <span ng-show="submitted && myForm.name.$error.unique" class="help-block">Already in use</span>
    </div>
  </div>

  <div class="form-group" ng-class="{'has-error': submitted && myForm.role.$invalid}">
    <label class="col-md-3 control-label" for="role">Rôle</label>
    <div class="col-md-6">
      <select required name="role" ng-model="user.role" ng-options="role as role for (role, caps) in roles" class="form-control">
	<option value="" ng-selected="selected">Sélectionnez un rôle</option>
      </select>
      <span ng-show="submitted && myForm.role.$error.required" class="help-block">Required</span>
    </div>
  </div>

  <div class="form-group">
    <div class="col-md-offset-3 col-md-6">
      <button class="btn btn-primary" type="submit">
	<span class="glyphicon" ng-class="{'glyphicon-plus': user.isNew, 'glyphicon-pencil': !user.isNew}"></span>
	{{user.isNew && "Créer" || "Enregistrer"}}</button>
    </div>
  </div>

 </form>

</div>
`

export default { template, controller: function($scope, restWsHelpers, $routeParams, $location, h_users) {
    var id = $routeParams.id;

    var updateCurrentTabTitle = function () {
	$scope.currentTab.text = $scope.user && $scope.user.login || (id === 'new' ? 'Création' : 'Modification');
    };
    updateCurrentTabTitle();
    $scope.$watch('user.login', updateCurrentTabTitle);

    $scope.checkUniqueName = function (name) {
	var user = $scope.login2user[name];
	return !user || user === $scope.user;
    };

    var modify = function (method) {
	var user = $scope.user;
	user = { id: user.id, login: user.login, role: user.role }; // keep only modifiable fields
	restWsHelpers.action(method, 'users', user).then(function () {
	    $location.path('/users');
	});
    };
    
    $scope.submit = function () {
	if (!$scope.myForm.$valid) return;
	modify($scope.user.isNew ? 'post' : 'put');
    };
    $scope["delete"] = function () {
	modify('delete');
    };

    $scope.login2user = h.array2hash(h_users, 'login');
    if (id === "new") {
	$scope.user = { isNew: true };
    } else {
	var id2user = h.array2hash(h_users, 'id');

	if (id in id2user) {
	    $scope.user = id2user[id];
	} else {
	    alert("invalid user " + id);
	}
    }
  }
}