import * as h from "./basicHelpers.js"


export const getInstAppAccount = function (e) {
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

export const createEmptyAccount = function (restWsHelpers, accountNameSuggestion, accounts) {
    var name = findName(accountNameSuggestion, h.array2hash(accounts, 'name'));
    console.log("found free account name " + name);

    return restWsHelpers.action('post', 'accounts', { name: name, quota: 0 }).then(function () {
	return restWsHelpers.simple('accounts');
    }).then(function (accounts) {
	return h.array2hash(accounts, 'name')[name];
    });
};

