import { currentRoutePath } from "../routes.js"

const template = /*html*/`
   <ul class="breadcrumb" v-if="currentTab">
     <li v-if="currentTab.path != '/welcome'" class="ifBottomNavbar">
       <router-link to="/">Accueil</router-link>
     </li>
     <li v-if="currentTab != currentMainTab" >
       <router-link :to="currentMainTab">{{currentMainTab.meta.mainText}}</router-link>
     </li>
     <li class="active">{{currentTab_text || currentTab.meta.mainText}}</li>
   </ul>
`

export default {
    template,
    props: ['currentTab', 'currentMainTab'],
    setup() {
        return { 
            currentTab_text: Vue.computed(() => $rootScope.currentTab_text[currentRoutePath()])
        }
    },
}