function debug_msg(msg) {
    $("#debug").html($("#debug").html() + "<br>" + msg);
};

(function () {
"use strict";

var app = angular.module('myApp');

app.service('h', function ($http, $rootScope, routes, globals, $q, $timeout, $window) {

var h = this;

this.objectKeys = function (o) {
    return $.map(o, function (l, k) { return k; });
};
this.objectValues = function (o) {
    return $.map(o, function (l, k) { return l; });
};
this.objectSlice = function (o, fields) {
    var r = {};
    angular.forEach(fields, function (field) {
	if (field in o)
	    r[field] = o[field];
    });
    return r;
};
this.array2hash = function (array, field) {
    var h = {};
    angular.forEach(array, function (e) {
	h[e[field]] = e;
    });
    return h;
};
this.array2hashMulti = function (array, field) {
    var h = {};
    angular.forEach(array, function (e) {
	var k = e[field];
	(h[k] = h[k] || []).push(e);
    });
    return h;
};
this.array_map = function (array, f) {
    var r = [];
    angular.forEach(array, function (e) {
	r.push(f(e)); 
    });
    return r;
};
this.simpleFilter = function (array, f) {
    var r = [];
    angular.forEach(array, function (e) {
	if (f(e)) r.push(e);
    });
    return r;
};
this.simpleFind = function (array, f) {
    var r;
    angular.forEach(array, function (e) {
	if (f(e)) r = e; 
    });
    return r;
};
this.uniqWith = function (array, f) {
    var o = {};
    angular.forEach(array, function (e) {
	var k = f(e);
	if (!(k in o)) o[k] = e;
    });
    return h.objectValues(o);
};

this.jsonpLogin = function () {
    return $http.jsonp(globals.baseURL + '/rest/login?callback=JSON_CALLBACK').then(function (resp) {
	return resp.data;
    });
};

var iframeLoginDOM;
var iframeLoginOnMessage;
function iframeLoginCleanup() {
    if (iframeLoginDOM) iframeLoginDOM.remove();   
    //if (iframeLoginOnMessage) angular.element($window).unbind("message", iframeLoginOnMessage);
    if (iframeLoginOnMessage) $window.removeEventListener("message", iframeLoginOnMessage);  
    iframeLoginDOM = iframeLoginOnMessage = undefined;
};

this.iframeLogin = function () {
    iframeLoginCleanup();

    var deferred = $q.defer();
    iframeLoginOnMessage = function(e) {
	console.log('iframeLoginOnMessage');
	console.log(e);
	if (typeof e.data === "string") {
	    var m = e.data.match(/^loggedUser=(.*)$/);
	    if (m) {
		iframeLoginCleanup();
		$rootScope.$apply(function () { 
		    deferred.resolve(angular.fromJson(m[1]));
		});
	    }
	}
    };
    // below is not working, why?
    //angular.element('window').bind("message", iframeLoginOnMessage);
    $window.addEventListener("message", iframeLoginOnMessage);  

    iframeLoginDOM = angular.element('<iframe>', {src: globals.baseURL + '/rest/login', 'class': 'iframeLogin delayed'});
    angular.element('body').prepend(iframeLoginDOM);

    $timeout(function () {
	if (iframeLoginDOM) iframeLoginDOM.removeClass('delayed');
    }, 500);

    return deferred.promise;
};

this.setLoggedUser = function (loggedUser) {
    console.log('user logged in: ' + loggedUser.login + " " + loggedUser.role);

    function setIt() {
	$rootScope.loggedUser = h.userWithCapabilities(loggedUser, $rootScope.roles);
    };
    if ($rootScope.roles) {
	setIt();
    } else {
	h.getRolesInScope($rootScope).then(setIt);
    }
};

function setHttpHeader(methods, name, val) {
    var headers = $http.defaults.headers;
    angular.forEach(methods, function (method) {
	if (!headers[method]) headers[method] = {};
	headers[method][name] = val;
    });
}

var xhrRequest401State = false;
var xhrRequestInvalidCsrfState = false;
function xhrRequest(args) {
    var onError = function(resp) {
	var status = resp.status;
	if (status == 401) {
	    if (xhrRequest401State) {
		alert("fatal, relog failed");
		return $q.reject(resp);
	    } else {
		xhrRequest401State = true;
		return h.iframeLogin().then(function (loggedUser) {
		    console.log('relog success');
		    h.setLoggedUser(loggedUser);
		    return xhrRequest(args);
		}, function (resp) {
		    console.log('relog failed');
		    console.log(resp);
		    alert("relog failed");
		    return $q.reject("needIframe");
		});
	    }
	} else if (status == 0) {
	    alert("unknown failure (server seems to be down)");
	    return $q.reject(resp);
	} else {
	    var msg = "unknown error " + status;
	    if (resp.data) {
		try {
		    var err = angular.fromJson(resp.data);
		    msg = err.error;
		    if (msg === "Invalid CRSF prevention token" && !xhrRequestInvalidCsrfState) {
			console.log("retrying with new CSRF token");
			setHttpHeader(['post','put','delete'], "X-CSRF-TOKEN", err.token);
			xhrRequestInvalidCsrfState = true;
			return xhrRequest(args);
		    }
		} catch (e) {
		    console.log("??"); console.log(e);
		}
	    }
	    alert(msg);
	    return $q.reject(resp);
	}
    };
    return $http(args).then(function (resp) {
	if (xhrRequest401State) { console.log('rest after relog success'); console.log(resp); }
	xhrRequest401State = false;
	xhrRequestInvalidCsrfState = false;
	return resp;
    }, onError);
}

this.callRest = function ($function, params) {
    var url = globals.baseURL + '/rest/' + $function;
    params = angular.extend({}, params);
    params.cacheSlayer = new Date().getTime(); // for our beloved IE which caches every AJAX... ( http://stackoverflow.com/questions/16098430/angular-ie-caching-issue-for-http )
    var args = { method: 'get', url: url, params: params };
    return xhrRequest(args).then(function(resp) {
	return resp.data;
    });
};

this.callRestModify = function (method, restPath, o) {
    var args = { method: method, url: globals.baseURL + '/rest/' + restPath };
    if (method !== 'post') {
	var id = o.id;
	if (typeof id === "undefined") return alert("internal error: missing id");
	delete o.id;
	args.url += '/'+id;
    }
    if (method !== 'delete') {
	args.data = o;
    }
    return xhrRequest(args);
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

});

})();
