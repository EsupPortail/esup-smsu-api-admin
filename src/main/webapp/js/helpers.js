import * as basicHelpers from './basicHelpers.js'

var app = angular.module('myApp');

app.service('h', function (restWsHelpers) {

var h = this;

Object.assign(this, basicHelpers);

this.getInstAppAccount = function (e) {
    return { institution: e.institution, app: e.appName, account: e.accountName };
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

    return restWsHelpers.action('post', 'accounts', { name: name, quota: 0 }).then(function () {
	return restWsHelpers.simple('accounts');
    }).then(function (accounts) {
	return h.array2hash(accounts, 'name')[name];
    });
};

});

