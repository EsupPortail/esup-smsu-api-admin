import * as h from '../basicHelpers.js'

const template = /*html*/`
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
        <component :is="columnComponents[field]" :row="row" :cell="row[field]"/>
    </td>
  </tr>
  </tbody>
</table>
`

export default {
    template,
    name: 'MyTable',
    props: ['data', 'columnDefs'],
    setup(props) {
        const columnComponents = Vue.computed(() => (
            h.simpleMap(props.columnDefs, (colDef) =>
                ({ props: ['row', 'cell'], template: colDef.cellTemplate || '{{cell}}' })
            )
        ))
        const sort = Vue.reactive({})
        const set_sort_field = (field) => {
            if (sort.field === field) {
                sort.direction *= -1
            } else {
                sort.field = field
                sort.direction = 1
            }
        }
        const sorted_data = Vue.computed(() => (
            sort.field ? props.data.sort((a,b) => {
                const v_a = a[sort.field]
                const v_b = b[sort.field]
                return sort.direction * (v_a < v_b ? -1 : v_a > v_b ? 1 : 0)
            }) : props.data
        ))
        return {
            sort, set_sort_field,
            sorted_data,
            columnComponents,
        }
    },
}