'use strict';

var appUrl = 'index.html';

function navigateTo(route) {
    browser().navigateTo(appUrl + (route ? '#'+route : ''));
}
function title() {
    return element('.breadcrumb li.active:visible').text();
}
function content() {
    return element('.content').text();
}
function url() {
    return browser().location().url();
}

describe('App', function() {
  it('should redirect to #/welcome', function() {
      navigateTo();
      expect(url()).toBe('/welcome');
  });
});

describe('Welcome view', function() {
    beforeEach(function() {
	navigateTo('/welcome');
    });

    it('should have a working navbar', function() {
	expect(title()).toBe('Accueil');
	expect(content()).toContain('Bienvenue dans');

	expect(repeater('.sidenav li').count()).toBe(7);
	
	var btn7 = element('.sidenav li:nth-child(7) a');
	expect(btn7.text()).toContain('A propos de');
	btn7.click();
	expect(url()).toBe('/about');
    });
});

describe('About view', function() {
    beforeEach(function() {
	navigateTo('/about');
    });
    it('should contain valid content', function () {
	expect(title()).toBe('A propos de');	  
	expect(content()).toContain('SMS-U est un service numérique');
    });
});

describe('Users view', function() {
    beforeEach(function() {
	navigateTo('/users');
    });
    it('should display users + button', function () {
	expect(title()).toBe('Gestion des utilisateurs');	  
	expect(element("a[href='#/users/new']").text()).toContain('Ajouter un utilisateur');
	expect(element("table a.ng-binding:first").text()).toBe('admin');
	expect(element("table a.ng-binding:first").attr('href')).toBe('#/users/1');
	expect(element("table tr.ng-scope:first td:nth-child(2)").text()).toBe('ROLE_SUPER_ADMIN');
    });
});

describe('Existing user view', function() {
    beforeEach(function() {
	navigateTo('/users/1');
    });
    it('should display test user and be editable', function () {
	expect(title()).toBe('admin');	  
	expect(element("a[ng-click='delete()']:visible").text()).toContain("Supprimer l'utilisateur");
	expect(input('user.login').val()).toBe("admin");
	expect(input('user.role').val()).toBe("ROLE_SUPER_ADMIN");

	input('user.login').enter('adminFoo');
	element("form button").click();
	expect(url()).toBe('/users');
	expect(element("table a.ng-binding:first").text()).toBe('adminFoo');

	element("table a.ng-binding:first").click();
	expect(url()).toBe('/users/1');
	expect(input('user.login').val()).toBe("adminFoo");
    });
    it('should delete user', function () {
	element("a[ng-click='delete()']").click();
	expect(url()).toBe('/users');
	expect(element("table a.ng-binding:first").text()).toBe('report');
    });
});

describe('New user view', function() {
    beforeEach(function() {
	navigateTo('/users/new');
    });
    it('should check validity of new user and create it', function () {
	expect(title()).toBe('Création');	  
	expect(element("a[ng-click='delete()']:visible").count()).toBe(0);
	expect(input('user.login').val()).toBe("");
	expect(input('user.role').val()).toBe("");

	var visibleErrors = "span.help-block:visible";
	expect(element(visibleErrors).count()).toBe(0);

	element("form button").click();

	expect(element(visibleErrors).count()).toBe(2);
	expect(element(visibleErrors + ":first").text()).toBe("Required");

	input('user.login').enter('manage');
	element("form button").click();
	expect(element(visibleErrors + ":first").text()).toBe("Already in use");

	input('user.login').enter('newUserFoo');
	expect(element(visibleErrors).count()).toBe(1);
	element("form button").click();
	expect(element(visibleErrors).count()).toBe(1);

	select("user.role").option("ROLE_REPORT");
	expect(element(visibleErrors).count()).toBe(0);

	element("form button").click();
	expect(url()).toBe('/users');
	expect(element("table a.ng-binding:last").text()).toBe('newUserFoo');
	element("table a.ng-binding:last").click();
	expect(url()).toBe('/users/6');
	expect(input('user.login').val()).toBe("newUserFoo");
	expect(input('user.role').val()).toBe("ROLE_REPORT");
    });
});

describe('Applications view', function() {
    beforeEach(function() {
	navigateTo('/applications');
    });
    it('should display apps + button', function () {
	expect(title()).toBe('Applications clientes');	  
	expect(element("a[href='#/applications/new']").text()).toContain('Ajouter une application');
	expect(element(".ngRow:first .ngCellText:first").text()).toBe('app');
	expect(element(".ngRow:first .ngCellText:first a").attr('href')).toBe('#/applications/2');
    });
});

describe('Existing application view', function() {
    beforeEach(function() {
	navigateTo('/applications/2');
    });
    it('should display test application and be editable', function () {
	expect(title()).toBe('app');	  
	expect(element("a[ng-click='delete()']:visible").text()).toContain("Supprimer l'application");
	expect(input('app.name').val()).toBe("app");
	expect(input('app.institution').val()).toBe("inst1");
	//expect(input('app.accountName').val()).toBe("0");
	expect(input('app.quota').val()).toBe("20000");
	expect(element('form input[name="password"]').count()).toBe(1);

	input('app.name').enter('app0');
	element("form button").click();
	expect(url()).toBe('/applications');
	expect(element(".ngRow:first .ngCellText:first").text()).toBe('app0');

	element(".ngRow:first .ngCellText:first a").click();
	expect(url()).toBe('/applications/2');
	expect(input('app.name').val()).toBe("app0");
    });
    it('should delete application', function () {
	element("a[ng-click='delete()']").click();
	expect(url()).toBe('/applications');
	expect(element(".ngRow:first .ngCellText:first").text()).toBe('app2');
    });
});

describe('Existing non deletable application view', function() {
    beforeEach(function() {
	navigateTo('/applications/4');
    });
    it('should display test application and be non-deletable', function () {
	expect(title()).toBe('app2');	  
	expect(element("a[ng-click='delete()']:visible").count()).toBe(0);
    });
});

describe('New application view', function() {
    beforeEach(function() {
	navigateTo('/applications/new');
    });
    it('should check validity of new application and create it', function () {
	expect(title()).toBe('Création');	  
	expect(element("a[ng-click='delete()']:visible").count()).toBe(0);
	expect(input('app.name').val()).toBe("");
	expect(input('app.password').val()).toBe("");

	element("form button").click();

	var visibleErrors = "span.help-block:visible";
	expect(element(visibleErrors).count()).toBe(3);
	expect(element(visibleErrors + ":first").text()).toBe("Required");

	input('app.name').enter('app2');
	element("form button").click();
	expect(element(visibleErrors + ":first").text()).toBe("Already in use");

	input('app.name').enter('app4');
	input('app.password').enter('xxx');
	input('app.institution').enter('inst1');
	element("form button").click();
	expect(url()).toBe('/accounts/5?isNew');

    });
});
