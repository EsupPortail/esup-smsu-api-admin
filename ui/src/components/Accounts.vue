<template>
<MyTable :data="accounts" :columnDefs="columnDefs"/>
</template>

<script>
import MyTable from "./MyTable.vue"

export default { props: ['accounts'], components: { MyTable }, setup: function(props) {
    const warnConsumedRatio = 0.9;
    return {
	    columnDefs: {name: { displayName:"Compte d'imputation", 
					   cellTemplate: /*html*/`<router-link :to="'/accounts/' + row.id">{{cell}}</router-link>`},
					quota: { displayName: 'Quota'},
					consumedSms: { displayName: 'Nombre de SMS consommés',
					 cellTemplate: /*html*/`<div :style="row.consumedSms / row.quota > ${warnConsumedRatio} ? {color: '#f00'} : {}">{{cell}}</div>`
					}},
    }
  }
}
</script>