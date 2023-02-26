(function () {
"use strict";

var app = angular.module('myApp');

app.service('login', function ($http, globals, $window, $rootScope) {

var login = this;

this.jsonp = function () {
    if (globals.jsonpDisabled) {
	return Promise.reject("jsonp login disabled");
    }

    console.log("jsonpLogin start");
    return $http.jsonp(globals.baseURL + '/rest/login?callback=JSON_CALLBACK').then(function (resp) {
	console.log("jsonpLogin ok");
	return resp.data;
    });
};

function mayAddIdpId(url) {
    if ($rootScope.idpId) url += "&idpId=" + encodeURIComponent($rootScope.idpId);
    return url;
}

function windowOpenDivCreate(isRelog) {
    var elt = angular.element('<div>', {'class': 'windowOpenLoginDiv alert alert-warning'});
    var msg = globals.jsonpDisabled && !isRelog ? 
	($rootScope.idpId ? 
	 "Authentification en cours. Pour forcer l'authentification, veuillez cliquer ci-dessous" : 
	 "Veuillez cliquer ici pour vous authentifier") :
	'Votre session a expiré. Veuillez vous identifier à nouveau.';
    elt.html(msg + ' <span class="glyphicon glyphicon-log-in"></span>');
    angular.element('.myAppDiv').prepend(elt);
    return elt;
}
function hiddenIframeCreate(url) {
    var elt = angular.element('<iframe>', {'src': url, 'style': 'display: none'});
    angular.element('.myAppDiv').prepend(elt);
    return elt;
}
function windowOpenOnMessage(state) {
    var onmessage = function(e) {
	if (typeof e.data !== "string") return;
	var m = e.data.match(/^loggedUser=(.*)$/);
	if (!m) return;

	windowOpenCleanup(state);
	$rootScope.$apply(function () { 
	    state.deferredLogin.resolve(JSON.parse(m[1]));
	    for (const deferred of state.deferredQueue) { deferred.resolve(); }
	});
    };
    $window.addEventListener("message", onmessage);  
    return onmessage;
}
this.windowOpenState = {};
function windowOpenCleanup(state) {
    try {
	if (state.div) state.div.remove();
	if (state.iframe) state.iframe.remove();
	if (state.listener) $window.removeEventListener("message", state.listener);  
	if (state.window) state.window.close(); 
    } catch (e) {}
    login.windowOpenState = {};
}
this.windowOpen = function (isRelog) {
    windowOpenCleanup(login.windowOpenState);

    var postMessageURL = mayAddIdpId(globals.baseURL + '/rest/login?postMessage');

    var state = {};
    login.windowOpenState = state;

    state.deferredLogin = h.promise_defer();
    state.deferredQueue = [];
    state.div = windowOpenDivCreate(isRelog);
    state.listener = windowOpenOnMessage(state); 
    state.div.bind("click", function () {
	state.window = $window.open(postMessageURL);
    });

    if (globals.jsonpDisabled && $rootScope.idpId) {
	// alternative to jsonp:
	state.iframe = hiddenIframeCreate(postMessageURL);
    }

    return state.deferredLogin.promise;
};

this.mayRedirect = function () {
	console.log("jsonpLogin failed, trying redirect");
	var then = $window.location.hash && $window.location.hash.replace(/^#/, '');
	var dest = globals.baseURL + '/rest/login?then=' + encodeURIComponent(then);
	$window.location.href = mayAddIdpId(dest);
	// the redirect may take time, in the meantime, do not think login was succesful
	return Promise.reject("jsonpLogin failed, trying redirect");
};


});

})();