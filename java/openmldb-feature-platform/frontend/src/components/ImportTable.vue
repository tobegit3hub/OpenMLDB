<template>

<div>
  <br />
  <div>
    <h1>{{ $t('Import Table') }}</h1>
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
        <a-button type="primary" html-type="submit">{{ $t('Submit') }}</a-button>
      </a-form-item>
    </a-form>
  </div>

  <a-button type="primary" @click="click_display_tables()">{{ $t('Display Tables') }}</a-button>

  <div v-if="isDisplayTable">
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

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';

export default {
  data() {
    return {
      isDisplayTable: false,

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

    click_display_tables() {
      if (this.isDisplayTable) {
        this.isDisplayTable = false;
      } else {
        this.isDisplayTable = true;
      }
    }

  },
};
</script>