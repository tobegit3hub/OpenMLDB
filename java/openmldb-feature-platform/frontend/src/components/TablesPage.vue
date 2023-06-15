<template>

<div>
  <br/>
  <h1>Tables</h1>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="tables" :loading="loading">
    <template #table="{ text, record }">
      <router-link :to="`/tables/${record.db}/${record.table}`">{{ text }}</router-link>
    </template>
  </a-table>

  <br />
  <div>
    <h1>Update Tables</h1>
    <a-typography>
      <a-typography-paragraph>
        <p>Use SQL to create or delete the databases or tables.</p>
        <p>eg. CREATE DATABASE IF NOT EXISTS db1</p>
        <p>eg. CREATE TABLE db1.user (name varchar, age int)</p>
        <p>eg. DROP TABLE db1.user</p>
      </a-typography-paragraph>
    </a-typography>
    <!-- Create form -->
    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      @submit="handleSubmit">
      <a-form-item
        label="SQL"
        :rules="[{ required: true, message: 'Please input SQL!' }]">
        <a-input v-model:value="formState.sql" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">Submit</a-button>
      </a-form-item>
    </a-form>
  </div>

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';

export default {
  data() {
    return {
      tables: [],

      loading: false,
      
      columns: [{
        title: 'Database',
        dataIndex: 'db',
        key: 'db'
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
      }],

      formState: {
        sql: '',
      }
    };
  },

  mounted() {
    this.initData();
  },

  methods: {
    initData() {
      this.loading = true;
      axios.get(`/api/tables`)
        .then(response => {
          this.tables = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    handleSubmit() {
      axios.post(`/api/sql/execute`, {
        "sql": this.formState.sql,
      })
      .then(response => {
        message.success(`Success to execute SQL: ${this.formState.sql}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },
  },
};
</script>