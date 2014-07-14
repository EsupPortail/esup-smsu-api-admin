(function () {
"use strict";

var app = angular.module('myApp');

app.service('h', function (routes, globals, basicHelpers, restWsHelpers) {

var h = this;

angular.extend(this, basicHelpers);

// rename restWsHelpers methods for compat
this.callRest = restWsHelpers.simple;
this.callRestModify = restWsHelpers.action;


this.findCurrentTab = function ($scope, templateUrl) {
    var tab = h.simpleFind(routes.routes, function (tab) { return tab.templateUrl === templateUrl; });
    if (!tab) return;
    var mainTab;
    if (tab.parent) {
	mainTab = h.simpleFind(routes.routes, function (mainTab) { return mainTab.route === tab.parent; });
    } else {
	mainTab = tab;
    }
    $scope.currentMainTab = mainTab;
    $scope.currentTab = tab;
};

this.getTemplateUrl = function (basename) {
    return globals.baseURL + '/partials/' + basename;
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

function toCSV(rows, attrs) {
    return rows.map(function (row) {
	return row.map(function (v) { 
	    return v.replace(/,/g, '');
	}).join(',');
    }).join("\n");
}

this.exportCSV = function (domElt, rows, fileName) {
    var csv = toCSV(rows);
    var uri = "data:text/csv;charset=utf-8," + csv;
    var link = document.createElement("a");
    link.setAttribute("href", encodeURI(uri));
    if (fileName) link.setAttribute("download", fileName);
    domElt.appendChild(link); // needed on Firefox, but not Chromium.
    link.click();
};

});

})();
