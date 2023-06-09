<template>

<div>
  <br/>
  <h1>Entities</h1>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="entities" :loading="loading">
    <!-- The delete column-->
    <template v-slot:custom="scope">
      <a-popconfirm
          title="Sure to delete?"
          @confirm="handleDelete(scope.record.name)">
        <a>Delete</a>
      </a-popconfirm>
    </template>
  </a-table>

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';

  export default {
    data() {
      return {
        entities: [],
        loading: false,
        columns: [{
          title: 'Name',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: 'PrimaryKeys',
          dataIndex: 'primaryKeys',
          key: 'primaryKeys',
        },
        {
          title: 'Actions',
          key: 'actions',
          slots: { customRender: 'custom' },
        },],
        formState: {
          name: '',
          primaryKeys: '',
        }
      };
    },
    mounted() {
      this.initData();
  
    },
    methods: {
      initData() {
        this.loading = true;
        axios.get(`/api/entities`)
          .then(response => {
            this.entities = response.data;
          })
          .catch(error => {
            console.log(error.message);
          })
          .finally(() => {
            this.loading = false;
          });
      },
    },
  };
</script>
  