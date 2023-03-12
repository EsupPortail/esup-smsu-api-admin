import * as h from './basicHelpers.js'
import * as restWsHelpers from './restWsHelpers.js'

export const set = function (loggedUser) {
    console.log('user logged in: ' + loggedUser.login + " " + loggedUser.role);

    function setIt() {
	$rootScope.loggedUser = userWithCapabilities(loggedUser, $rootScope.roles);
    };
    if ($rootScope.roles) {
	setIt();
    } else {
	getRolesInScope(restWsHelpers, $rootScope).then(setIt);
    }
};

function computeUserCapabilities(user, roles) {
    var role = roles[user.role];
    var can = {};
    if (!role) {
	console.log("user " + user.login + " has unknown role " + user.role);
    } else {
	for(const c of role.fonctions || []) {
	    can[c] = true;
	}
    }
    return can;
}

function userWithCapabilities(user, roles) {
    user.can = computeUserCapabilities(user, roles);
    return user;
}

function getRolesInScope(restWsHelpers, $scope) {
    return restWsHelpers.simple('roles').then(function(roles) {
	$scope.roles = h.array2hash(roles, 'role');
    });
}
