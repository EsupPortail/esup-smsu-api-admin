import * as h from '../basicHelpers.js'
import * as login from '../login.js'

console.log("appTest!")
    var loggedUser = {"login":"admin","role":"ROLE_SUPER_ADMIN"};
    login.fake.jsonp = function () { return Promise.resolve(loggedUser) }

    function create(list) {
	return function(_url, data) {
	    var o = JSON.parse(data);
	    o.id = 1 + Math.max.apply(null, list.map(function(o) { return o.id; }));
	    list.push(o);
	};
    }
    function modify(list) {
	return function(url, data) {
	    var id = url.match(/(\d+)$/)[0];
	    var o = list.find(function (o) { return o.id == id; });
	    Object.assign(o, JSON.parse(data));
	};
    }
    function delete_(list) {
	return function(url, _data) {
	    var id = url.match(/(\d+)$/)[0];
	    var list_ = list.filter(function (o) { return o.id != id; });
	    list.splice(0, 999, ...list_);
	};
    }

    var data = {
	roles: [{"id":"1","role":"ROLE_MANAGE","fonctions":["FCTN_GESTION_CPT_IMPUT","FCTN_API_CONFIG_APPLIS"]},
		{"id":"2","role":"ROLE_REPORT","fonctions":["FCTN_API_EDITION_RAPPORT"]},
		{"id":"3","role":"ROLE_SUPER_ADMIN","fonctions":["FCTN_API_EDITION_RAPPORT","FCTN_GESTION_CPT_IMPUT","FCTN_API_CONFIG_APPLIS","FCTN_MANAGE_USERS"]}],
	users: [{"id":"1","login":"admin","role":"ROLE_SUPER_ADMIN"},
		{"id":"2","login":"report","role":"ROLE_REPORT"},
		{"id":"5","login":"manage","role":"ROLE_MANAGE"}],
	applications: [{"id":2,"name":"app","password":"xxx","institution":"inst1","accountName":"acc1","quota":20000,"consumedSms":19000,"deletable":true},
		       {"id":4,"name":"app2","password":"xxx","institution":"inst1","accountName":"acc2","quota":40000,"deletable":false},
		       {"id":5,"name":"app3","password":"xxx","institution":"inst2","accountName":"acc3","quota":2,"deletable":true}],
	accounts: [{"id":1,"name":"acc1","quota":200,"consumedSms":167},
		   {"id":3,"name":"acc2","quota":600,"consumedSms":0},
		   {"id":4,"name":"acc3","quota":19550,"consumedSms":18548}]
    };

    let httpFake = []
    for(const kind of [ 'roles', 'users', 'applications', 'accounts' ]) {
	let list = data[kind];
	var path = "/rest/" + kind;
	httpFake.push({ method: 'GET', path, respond: () => list })
	httpFake.push({ method: 'PUT', path, respond: modify(list) })
	httpFake.push({ method: 'POST', path, respond: create(list) })
	httpFake.push({ method: 'DELETE', path, respond: delete_(list) })
    }


    var summary_consolidated_compact = [
	{"accountName":"cajun.rouge","appName":"cajun.rouge","institution":"BIU Cajun","stats":[["2012-11",11,9], ["2012-12",21,21]]},
	{"accountName":"sms.univ-terre2.fr","appName":"sms.univ-terre2.fr","institution":"Université de Terre 2","stats":[["2011-11",1,0], ["2011-12",5,0], ["2012-01",1,0], ["2012-06",3,0], ["2012-10",3,0], ["2012-11",5,0], ["2013-02",2,0]]},
	{"accountName":"ent.univ-rouge.fr","appName":"ent.univ-rouge.fr","institution":"Université Rouge","stats":[["2011-10",135,9], ["2011-11",1231,83], ["2011-12",120,4], ["2012-01",1719,40], ["2012-02",792,73], ["2012-03",854,52], ["2012-04",452,12], ["2012-05",43,17], ["2012-06",129,4], ["2012-07",55,5], ["2012-08",14,0], ["2012-09",809,125], ["2012-10",762,38], ["2012-11",927,110], ["2012-12",1748,432], ["2013-01",1767,279], ["2013-02",413,170], ["2013-03",2031,150], ["2013-04",943,72], ["2013-05",232,9], ["2013-06",1613,239], ["2013-07",14,11], ["2013-08",3,0]]},
	{"accountName":"mail2sms.cri.rouge","appName":"mail2sms.univ-rouge.fr","institution":"Université Rouge","stats":[["2011-10",418,53], ["2011-11",551,23], ["2011-12",682,14], ["2012-01",1242,10], ["2012-02",807,15], ["2012-03",1481,13], ["2012-04",804,8], ["2012-05",147,14], ["2012-06",640,13], ["2012-07",643,42], ["2012-08",888,28], ["2012-09",1227,77], ["2012-10",2211,31], ["2012-11",1875,35], ["2012-12",1558,13], ["2013-01",775,11], ["2013-02",543,16], ["2013-03",436,7], ["2013-04",429,12], ["2013-05",498,8], ["2013-06",269,8], ["2013-07",398,24], ["2011-12",19,0]]},
	{"accountName":"mail2sms.unr.rouge","appName":"mail2sms.univ-rouge.fr","institution":"Université Rouge","stats":[["2012-11",5,2], ["2012-12",6,0], ["2013-01",3,0], ["2013-02",2,0], ["2013-03",1,0], ["2013-04",6,0], ["2013-05",1,0], ["2013-06",8,0], ["2013-07",13,0], ["2013-08",10,0]]},
	{"accountName":"mail2sms.urtye.fr","appName":"mail2sms.univ-rouge.fr","institution":"Université Rouge","stats":[["2012-02",2,0], ["2012-05",2,2], ["2012-06",1,1], ["2012-07",1,1], ["2012-09",1,0], ["2013-01",2,0]]},
	{"accountName":"test-univ-rouge.fr","appName":"ent-test","institution":"Université Rouge","stats":[["2013-04",14,0], ["2013-05",6,0]]}
    ];
    function summary_consolidated() {
	var r = [];
	for (const e of summary_consolidated_compact) {
	    for (const stat of e.stats) {
		var e_ = h.cloneDeep(e);
		delete e_.stats;
		e_.month = stat[0];
		e_.nbSendedSMS = stat[1];
		e_.nbSMSInError = stat[2];
		r.push(e_);
	    }
	}
	return r;
    }
    httpFake.push({ method: 'GET', path: '/rest/summary/consolidated', respond: summary_consolidated });


    var summary_detailed_base = [
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380184391000,"nbDelivered":23,"nbInProgress":3,"errors":"1","nbSms":27},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380181158000,"nbDelivered":18,"nbInProgress":3,"errors":"0","nbSms":21},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380122812000,"nbDelivered":8,"nbInProgress":1,"errors":"0","nbSms":9},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380122155000,"nbDelivered":1,"nbInProgress":0,"errors":"0","nbSms":1},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380119245000,"nbDelivered":12,"nbInProgress":0,"errors":"0","nbSms":12},
	{"institution":"Université Rouge","appName":"mail2sms.univ-rouge.fr","accountName":"mail2sms.cri.rouge","date":1380118059000,"nbDelivered":1,"nbInProgress":0,"errors":"0","nbSms":1},
	{"institution":"Université Rouge","appName":"mail2sms.univ-rouge.fr","accountName":"mail2sms.cri.rouge","date":1380107593000,"nbDelivered":3,"nbInProgress":0,"errors":"0","nbSms":3},
	{"institution":"Université Rouge","appName":"mail2sms.univ-rouge.fr","accountName":"mail2sms.unr.rouge","date":1380107411000,"nbDelivered":3,"nbInProgress":0,"errors":"0","nbSms":3},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380103728000,"nbDelivered":2,"nbInProgress":0,"errors":"0","nbSms":2},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380103418000,"nbDelivered":3,"nbInProgress":0,"errors":"0","nbSms":3},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380103164000,"nbDelivered":3,"nbInProgress":0,"errors":"0","nbSms":3},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380102671000,"nbDelivered":3,"nbInProgress":0,"errors":"0","nbSms":3},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380102163000,"nbDelivered":1,"nbInProgress":0,"errors":"0","nbSms":1},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380101835000,"nbDelivered":21,"nbInProgress":1,"errors":"0","nbSms":22},
	{"institution":"Université Rouge","appName":"ent.univ-rouge.fr","accountName":"ent.univ-rouge.fr","date":1380099148000,"nbDelivered":2,"nbInProgress":1,"errors":"0","nbSms":3},
    ];
    function summary_detailed_criteria(_url, _data) {
	var r = summary_detailed_base.map(function (e) {
	    return h.objectSlice(e, ["institution", "appName", "accountName"]);
	});
	r = h.uniqWith(r, JSON.stringify);
	return r;
    }
    function summary_detailed(url, _data) {
	var search = new URLSearchParams(new URL(url, document.location.href).search)

	var filteredBase = summary_detailed_base.filter(function (e) {
	    return !(search.has('account') && search.get('account') !== e.accountName) &&
		   !(search.has('app') && search.get('app') !== e.appName) &&
		   !(search.has('institution') && search.get('institution') !== e.institution);
	});
	var nbBase = filteredBase.length;
	var nbResults = nbBase < 5 ? nbBase : Math.min(parseInt(search.get('maxResults')), 80);
	var r = [];
	for (var i = 0; i < nbResults; i++) {
	    r.push(h.cloneDeep(filteredBase[i % nbBase]));
	}
	return r;
    }
    httpFake.push({ method: 'GET', path: '/rest/summary/detailed/criteria', respond: summary_detailed_criteria });
    httpFake.push({ method: 'GET', path: '/rest/summary/detailed', respond: summary_detailed });

    let real_fetch = window.fetch
    window.fetch = function (url, args) {
        // @ts-ignore
        const { pathname } = new URL(url, document.location)
        const fake = httpFake.find(fake => (
            args.method.toUpperCase() === fake.method && pathname.startsWith(fake.path)
        ))
        if (!fake) return real_fetch(url, args)
        let response = fake.respond(url, args.body)
        return Promise.resolve({ 
            status: 200,
            // @ts-ignore
            headers: {
                get: _ => "application/json",
            },
            json: () => Promise.resolve(response) })
    }




