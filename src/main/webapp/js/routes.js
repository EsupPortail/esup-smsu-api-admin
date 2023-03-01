import About from './components/About.js'
import AccountsDetail from './components/AccountsDetail.js'
import Accounts from './components/Accounts.js'
import ApplicationsDetail from './components/ApplicationsDetail.js'
import Applications from './components/Applications.js'
import ConsolidatedSummary from './components/ConsolidatedSummary.js'
import DetailedSummary from './components/DetailedSummary.js'
import UsersDetail from './components/UsersDetail.js'
import Users from './components/Users.js'
import Welcome from './components/Welcome.js'

function resolve(...l) {
    var r = {};
    for(const $function of l) {
        const name = "h_" + $function.replaceAll('/', '_')
	r[name] = function (restWsHelpers) { return restWsHelpers.simple($function); };
    }
    return r;
}

let routes = 
    [{ route: '/welcome', mainText: "Accueil", ...Welcome }, 
     { route: '/applications', mainText: "Applications clientes", show: 'loggedUser.can.FCTN_API_CONFIG_APPLIS', ...Applications, resolve: resolve('applications') },
     { route: '/accounts', mainText: "Comptes d'imputation", show: 'loggedUser.can.FCTN_GESTION_CPT_IMPUT', ...Accounts, resolve: resolve('accounts') },
     { route: '/consolidatedSummary', mainText: "Relevé consolidé", show: 'loggedUser.can.FCTN_API_EDITION_RAPPORT', ...ConsolidatedSummary, resolve: resolve('summary/consolidated') },
     { route: '/detailedSummary', mainText: "Relevé détaillé", show: 'loggedUser.can.FCTN_API_EDITION_RAPPORT', ...DetailedSummary, resolve: resolve('summary/detailed/criteria') },
     { route: '/users', mainText: "Gestion des utilisateurs", show: 'loggedUser.can.FCTN_MANAGE_USERS', ...Users, resolve: resolve('users') },
     { route: '/logout', mainText: "Déconnexion", show: 'allowLogout', ...About },
     { route: '/about', mainText: "A propos de", title: "A propos de SMSU-U", ...About },
     { route: '/users/:id', parent: '/users', ...UsersDetail, resolve: resolve('users') },
     { route: '/applications/:id', parent: '/applications', ...ApplicationsDetail, resolve: resolve('accounts','applications') },
     { route: '/accounts/:id', parent: '/accounts', ...AccountsDetail, resolve: resolve('accounts') }];

function findCurrentTab($scope, template) {
    var routes = this.routes;
    var tab = routes.find(function (tab) { return tab.template === template; });
    if (!tab) return;
    var mainTab;
    if (tab.parent) {
	mainTab = routes.find(function (mainTab) { return mainTab.route === tab.parent; });
    } else {
	mainTab = tab;
    }
    $scope.currentMainTab = mainTab;
    $scope.currentTab = tab;
}

var app = angular.module('myApp');

app.provider('routes', function () {
    this.routes = routes;
    this.$get = function () {
	return { routes: this.routes, findCurrentTab: findCurrentTab };
    };
});

app.config(function($routeProvider, routesProvider) {
    for (const tab of routesProvider.routes) {
	if (tab.controller) $routeProvider.when(tab.route, {template: tab.template, controller: tab.controller, resolve: tab.resolve});
    }
    $routeProvider.otherwise({redirectTo: '/welcome'});
});
