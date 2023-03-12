<template>
   <ul class="breadcrumb" v-if="currentTab">
     <li v-if="currentTab.path != '/welcome'" class="ifBottomNavbar">
       <router-link to="/">Accueil</router-link>
     </li>
     <li v-if="currentTab != currentMainTab" >
       <router-link :to="currentMainTab">{{currentMainTab.meta.mainText}}</router-link>
     </li>
     <li class="active">{{currentTab_text || currentTab.meta.mainText}}</li>
   </ul>
</template>

<script lang="ts">
import { computed } from "vue"
import { $rootScope } from '../globals.js'
import { currentRoutePath } from "../routes.js"

export default {
    props: ['currentTab', 'currentMainTab'],
    setup() {
        return { 
            currentTab_text: computed(() => $rootScope.currentTab_text[currentRoutePath()])
        }
    },
}
</script>