import router, { findParentTab } from '../routes.js'
import * as restWsHelpers from '../restWsHelpers.js'
import BreadcrumbInc from './BreadcrumbInc.js'
import WelcomeInc from './WelcomeInc.js'

const template = /*html*/`
<div class="container-fluid" v-if="loggedUser">
<div class="row">

 <div class="page col-sm-12 ifBottomNavbar" v-if="forceSidenav">
   <!-- only displayed on "welcome" if small devices -->
   <BreadcrumbInc :currentTab="currentTab" :currentMainTab="currentMainTab"/>
   <WelcomeInc/>
 </div>

 <div class="col-md-3 sidenavDiv">
  <ul class="sidenav nav nav-stacked" :class="{ifSideNavbar: !forceSidenav}">
	<li v-for="tab in mainVisibleTabs" :class="{active: tab == currentMainTab}">
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
`


export default {
  template,
  components: { BreadcrumbInc, WelcomeInc },
  props: [],
  setup(_props) {
    restWsHelpers.initialLogin();
    return {
        loggedUser: Vue.computed(() => $rootScope.loggedUser),
        currentTab: router.currentRoute,
        currentMainTab: Vue.computed(findParentTab),
        mainVisibleTabs: Vue.computed(() => (
            router.getRoutes().filter(e => (
                e.meta.mainText && (!e.meta.show || e.meta.show())
            )) 
        )),
        forceSidenav: Vue.computed(() => router.currentRoute.value?.path === "/welcome"),
        loadInProgress: Vue.ref(false),
    }
  }
}