<template>
<table class="table table-striped">
  <tr>
    <template v-for="(colDef, field) in columnDefs">
        <th style="cursor: pointer" @click="set_sort_field(field)">
            {{colDef.displayName}}
            <span v-if="field === sort.field">
                {{sort.direction > 0 ? "▴" : "▾"}}
            </span>
        </th>
    </template>
  </tr>
  <tbody>
  <tr v-for="row in sorted_data">
    <td v-for="(_, field) in columnDefs">
        <slot :name="field" :row="row" :cell="row[field]">
            {{ row[field] }}
        </slot>
    </td>
  </tr>
  </tbody>
</table>
</template>

<script lang="ts">
import { computed, reactive } from 'vue'

export default {
    props: ['data', 'columnDefs'],
    setup(props) {
        const sort = reactive({ field: undefined, direction: 1 })
        const set_sort_field = (field) => {
            if (sort.field === field) {
                sort.direction *= -1
            } else {
                sort.field = field
                sort.direction = 1
            }
        }
        const sorted_data = computed(() => (
            sort.field ? props.data.sort((a,b) => {
                const v_a = a[sort.field]
                const v_b = b[sort.field]
                return sort.direction * (v_a < v_b ? -1 : v_a > v_b ? 1 : 0)
            }) : props.data
        ))
        return {
            sort, set_sort_field,
            sorted_data,
        }
    },
}
</script>