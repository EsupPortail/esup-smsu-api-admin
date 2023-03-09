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

<MyTable :data="applications" :columnDefs="columnDefs"/>
</template>

<script>
import MyTable from "./MyTable.vue"

export default { props: ['applications'], components: { MyTable }, setup: function(_props) {
    const warnConsumedRatio = 0.9;
	return {
        columnDefs: {name: { displayName:"Application", 
					   cellTemplate: /*html*/`<router-link :to="'/applications/' + row.id">{{cell}}</router-link>`},
					quota: {displayName: 'Quota'},
					consumedSms: {displayName: 'Nombre de \nSMS consommés',
					 cellTemplate: /*html*/`<div :style="row.consumedSms / row.quota > ${warnConsumedRatio} ? {color: '#f00'} : {}">{{cell}}</div>`
					}},
    }
  }
}
</script>