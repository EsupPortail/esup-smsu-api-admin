(function () {
"use strict";

var app = angular.module('myApp');

app.service('loginSuccess', function ($rootScope, basicHelpers) {

var loginSuccess = this;

this.set = function (loggedUser) {
    console.log('user logged in: ' + loggedUser.login + " " + loggedUser.role);

    $rootScope.sessionId = loggedUser.sessionId;
    delete loggedUser.sessionId;

    function setIt() {
	$rootScope.loggedUser = userWithCapabilities(loggedUser, $rootScope.roles);
    };
    if ($rootScope.roles) {
	setIt();
    } else {
	getRolesInScope($rootScope).then(setIt);
    }
};

function computeUserCapabilities(user, roles) {
    var role = roles[user.role];
    if (!role) console.log("user " + user.login + " has unknown role " + user.role);
    var can = {};
    angular.forEach(role.fonctions || [], function (c) {
	can[c] = true;
    });
    return can;
}

function userWithCapabilities(user, roles) {
    user.can = computeUserCapabilities(user, roles);
    return user;
}

function getRolesInScope($scope) {
    // NB: hack for restWsHelpers: to break circular dependencies,
    // loginSuccess.restWsHelpers is set by hand in restWsHelpers.java
    return loginSuccess.restWsHelpers.simple('roles').then(function(roles) {
	$scope.roles = basicHelpers.array2hash(roles, 'role');
    });
}

});

})();