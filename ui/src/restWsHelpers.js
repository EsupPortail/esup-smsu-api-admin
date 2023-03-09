import * as h from './basicHelpers.js'
import { $rootScope } from './globals.js'
import * as login from './login.js'
import * as loginSuccess from './loginSuccess.js'

const parse_querystring = qs => (
    qs ? Object.fromEntries(new URLSearchParams(qs)) : {}
)

getUrlStartupParams();

function getUrlStartupParams() {
    // used for first URL, you can have either:
    // -  /?idpId=xxx : when you want the user to bookmark the idp choice
    // - /#/?idpId=xxx : when you want the idpId to be hidden from URL
    // also used "redirect login" if no cookies
    const params = parse_querystring(location.search || location.hash.match(/[?].*/)?.[0])
	if (getUrlStartupParam(params, 'idpId') && !location.search) {
        // clean up URL
        location.hash = location.hash.replace(/[?].*/, '');
    }
}
function getUrlStartupParam(params, name) {
    var val = params[name];
    if (val) {
	$rootScope[name] = val;
	if (name === 'idpId') console.log("got idpId from startup url");
    }
    return val
}

export function initialLogin() {

    if (globals.jsonpDisabled) {
	// try a simple XHR login, especially needed in case we arrive here after a redirect
	simple('login', {}, { noErrorHandling: true })
        .then(null, _ => login.mayRedirect($rootScope))
        .then(loginSuccess.set);
    } else {
	login.jsonp()
        .then(null, _ => login.mayRedirect($rootScope))
        .then(loginSuccess.set);
    }
}

function simpleLogin() {
    simple('login').then(loginSuccess.set);
}

function tryRelog() {

    function relogSuccess(loggedUser) {
	console.log('relog success');

	if ($rootScope.impersonatedUser) {
	    // relog does not use XHR, so X-Impersonate-User was not passed
	    // update loggedUser by using XHR
	    simpleLogin();
	} else {
	    loginSuccess.set(loggedUser);
	}
	return null;
    }
    function queueXhrRequest() {
	console.log('queuing request');
	var deferred = h.promise_defer();
	login.windowOpenState.deferredQueue.push(deferred);
	return deferred.promise;
    }

    if (login.windowOpenState.deferredQueue) {
	return queueXhrRequest();
    }
    return login.jsonp().then(relogSuccess, function () {
	if (login.windowOpenState.deferredQueue) {
	    return queueXhrRequest();
	}
	console.log("jsonpLogin failed, going to windowOpenLogin");

	$rootScope.loggedUser = undefined; // hide app
	return login.windowOpen($rootScope, 'relog').then(relogSuccess, function (resp) {
	    console.log('relog failed');
	    console.log(resp);
	    alert("relog failed");
	    return Promise.reject("needIframe");
	});
    });
}

var alerted = {};
function alertOnce(msg, timeout) {
    if (alerted[msg]) return;
    alert(msg);
    alerted[msg] = 1;
    if (timeout > 0) {
       setTimeout(function () { delete alerted[msg]; }, timeout);
    }
}

function add_query_params(url, params) {
    const qs = "" + new URLSearchParams(params)
    return url + (qs ? "?" + qs : '')
}

// { method: 'get'|'post'|'delete', url: string, data: object, params: Dictionary<string>, headers }
function xhrRequest(args, flags) {
    var onError401 = function (resp) {
	if (flags.justSuccessfullyLogged) {
		alert("FATAL : ????");
		return Promise.reject(resp);
	}
	return tryRelog().then(function () { 
	    return xhrRequest(args, { justSuccessfullyLogged: true });
	});
    };
    var onErrorFromJson = function(_resp, err) {
	    alert(err.error);
	    return Promise.reject(err);
    };
    var onError = function(resp) {
	var status = resp.status;
	if (status === 0) {
	    alert("unknown failure (server seems to be down)");
	    return Promise.reject(resp);
	} else if (status === 503) {
            alertOnce("Le serveur est en maintenance, veuillez ré-essayer ultérieurement", 2000);
            return Promise.reject(resp);         
	} else if (status === 401) {
	    return onError401(resp);
	} else if (resp.data) {
	    var err = h.fromJsonOrNull(resp.data);
	    if (err && err.error)
		return onErrorFromJson(resp, err);

	}
	alert("unknown error " + status);
	return Promise.reject(resp);
    };
    if (flags.noErrorHandling) onError = null;
    const { url, params, method, data, headers } = args
    return fetch(add_query_params(url, params), { method, body: JSON.stringify(data), headers }).then(function (resp) {
        if (resp.status !== 200) return onError(resp)
        const contentType = resp.headers.get("content-type");
        if (contentType && contentType.startsWith("application/json")) {
            return resp.json()
        }
    }, onError)
}

function headers() {
    var r = {};
    if ($rootScope.impersonatedUser) {
	r["X-Impersonate-User"] = $rootScope.impersonatedUser;
    }
    return r;
}

export function simple($function, params, flags) {
    var url = globals.baseURL + '/rest/' + $function;
    params = { ...params };
    flags = { ...flags };
    var args = { method: 'get', url: url, params: params, headers: headers() };
    return xhrRequest(args, flags);
};

export async function action(method, restPath, o) {
    var args = { method: method, url: globals.baseURL + '/rest/' + restPath, headers: headers() };
    if (method !== 'post') {
	var id = o.id;
	if (typeof id === "undefined") return alert("internal error: missing id");
	delete o.id;
	args.url += '/'+id;
    }
    if (method !== 'delete') {
	args.data = o;
	args.headers['Content-Type'] = 'application/json;charset=utf-8' 
    }
    return await xhrRequest(args, {});
};

