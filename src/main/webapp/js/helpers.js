(function () {
"use strict";

var app = angular.module('myApp');

app.service('h', function ($http, $rootScope, routes, globals, $q, $timeout, $window, basicHelpers) {

var h = this;

angular.extend(this, basicHelpers);

this.jsonpLogin = function () {
    return $http.jsonp(globals.baseURL + '/rest/login?callback=JSON_CALLBACK').then(function (resp) {
	return resp.data;
    });
};

function windowOpenLoginDivCreate() {
    var elt = angular.element('<div>', {'class': 'windowOpenLoginDiv alert alert-warning'});
    elt.html('Votre session a expiré. Veuillez vous identifier à nouveau. <span class="glyphicon glyphicon-log-in"></span>');
    angular.element('.myAppDiv').prepend(elt);
    return elt;
}
function windowOpenLoginOnMessage(state) {
    var onmessage = function(e) {
	if (typeof e.data !== "string") return;
	var m = e.data.match(/^loggedUser=(.*)$/);
	if (!m) return;

	windowOpenLoginCleanup(state);
	$rootScope.$apply(function () { 
	    state.deferredLogin.resolve(angular.fromJson(m[1]));
	    angular.forEach(state.deferredQueue, function (deferred) { deferred.resolve(); });
	});
    };
    $window.addEventListener("message", onmessage);  
    return onmessage;
}
var windowOpenLoginState = {};
function windowOpenLoginCleanup(state) {
    try {
	if (state.div) state.div.remove();
	if (state.listener) $window.removeEventListener("message", state.listener);  
	if (state.window) state.window.close(); 
    } catch (e) {}
    windowOpenLoginState = {};
};
this.windowOpenLogin = function () {
    $rootScope.loggedUser = undefined; // hide app

    windowOpenLoginCleanup(windowOpenLoginState);
    var state = windowOpenLoginState = {};

    state.deferredLogin = $q.defer();
    state.deferredQueue = [];
    state.div = windowOpenLoginDivCreate();
    state.div.bind("click", function () {
	state.listener = windowOpenLoginOnMessage(state); 
	state.window = $window.open(globals.baseURL + '/rest/login?postMessage');
    });
    return state.deferredLogin.promise;
};

this.loginMayRedirect = function () {
    if (globals.isWebWidget) {
	// we can't redirect
	console.log("jsonpLogin failed, trying windowOpenLogin");
	return h.windowOpenLogin();
    } else {
	console.log("jsonpLogin failed, trying redirect");
	var then = $window.location.hash && $window.location.hash.replace(/^#/, '');
	$window.location.href = globals.baseURL + '/rest/login?then=' + encodeURIComponent(then);
    }
};

function tryRelog() {

    function relogSuccess(loggedUser) {
	console.log('relog success');

	if ($rootScope.impersonatedUser) {
	    // relog does not use XHR, so X-Impersonate-User was not passed
	    // update loggedUser by using XHR
	    h.callRest('login').then(h.setLoggedUser);
	} else {
	    h.setLoggedUser(loggedUser);
	}
	return null;
    }
    function queueXhrRequest() {
	console.log('queuing request');
	var deferred = $q.defer();
	windowOpenLoginState.deferredQueue.push(deferred);
	return deferred.promise;
    }

    if (windowOpenLoginState.deferredQueue) {
	return queueXhrRequest();
    }
    return h.jsonpLogin().then(relogSuccess, function () {
	if (windowOpenLoginState.deferredQueue) {
	    return queueXhrRequest();
	}
	console.log("jsonpLogin failed, going to windowOpenLogin");
	return h.windowOpenLogin().then(relogSuccess, function (resp) {
	    console.log('relog failed');
	    console.log(resp);
	    alert("relog failed");
	    return $q.reject("needIframe");
	});
    });
}

this.setLoggedUser = function (loggedUser) {
    console.log('user logged in: ' + loggedUser.login + " " + loggedUser.role);

    $rootScope.sessionId = loggedUser.sessionId;
    delete loggedUser.sessionId;

    function setIt() {
	$rootScope.loggedUser = h.userWithCapabilities(loggedUser, $rootScope.roles);
    };
    if ($rootScope.roles) {
	setIt();
    } else {
	h.getRolesInScope($rootScope).then(setIt);
    }
};

function fromJsonOrNull(json) {
    try {
	return angular.fromJson(json);
    } catch (e) {
	return null;
    }
}

function setHttpHeader(methods, name, val) {
    var headers = $http.defaults.headers;
    angular.forEach(methods, function (method) {
	if (!headers[method]) headers[method] = {};
	headers[method][name] = val;
    });
}

var alerted = {};
function alertOnce(msg, timeout) {
    if (alerted[msg]) return;
    alert(msg);
    alerted[msg] = 1;
    if (timeout > 0) {
       $timeout(function () { delete alerted[msg]; }, timeout);
    }
}

var cookiesRejected = false;
function xhrRequest(args, flags) {
    var onError401 = function (resp) {
	if (flags.justSuccessfullyLogged) {
	    if (!flags.cookiesRejected) {
		console.log("It looks like our cookies are rejected. Trying to pass sessionId in URLs...");
		cookiesRejected = true;
		return xhrRequest(args, flags);
	    } else {	
		alert("FATAL : both cookies and URL parameter jsessionid are rejected");
		return $q.reject(resp);
	    }
	}
	return tryRelog().then(function () { 
	    return xhrRequest(args, { justSuccessfullyLogged: true });
	});
    };
    var onErrorCsrf = function (resp, err) {
	if (flags.xhrRequestInvalidCsrfState) {
	    alert("Invalid CRSF prevention token failed twice");
	    return $q.reject(resp);
	}
	setHttpHeader(['post','put','delete'], "X-CSRF-TOKEN", err.token);
	console.log("retrying with new CSRF token");
	return xhrRequest(args, { xhrRequestInvalidCsrfState: true });
    };
    var onErrorFromJson = function(resp, err) {
	if (err.error === "Invalid CRSF prevention token")
	    return onErrorCsrf(resp, err);
	else {
	    alert(err.error);
	    return $q.reject(resp);
	}
    };
    var onError = function(resp) {
	var status = resp.status;
	if (status == 0) {
	    alert("unknown failure (server seems to be down)");
	    return $q.reject(resp);
	} else if (status === 503) {
            alertOnce("Le serveur est en maintenance, veuillez ré-essayer ultérieurement", 2000);
            return $q.reject(resp);         
	} else if (status === 401) {
	    return onError401(resp);
	} else if (resp.data) {
	    var err = fromJsonOrNull(resp.data);
	    if (err && err.error)
		return onErrorFromJson(resp, err);

	}
	alert("unknown error " + status);
	return $q.reject(resp);
    };
    if (cookiesRejected && !flags.cookiesRejected) {
	flags.cookiesRejected = true;
	args = angular.copy(args);
	args.url = args.url + ";jsessionid=" + $rootScope.sessionId;
    }
    return $http(args).then(function (resp) {
	return resp;
    }, onError);
}

this.callRest_headers = function () {
    var r = {};
    if ($rootScope.impersonatedUser) {
	r["X-Impersonate-User"] = $rootScope.impersonatedUser;
    }
    console.log(r);
    return r;
};

this.callRest = function ($function, params) {
    var url = globals.baseURL + '/rest/' + $function;
    params = angular.extend({}, params);
    params.cacheSlayer = new Date().getTime(); // for our beloved IE which caches every AJAX... ( http://stackoverflow.com/questions/16098430/angular-ie-caching-issue-for-http )
    var args = { method: 'get', url: url, params: params, headers: h.callRest_headers() };
    return xhrRequest(args, {}).then(function(resp) {
	return resp.data;
    });
};

this.callRestModify = function (method, restPath, o) {
    var args = { method: method, url: globals.baseURL + '/rest/' + restPath, headers: h.callRest_headers() };
    if (method !== 'post') {
	var id = o.id;
	if (typeof id === "undefined") return alert("internal error: missing id");
	delete o.id;
	args.url += '/'+id;
    }
    if (method !== 'delete') {
	args.data = o;
    }
    return xhrRequest(args, {});
};

var computeUserCapabilities = function (user, roles) {
    var role = roles[user.role];
    if (!role) console.log("user " + user.login + " has unknown role " + user.role);
    var can = {};
    angular.forEach(role.fonctions || [], function (c) {
	can[c] = true;
    });
    return can;
};

this.userWithCapabilities = function (user, roles) {
    user.can = computeUserCapabilities(user, roles);
    return user;
};

this.findCurrentTab = function ($scope, templateUrl) {
    var tab = h.simpleFind(routes.routes, function (tab) { return tab.templateUrl == templateUrl; });
    if (!tab) return;
    var mainTab;
    if (tab.parent) {
	mainTab = h.simpleFind(routes.routes, function (mainTab) { return mainTab.route == tab.parent; });
    } else {
	mainTab = tab;
    }
    $scope.currentMainTab = mainTab;
    $scope.currentTab = tab;
};

this.getTemplateUrl = function (basename) {
    return globals.baseURL + '/partials/' + basename;
};

this.getRolesInScope = function ($scope) {
    return h.callRest('roles').then(function(roles) {
	$scope.roles = h.array2hash(roles, 'role');
    });
};

this.getInstAppAccount = function (e) {
    return { institution: e.institution, app: e.appName, account: e.accountName };
};

this.getUsers = function () {
    return h.callRest('users').then(function(users) {
	return h.array2hash(users, 'id');
    });
};

this.getApplications = function () {
    return h.callRest('applications');
};

this.getAccounts = function () {
    return h.callRest('accounts');
};

function findName(base, existingHash) {
    var name = base;
    var i = 2;
    while (name in existingHash) {
	name = base + ' ' + i++;
    }
    return name;
}

this.createEmptyAccount = function (accountNameSuggestion, accounts) {
    var name = findName(accountNameSuggestion, h.array2hash(accounts, 'name'));
    console.log("found free account name " + name);

    return h.callRestModify('post', 'accounts', { name: name, quota: 0 }).then(function () {
	return h.getAccounts();
    }).then(function (accounts) {
	return h.array2hash(accounts, 'name')[name];
    });
};

this.cleanupUportalCss = function () {
    // get rid of ugly stuff from uportal...
    $('link[href*="/fss-framework-1.1.2.min.css"]').attr('disabled', 'disabled');
    $('link[href*="/portlet.css"]').attr('disabled', 'disabled');
    //$('link[href*="/fss-theme-mist.min.css"]').attr('disabled', 'disabled');
    $('.fl-theme-mist').toggleClass('fl-theme-mist');
};

function toCSV(rows, attrs) {
    return rows.map(function (row) {
	return row.map(function (v) { 
	    return v.replace(/,/g, '');
	}).join(',');
    }).join("\n");
}

this.exportCSV = function (domElt, rows, fileName) {
    var csv = toCSV(rows);
    var uri = "data:text/csv;charset=utf-8," + csv;
    var link = document.createElement("a");
    link.setAttribute("href", encodeURI(uri));
    if (fileName) link.setAttribute("download", fileName);
    domElt.appendChild(link); // needed on Firefox, but not Chromium.
    link.click();
};

});

})();
