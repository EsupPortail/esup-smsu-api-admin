import * as basicHelpers from './basicHelpers.js'

var app = angular.module('myApp');

app.service('h', function (restWsHelpers) {

var h = this;

Object.assign(this, basicHelpers);

function willCallRest($function) {
    return function () {
	return restWsHelpers.simple($function);
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

    return restWsHelpers.action('post', 'accounts', { name: name, quota: 0 }).then(function () {
	return h.accounts();
    }).then(function (accounts) {
	return h.array2hash(accounts, 'name')[name];
    });
};

});

