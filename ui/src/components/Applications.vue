/*
<style>
.ngHeaderText {
  white-space: normal;
}
.gridStyle.loading {
  opacity: 0.5;
}
</style>
*/
<template>
<div class="normalContent">
   <router-link class="btn btn-default" to="/applications/new"><span class="glyphicon glyphicon-plus"></span> Ajouter une application</router-link>
</div>

<p></p>

<MyTable :data="applications" :columnDefs="columnDefs">
    <template #name="{row, cell}">
        <router-link :to="'/applications/' + row.id">{{cell}}</router-link>
    </template>
    <template #consumedSms="{row, cell}">
        <div :style="row.consumedSms / row.quota > warnConsumedRatio ? {color: '#f00'} : {}">{{cell}}</div>
    </template>
</MyTable>
</template>

<script lang="ts">
import MyTable from "./MyTable.vue"

export default { props: ['applications'], components: { MyTable }, setup: function(_props) {
    return {
        warnConsumedRatio: 0.9,
        columnDefs: {name: { displayName:"Application" },
					quota: {displayName: 'Quota'},
					consumedSms: {displayName: 'Nombre de \nSMS consommés'},
					},
    }
  }
}
</script>