<template>
<MyTable :data="accounts" :columnDefs="columnDefs">
    <template #name="{row, cell}">
        <router-link :to="'/accounts/' + row.id">{{cell}}</router-link>
    </template>
    <template #consumedSms="{row, cell}">
        <div :style="row.consumedSms / row.quota > warnConsumedRatio ? {color: '#f00'} : {}">{{cell}}</div>
    </template>
</MyTable>
</template>

<script lang="ts">
import MyTable from "./MyTable.vue"

export default { props: ['accounts'], components: { MyTable }, setup: function(_props) {
    return {
        warnConsumedRatio: 0.9,
	    columnDefs: {name: { displayName:"Compte d'imputation" }, 
					quota: { displayName: 'Quota'},
					consumedSms: { displayName: 'Nombre de SMS consommés' },
        },
    }
  }
}
</script>