(function () {
"use strict";

var app = angular.module('myApp');

app.service('basicHelpers', function () {

var h = this;

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
this.uniqWith = function (array, f) {
    var o = {};
    angular.forEach(array, function (e) {
	var k = f(e);
	if (!(k in o)) o[k] = e;
    });
    return Object.values(o);
};

this.fromJsonOrNull = function(json) {
    try {
	return angular.fromJson(json);
    } catch (e) {
	return null;
    }
};

});

})();