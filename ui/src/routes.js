import * as VueRouter from 'vue-router'
import * as restWsHelpers from './restWsHelpers.js'
import About from './components/About.vue'
import AccountsDetail from './components/AccountsDetail.vue'
import Accounts from './components/Accounts.vue'
import ApplicationsDetail from './components/ApplicationsDetail.vue'
import Applications from './components/Applications.vue'
import ConsolidatedSummary from './components/ConsolidatedSummary.vue'
import DetailedSummary from './components/DetailedSummary.vue'
import UsersDetail from './components/UsersDetail.vue'
import Users from './components/Users.vue'
import Welcome from './components/Welcome.vue'
import { $rootScope } from './globals.js'

const loggedUser_can = (fctn) => (
    () => $rootScope.loggedUser?.can?.[fctn]
)

const routes = 
    [{ path: '/welcome', component: Welcome, meta: { mainText: "Accueil" } }, 
     { path: '/applications', component: Applications, meta: { mainText: "Applications clientes", show: loggedUser_can('FCTN_API_CONFIG_APPLIS'), get_props: ['applications'] } },
     { path: '/accounts', component: Accounts, meta: { mainText: "Comptes d'imputation", show: loggedUser_can('FCTN_GESTION_CPT_IMPUT'), get_props: ['accounts'] } },
     { path: '/consolidatedSummary', component: ConsolidatedSummary, meta: { mainText: "Relevé consolidé", show: loggedUser_can('FCTN_API_EDITION_RAPPORT'), get_props: ['summary/consolidated'] } },
     { path: '/detailedSummary', component: DetailedSummary, meta: { mainText: "Relevé détaillé", show: loggedUser_can('FCTN_API_EDITION_RAPPORT'), get_props: ['summary/detailed/criteria'] } },
     { path: '/users', component: Users, meta: { mainText: "Gestion des utilisateurs", show: loggedUser_can('FCTN_MANAGE_USERS'), get_props: ['users'] } },
     { path: '/logout', component: About, meta: { mainText: "Déconnexion", show: () => $rootScope.allowLogout } },
     { path: '/about', component: About, meta: { mainText: "A propos de" } },

     { path: '/users/:id', component: UsersDetail, meta: { parent: '/users', get_props: ['users'] } },
     { path: '/applications/:id', component: ApplicationsDetail, meta: { parent: '/applications', get_props: ['accounts','applications'] } },
     { path: '/accounts/:id', component: AccountsDetail, meta: { parent: '/accounts', get_props: ['accounts'] } }];

for (const route of routes) {
    if (route.meta.get_props) {
        route.props = route => ({ ...route.meta.resolved_props, ...route.params })
    }
}

let router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    //history: globals.test ? VueRouter.createWebHashHistory() : VueRouter.createWebHistory(),
    routes
})

router.addRoute({ path: '/:unused(.*)', redirect: '/welcome' })

for (const route of routes) {
    if (route.meta.get_props) {
        router.beforeEach(async (to) => {
            if (to.matched[0].path === route.path) {
                let r = {}
                for (const $function of route.meta.get_props) {
                    const name = $function.replace(/[/]/g, '_') //replaceAll('/', '_')
                    r[name] = await restWsHelpers.simple($function)
                }
                to.meta.resolved_props = r
            }
        })
    }
}

export function findParentTab() {
    var tab = router.currentRoute.value;
    return tab?.meta.parent ? routes.find(mainTab => mainTab.path === tab.meta.parent) : tab
}

export const hash_params = () => {
    const { hash } = router.currentRoute.value
    return restWsHelpers.parse_querystring(hash.replace(/^#/, ''));
}

export const currentRoutePath = () => (
    router.currentRoute.value.matched[0]?.path
)

export default router
  