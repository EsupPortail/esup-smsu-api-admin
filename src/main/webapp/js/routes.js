(function () {
"use strict";

function resolve(l) {
    var r = {};
    angular.forEach(arguments, function (name) {
	r["h_" + name] = function (h) { return h[name](); };
    });
    return r;
}

function computeRoutes(baseURL) {
  var templatesBaseURL = baseURL + "/partials";
  var l =
    [{ route: '/welcome', mainText: "Accueil", controller: 'EmptyCtrl' }, 
     { route: '/applications', mainText: "Applications clientes", show: 'loggedUser.can.FCTN_API_CONFIG_APPLIS', controller: 'ApplicationsCtrl', resolve: resolve('applications') },
     { route: '/accounts', mainText: "Comptes d'imputation", show: 'loggedUser.can.FCTN_GESTION_CPT_IMPUT', controller: 'AccountsCtrl', resolve: resolve('accounts') },
     { route: '/consolidatedSummary', mainText: "Relevé consolidé", show: 'loggedUser.can.FCTN_API_EDITION_RAPPORT', controller: 'ConsolidatedSummaryCtrl', resolve: resolve('summary_consolidated') },
     { route: '/detailedSummary', mainText: "Relevé détaillé", show: 'loggedUser.can.FCTN_API_EDITION_RAPPORT', controller: 'DetailedSummaryCtrl', resolve: resolve('summary_detailed_criteria') },
     { route: '/users', mainText: "Gestion des utilisateurs", show: 'loggedUser.can.FCTN_MANAGE_USERS', controller: 'UsersCtrl', resolve: resolve('users') },
     { route: '/logout', mainText: "Déconnexion", show: 'allowLogout', controller: 'EmptyCtrl' },
     { route: '/about', mainText: "A propos de", title: "A propos de SMSU-U", controller: 'EmptyCtrl'},
     { route: '/users/:id', parent: '/users', controller: 'UsersDetailCtrl', templateUrl: templatesBaseURL + '/users-detail.html', resolve: resolve('users') },
     { route: '/applications/:id', parent: '/applications', controller: 'ApplicationsDetailCtrl', templateUrl: templatesBaseURL + '/applications-detail.html', resolve: resolve('accounts','applications') },
     { route: '/accounts/:id', parent: '/accounts', controller: 'AccountsDetailCtrl', templateUrl: templatesBaseURL + '/accounts-detail.html', resolve: resolve('accounts') }];
  angular.forEach(l, function (tab) {
    if (!tab.templateUrl) tab.templateUrl = templatesBaseURL + tab.route + '.html';
  });
  return l;
}

function findCurrentTab($scope, templateUrl) {
    var routes = this.routes;
    var tab = this.h.simpleFind(routes, function (tab) { return tab.templateUrl === templateUrl; });
    if (!tab) return;
    var mainTab;
    if (tab.parent) {
	mainTab = this.h.simpleFind(routes, function (mainTab) { return mainTab.route === tab.parent; });
    } else {
	mainTab = tab;
    }
    $scope.currentMainTab = mainTab;
    $scope.currentTab = tab;
}

var app = angular.module('myApp');

app.provider('routes', function () {
    this.routes = [];
    this.computeRoutes = computeRoutes;
    this.$get = function (basicHelpers) {
	return { routes: this.routes, findCurrentTab: findCurrentTab, h: basicHelpers };
    };
});

}());
