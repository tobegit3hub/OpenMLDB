<template>

<div>
  <br/>
  <a-button type="primary"><router-link to='/tables/import'>{{ $t('Import Table') }}</router-link></a-button>

  <br/>
  <br/>
  <h1>{{ $t('Databases') }}</h1>
  <!-- Databases table -->
  <a-table :columns="databaseColumns" :data-source="databases">
    <template #database="{ text, record }">
      <router-link :to="`/databases/${record}`">{{ text }}</router-link>
    </template>
  </a-table>

  <h1>{{ $t('Data Tables') }}</h1>
  <!-- Tables table -->
  <a-table :columns="columns" :data-source="tables">
    <template #database="{ text, record }">
      <router-link :to="`/databases/${record.db}`">{{ text }}</router-link>
    </template>    
    <template #table="{ text, record }">
      <router-link :to="`/tables/${record.db}/${record.table}`">{{ text }}</router-link>
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
      databases: [],

      databaseColumns: [{
        title: 'Database',
        slots: { customRender: 'database' }
      }],

      tables: [],
      
      columns: [{
        title: 'Database',
        dataIndex: 'db',
        key: 'db',
        slots: { customRender: 'database' }
      },
      {
        title: 'Table',
        dataIndex: 'table',
        key: 'table',
        slots: { customRender: 'table' }
      },
      {
        title: 'Schema',
        dataIndex: 'schema',
        key: 'schema',
      }]

    };
  },

  mounted() {
    this.initData();
  },

  methods: {
    initData() {
      axios.get(`/api/databases`)
        .then(response => {
          this.databases = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {});

      axios.get(`/api/tables`)
        .then(response => {
          this.tables = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {});
    },

  },
};
</script>