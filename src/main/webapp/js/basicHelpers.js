(function () {
"use strict";

var app = angular.module('myApp');

app.service('basicHelpers', function () {

var h = this;

// reify a promise: create a promise and return an object with promise + resolve/reject functions
this.promise_defer = function() {
    let deferred = {}
    deferred.promise = new Promise((resolve, reject) => { deferred.resolve = resolve; deferred.reject = reject });
    return deferred;
};
this.cloneDeep = function (o) {
    return JSON.parse(JSON.stringify(o))
};
this.simpleEach = function (o, f) {
    for (const k in o) {
        f(o[k], k)
    }
};
this.objectSlice = function (o, fields) {
    var r = {};
    for (const field of fields) {
	if (field in o)
	    r[field] = o[field];
    }
    return r;
};
this.array2hash = function (array, field) {
    var h = {};
    for (const e of array) {
	h[e[field]] = e;
    }
    return h;
};
this.array2hashMulti = function (array, field) {
    var h = {};
    for (const e of array) {
	var k = e[field];
	(h[k] = h[k] || []).push(e);
    }
    return h;
};
this.uniqWith = function (array, f) {
    var o = {};
    for (const e of array) {
	var k = f(e);
	if (!(k in o)) o[k] = e;
    }
    return Object.values(o);
};

this.fromJsonOrNull = function(json) {
    try {
	return JSON.parse(json);
    } catch (e) {
	return null;
    }
};

});

})();