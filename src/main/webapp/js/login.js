import * as basicHelpers from './basicHelpers.js'


function loadScript(url, onerror) {
    var elt = document.createElement("script");
    elt.setAttribute("type", "text/javascript");
    elt.setAttribute("src", url);
    elt.onerror = onerror
    document.head.appendChild(elt);
}

var jsonpDeferreds = []
window.jsonp_success = function(resp) {
    console.log('jsonp_success', resp)
    for (const p of jsonpDeferreds) { p.resolve(resp) }
    jsonpDeferreds = []
}
function jsonp_error(resp) {
    console.log('jsonp_error', resp)
    for (const p of jsonpDeferreds) { p.reject(resp) }
    jsonpDeferreds = []
}

export let fake = { jsonp: undefined }
export const jsonp = function () {
    if (fake.jsonp) return fake.jsonp()
    
    if (globals.jsonpDisabled) {
	return Promise.reject("jsonp login disabled");
    }

    console.log("jsonpLogin start");
    var defer = basicHelpers.promise_defer()
    jsonpDeferreds.push(defer)
    if (jsonpDeferreds.length === 1) {
        loadScript(globals.baseURL + '/rest/login?callback=jsonp_success', jsonp_error)
    }
    return defer.promise
};

function mayAddIdpId($rootScope, url) {
    if ($rootScope.idpId) url += "&idpId=" + encodeURIComponent($rootScope.idpId);
    return url;
}

function windowOpenDivCreate($rootScope, isRelog) {
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
function windowOpenOnMessage($rootScope, state) {
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
    window.addEventListener("message", onmessage);  
    return onmessage;
}
export let windowOpenState = {};
function windowOpenCleanup(state) {
    try {
	if (state.div) state.div.remove();
	if (state.iframe) state.iframe.remove();
	if (state.listener) window.removeEventListener("message", state.listener);  
	if (state.window) state.window.close(); 
    } catch (e) {}
    windowOpenState = {};
}
export const windowOpen = function ($rootScope, isRelog) {
    windowOpenCleanup(windowOpenState);

    var postMessageURL = mayAddIdpId($rootScope, globals.baseURL + '/rest/login?postMessage');

    var state = {};
    windowOpenState = state;

    state.deferredLogin = h.promise_defer();
    state.deferredQueue = [];
    state.div = windowOpenDivCreate($rootScope, isRelog);
    state.listener = windowOpenOnMessage($rootScope, state); 
    state.div.bind("click", function () {
	state.window = window.open(postMessageURL);
    });

    if (globals.jsonpDisabled && $rootScope.idpId) {
	// alternative to jsonp:
	state.iframe = hiddenIframeCreate(postMessageURL);
    }

    return state.deferredLogin.promise;
};

export const mayRedirect = function ($rootScope) {
	console.log("jsonpLogin failed, trying redirect");
	var then = window.location.hash && window.location.hash.replace(/^#/, '');
	var dest = globals.baseURL + '/rest/login?then=' + encodeURIComponent(then);
	window.location.href = mayAddIdpId($rootScope, dest);
	// the redirect may take time, in the meantime, do not think login was succesful
	return Promise.reject("jsonpLogin failed, trying redirect");
};

