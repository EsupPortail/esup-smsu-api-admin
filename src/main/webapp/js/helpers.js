(function () {
"use strict";

var app = angular.module('myApp');

app.service('h', function (basicHelpers, restWsHelpers) {

var h = this;

angular.extend(this, basicHelpers);

// rename restWsHelpers methods for compat
this.callRest = restWsHelpers.simple;
this.callRestModify = restWsHelpers.action;

function willCallRest($function) {
    return function () {
	return h.callRest($function);
    };
}

this.getInstAppAccount = function (e) {
    return { institution: e.institution, app: e.appName, account: e.accountName };
};

this.accounts = willCallRest('accounts');
this.applications = willCallRest('applications');
this.users = willCallRest('users');
this.summary_consolidated = willCallRest('summary/consolidated');
this.summary_detailed_criteria = willCallRest('summary/detailed/criteria');

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
	return h.accounts();
    }).then(function (accounts) {
	return h.array2hash(accounts, 'name')[name];
    });
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
