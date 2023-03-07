export const template = /*html*/`
<div class="normalContent" v-if="account">
 <div v-if="isNew" class="alert alert-success">Le nouveau compte « {{account.name}} » vient d'être créé, vous pouvez maintenant le modifier.</div>

 <a class="btn btn-default" v-if="!appDisabled" @click="disable()"><span class="glyphicon glyphicon-remove"></span> Désactiver le compte</a>

 <h4 style="margin-top: 2em">Modifier</h4>

 <form @submit.prevent="submitted = 1; submit()" class="form-horizontal">

  <div class="form-group" :class="{'has-error': submitted && !name_unique}">
    <label class="col-md-3 control-label" for="name">Nom</label>
    <div class="col-md-6">
      <input type="text" name="name" v-model="account.name" required maxlength="30" class="form-control">
      <span v-if="submitted && !name_unique" class="help-block">Already in use</span>
    </div>
  </div>

  <div class="form-group">
    <label class="col-md-3 control-label" for="quota">Quota</label>
    <div class="col-md-6">
      <input type="number" name="quota" v-model="account.quota" min="0" integer class="form-control">
    </div>
  </div>

  <div class="form-group">
    <div class="col-md-offset-3 col-md-6">
      <button class="btn btn-primary" type="submit">
	<span class="glyphicon" :class="{'glyphicon-pencil': !isNew}"></span>
	{{isNew && "Créer" || "Enregistrer"}}</button>
    </div>
  </div>

 </form>

</div>
`

import * as Vue from 'vue'
import * as h from "../basicHelpers.js"
import { $rootScope } from '../globals.js'
import * as restWsHelpers from '../restWsHelpers.js'
import router, { currentRoutePath } from '../routes.js'

export default { template, name: 'AccountsDetail', props: ['accounts', 'id'], setup: function(props) {
    let $scope = Vue.reactive({
        account: undefined,
        submitted: false,
    })
    const isNew = Vue.computed(() => 'isNew' in router.currentRoute.value.query)

    const our_path = currentRoutePath() // we want it to be static
    Vue.watchEffect(() => {
        $rootScope.currentTab_text[our_path] = $scope.account?.name || (isNew.value ? 'Création' : 'Modification');
    });

    let orig_account = Vue.computed(() => props.accounts.find(account => account.id == props.id))
    let name2account = Vue.computed(() => h.array2hash(props.accounts, 'name'))
    const checkUnique = function (name) {
	var account = name2account.value[name];
	return !account || name === orig_account.value?.name;
    };
    const name_unique = Vue.computed(() => checkUnique($scope.account?.name))

    var modify = function (method) {
	var account = h.cloneDeep($scope.account);
	restWsHelpers.action(method, 'accounts', account).then(function () {
	    router.push({ path: '/accounts' });
	});
    };    
    const submit = function () {
        if (!name_unique.value) return;
	modify('put');
    };
    const disable = function () {
	$scope.account.quota = 0;
	modify('put');	
    };

    Vue.watchEffect(() => {
        if (orig_account.value) {
            $scope.account = { ...orig_account.value }
	} else {
            alert("invalid account " + props.id);
	}
    })
    const appDisabled = Vue.computed(() => $scope.account?.quota == 0)
    return { ...Vue.toRefs($scope), isNew, name_unique, submit, disable, appDisabled }
  }
}
