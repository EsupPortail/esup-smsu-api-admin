(function () {
"use strict";

var app = angular.module('myApp');

app.config(function($routeProvider, routesProvider) {
    routesProvider.routes = routesProvider.computeRoutes(globals.baseURL);
    for (const tab of routesProvider.routes) {
	if (tab.controller) $routeProvider.when(tab.route, {templateUrl: tab.templateUrl, controller: tab.controller, resolve: tab.resolve});
    }
    $routeProvider.otherwise({redirectTo: '/welcome'});
});

})();
