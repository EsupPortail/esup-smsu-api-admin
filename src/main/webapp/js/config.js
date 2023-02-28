(function () {
"use strict";

var app = angular.module('myApp');

app.config(function($routeProvider, routesProvider) {
    for (const tab of routesProvider.routes) {
	if (tab.controller) $routeProvider.when(tab.route, {template: tab.template, controller: tab.controller, resolve: tab.resolve});
    }
    $routeProvider.otherwise({redirectTo: '/welcome'});
});

})();
