import router from './routes.js'
import Main from './components/Main.js'

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

app.mount('#app')
