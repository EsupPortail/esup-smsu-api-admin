import * as Vue from 'vue'

export const $rootScope = Vue.reactive({
    allowLogout: false,
    loggedUser: undefined,
    impersonatedUser: undefined,
    roles: undefined,
    currentTab_text: {},
    idpId: globals.idpId,
})
