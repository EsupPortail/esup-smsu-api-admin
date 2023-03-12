<template>
<div class="container-fluid" v-if="loggedUser">
<div class="row">

 <div class="page col-sm-12 ifBottomNavbar" v-if="forceSidenav">
   <!-- only displayed on "welcome" if small devices -->
   <BreadcrumbInc :currentTab="currentTab" :currentMainTab="currentMainTab"/>
   <WelcomeInc/>
 </div>

 <div class="col-md-3 sidenavDiv">
  <ul class="sidenav nav nav-stacked" :class="{ifSideNavbar: !forceSidenav}">
	<li v-for="tab in mainVisibleTabs" :class="
        // @ts-ignore
        {active: tab == currentMainTab}">
	   <router-link :to="tab">
	     <span class="glyphicon glyphicon-chevron-right"></span>
	     {{tab.meta.mainText}}
	   </router-link>
	</li>
  </ul>
 </div>

 <div class="page col-md-9">

   <BreadcrumbInc :currentTab="currentTab" :currentMainTab="currentMainTab"
	:class="{ifSideNavbar: forceSidenav}"/>

   <div class="content" :class="{loadInProgress: loadInProgress}">
     <router-view/>
   </div>
 </div>
</div>
</div>
</template>

<script lang="ts">
import { computed, ref } from 'vue'
import router, { findParentTab } from '../routes.js'
import * as restWsHelpers from '../restWsHelpers.js'
import BreadcrumbInc from './BreadcrumbInc.vue'
import WelcomeInc from './WelcomeInc.vue'
import { $rootScope } from '../globals.js'

export default {
  components: { BreadcrumbInc, WelcomeInc },
  props: [],
  setup(_props) {
    restWsHelpers.initialLogin();
    return {
        loggedUser: computed(() => $rootScope.loggedUser),
        currentTab: router.currentRoute,
        currentMainTab: computed(findParentTab),
        mainVisibleTabs: computed(() => (
            router.getRoutes().filter(e => (
                // @ts-ignore
                e.meta.mainText && (!e.meta.show || e.meta.show())
            )) 
        )),
        forceSidenav: computed(() => router.currentRoute.value?.path === "/welcome"),
        loadInProgress: ref(false),
    }
  }
}
</script>