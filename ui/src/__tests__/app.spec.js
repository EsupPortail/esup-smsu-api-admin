import { it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import './unit_test_init_globals.js'
import '../globals.js'
import '../test/appTest.js'

import router from '../routes.js'
import Main from '../components/Main.vue'

const wait = () => (
	new Promise(resolve => setTimeout(resolve, 1))
)

const wrapper = mount(Main, {
	global: {
		plugins: [router]
	}		 
})

function title() {
	return wrapper.get('.breadcrumb li.active').text();
}
function content() {
	return wrapper.get('.content').text();
}
function element(selector) {
	return wrapper.get(selector);
}
function url() {
	return router.currentRoute.value.fullPath
}	 

it('should redirect to #/welcome', async () => {
	await router.isReady()
	expect(url()).toBe('/welcome');	 
	expect(wrapper.text()).toContain('Bienvenue dans esup-smsu-api-admin')
})
it('should have a working navbar', async function() {
	expect(title()).toBe('Accueil');
	expect(content()).toMatch('Bienvenue dans');

	expect(wrapper.findAll('.sidenav li').length).toBe(7);
	
	var btn7 = element('.sidenav li:nth-child(7) a');
	expect(btn7.text()).toMatch('A propos de');
	btn7.trigger('click');
	await wait()

	expect(url()).toBe('/about');
});

it('/about should contain valid content', async function () {
	router.push({ path: '/about' });
	await wait()
	expect(title()).toBe('A propos de');	  
	expect(content()).toMatch('SMS-U est un service numérique');
});

it('should navigate to /users', async () => {
	router.push({ path: '/users' });
	await wait()
	expect(title()).toBe('Gestion des utilisateurs');	  
})
it('/users should display users + button', async function() {
	expect(title()).toBe('Gestion des utilisateurs');	  
	expect(element("a[href='#/users/new']").text()).toMatch('Ajouter un utilisateur');
	expect(element("table a").text()).toBe('admin');
	expect(element("table a").attributes('href')).toBe('#/users/1');
	expect(element("table tr td:nth-child(2)").text()).toBe('ROLE_SUPER_ADMIN');
});

it('should navigate to /users/1', async () => {
	router.push({ path: '/users/1' });
	await wait()
	expect(title()).toBe('admin');	  
})
it('/users should display test user and be editable', async function () {
	expect(element('input').element.value).toBe("admin");
	expect(element('select').element.value).toBe("ROLE_SUPER_ADMIN");

	element('input').setValue('adminFoo');
	element("form").trigger('submit');
	await wait()
	expect(url()).toBe('/users');
	expect(element("table a").text()).toBe('adminFoo');
	
	element("table a").trigger('click');
	await wait()
	expect(url()).toBe('/users/1');
	expect(element('input').element.value).toBe("adminFoo");
	return
});
it('/users should delete user', async function () {
	expect(element("a.btn-default").text()).toMatch("Supprimer l'utilisateur");
	element("a.btn-default").trigger('click');
	await wait()
	expect(url()).toBe('/users');
	expect(element("table a").text()).toBe('report');
});

it('should navigate to /users/new', async function() {
	router.push({ path: '/users/new' });
	await wait()
	expect(title()).toBe('Création');	  
});
it('should check validity of new user and create it', async function () {
	expect(wrapper.findAll(".normalContent a").length).toBe(0);
	expect(element('input').element.value).toBe("");
	expect(element('select').element.value).toBe("");

	const visibleErrors = "span.help-block";
	expect(wrapper.findAll(visibleErrors).length).toBe(0);

	element('input').setValue('manage');
	element("form").trigger('submit');
	await wait()
	expect(element(visibleErrors).text()).toBe("Already in use");

	element('input').setValue('newUserFoo');
	await wait()
	expect(wrapper.findAll(visibleErrors).length).toBe(0);

	element('select').setValue("ROLE_REPORT");
	element("form").trigger('submit');
	await wait()

	expect(url()).toBe('/users');

	const last_a = wrapper.findAll("table a").at(-1)
	expect(last_a.text()).toBe('newUserFoo');
	last_a.trigger('click');
	await wait()
	expect(url()).toBe('/users/6');
	expect(element('input').element.value).toBe("newUserFoo");
	expect(element('select').element.value).toBe("ROLE_REPORT");
});

it('should navigate to /applications', async function() {
	router.push({ path: '/applications' });
	await wait()
	expect(title()).toBe('Applications clientes');	  
});
it('/applications: should display apps + button', function () {
	expect(element("a.btn-default").text()).toMatch('Ajouter une application');
	expect(element("table a").text()).toBe('app');
	expect(element("table a").attributes('href')).toBe('#/applications/2');
});

it('should navigate to /applications/2', async function() {
	router.push({ path: '/applications/2' })
	await wait()
	expect(title()).toBe('app');	  
});
it('should display test application and be editable', async function () {
	expect(element("a.btn-default").text()).toMatch("Supprimer l'application");
	const [name, password, institution, quota] = wrapper.findAll('input')
	expect(name.element.value).toBe("app");
	expect(password.element.value).toBe("xxx");
	expect(institution.element.value).toBe("inst1");
	expect(quota.element.value).toBe("20000");

	name.setValue('app0');
	element("form").trigger('submit');
	await wait()
	expect(url()).toBe('/applications');
	expect(element("table a").text()).toBe('app0');

	element("table a").trigger('click');
	await wait()
	expect(url()).toBe('/applications/2');
	expect(element('input').element.value).toBe("app0");
});
it('should delete application', async function () {
	element("a.btn-default").trigger('click');
	await wait()
	expect(url()).toBe('/applications');
	expect(element("table a").text()).toBe('app2');
});

it('should navigate to existing non deletable application', async function() {
	router.push({ path: '/applications/4' })
	await wait()
	expect(title()).toBe('app2');	  
});
it('should display test application and be non-deletable', function () {
	expect(wrapper.findAll("a.btn-default").length).toBe(0);
});

it('should navigate to new application', async function() {
	router.push({ path: '/applications/new' })
	await wait()
	expect(title()).toBe('Création');	  
});
it('should check validity of new application and create it', async function () {
	expect(wrapper.findAll("a.btn-default").length).toBe(0);
	const [name, password, institution, _quota] = wrapper.findAll('input')
	expect(name.element.value).toBe("");
	expect(password.element.value).toBe("");

	name.setValue('app2');
	element("form").trigger('submit');
	await wait()
	const visibleErrors = "span.help-block";
	expect(element(visibleErrors).text()).toBe("Already in use");

	name.setValue('app4');
	password.setValue('xxx');
	institution.setValue('inst1');
	element("form").trigger('submit');
	await wait()
	expect(url()).toBe('/accounts/5?isNew');
});
