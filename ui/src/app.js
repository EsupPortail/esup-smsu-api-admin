import * as Vue from 'vue'
import router from './routes.js'
import Main from './components/Main.js'

window.Vue = Vue
window.$rootScope = Vue.reactive({
    allowLogout: false,
    loggedUser: undefined,
    impersonatedUser: undefined,
    roles: undefined,
    currentTab_text: {},
    idpId: globals.idpId,
})
const app = Vue.createApp(Main)
app.use(router)

const p = globals.test ? import('./test/appTest.js') : Promise.resolve()
p.then(_ => app.mount('#app'))
