import * as h from "../basicHelpers.js"
import * as restWsHelpers from '../restWsHelpers.js'
import router, { currentRoutePath } from '../routes.js'

export const template = /*html*/`
<div class="normalContent" v-if="user">

 <a class="btn btn-default" v-if="!user.isNew" @click="deleteUser()"><span class="glyphicon glyphicon-remove"></span> Supprimer l'utilisateur</a>

 <h4 style="margin-top: 2em" v-if="!user.isNew">Modifier</h4>

 <form @submit.prevent="submitted = 1; submit()" class="form-horizontal">

  <div class="form-group" :class="{'has-error': submitted && !login_unique}">
    <label class="col-md-3 control-label" for="name">Nom</label>
    <div class="col-md-6">
      <input type="text" name="name" v-model="user.login" required maxlength="30" class="form-control">
      <span v-if="submitted && !login_unique" class="help-block">Already in use</span>
    </div>
  </div>

  <div class="form-group">
    <label class="col-md-3 control-label" for="role">Rôle</label>
    <div class="col-md-6">
      <select required name="role" v-model="user.role" class="form-control">
	<option value="">Sélectionnez un rôle</option>
	<option v-for="(_, role) in roles" :value="role">{{role}}</option>
      </select>
    </div>
  </div>

  <div class="form-group">
    <div class="col-md-offset-3 col-md-6">
      <button class="btn btn-primary" type="submit">
	<span class="glyphicon" :class="{'glyphicon-plus': user.isNew, 'glyphicon-pencil': !user.isNew}"></span>
	{{user.isNew && "Créer" || "Enregistrer"}}</button>
    </div>
  </div>

 </form>

</div>
`

export default { template, name: 'UsersDetail', props: ['users', 'id'], setup: function(props) {
    let $scope = Vue.reactive({
        roles: Vue.ref($rootScope.roles),
        user: undefined,
        submitted: false,
    })
    const our_path = currentRoutePath() // we want it to be static
    Vue.watchEffect(() => {
	$rootScope.currentTab_text[our_path] = ($scope.user?.login || (props.id === 'new' ? 'Création' : 'Modification'));
    console.log('usersdetail', JSON.stringify($rootScope.currentTab_text))
    });

    let orig_user = Vue.computed(() => props.users.find(user => user.id == props.id))
    let login2user = Vue.computed(() => h.array2hash(props.users, 'login'))
    const checkUnique = function (login) {
	var user = login2user.value[login];
	return !user || login === orig_user.value?.login;
    };
    const login_unique = Vue.computed(() => checkUnique($scope.user?.login))

    var modify = function (method) {
	var user = h.objectSlice($scope.user, ['id', 'login', 'role']) // keep only modifiable fields
	restWsHelpers.action(method, 'users', user).then(function () {
	    router.push({ path: '/users' });
	});
    };
    
    const submit = function () {
	if (!login_unique.value) return;
	modify($scope.user.isNew ? 'post' : 'put');
    };
    const deleteUser = function () {
	modify('delete');
    };

    Vue.watchEffect(() => {
    if (props.id === "new") {
	    $scope.user = { isNew: true, role: '' };
    } else if (orig_user.value) {
	    $scope.user = { ...orig_user.value };
    } else {
	    alert("invalid user " + props.id);
	    router.push({ path: '/users' });
    }
    }) 
    return { ...Vue.toRefs($scope), login_unique, submit, deleteUser }
  }
}