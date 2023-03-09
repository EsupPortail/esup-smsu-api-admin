<template>
<div class="normalContent">
   <a class="btn btn-default" href="#/users/new"><span class="glyphicon glyphicon-plus"></span> Ajouter un utilisateur</a>
</div>

<p></p>

<div>
  <table class="table table-striped">
    <thead><tr><th>Nom</th><th>Role</th></tr></thead>
    <tbody>
    <tr v-for="user in users">
      <td><router-link :to="'/users/' + user.id">{{user.login}}</router-link></td>
      <td>{{user.role}}</td>
    </tr>
    </tbody>
  </table>
</div>
</template>

<script lang="ts">
export default { props: ['users'] }
</script>