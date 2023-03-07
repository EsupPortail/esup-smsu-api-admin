import * as Vue from 'vue'
import router from './routes.js'
import Main from './components/Main.vue'

const p = globals.test ? import('./test/appTest.js') : Promise.resolve()
p.then(_ => Vue.createApp(Main).use(router).mount('#app'))
