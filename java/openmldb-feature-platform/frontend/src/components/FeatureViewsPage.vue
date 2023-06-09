<template>

<div>
  <br/>
  <h1>Feature Views</h1>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="featureViews" :loading="loading">
    <!-- The delete column-->
    <template v-slot:custom="scope">
      <a-popconfirm
          title="Sure to delete?"
          @confirm="handleDelete(scope.record.name)">
        <a>Delete</a>
      </a-popconfirm>
    </template>
  </a-table>

  <br />
  <div>
    <h1>Create Feature View</h1>
    <!-- Create form -->
    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      @submit="handleSubmit">
      <a-form-item
        label="Name"
        :rules="[{ required: true, message: 'Please input name!' }]">
        <a-input v-model:value="formState.name" />
      </a-form-item>

      <a-form-item
        label="Entity names"
        :rules="[{ required: true, message: 'Please input entity names!' }]">
        <a-input v-model:value="formState.entityNames" />
      </a-form-item>

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
      featureViews: [],

      loading: false,
      
      columns: [{
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: 'Entities',
        dataIndex: 'entityNames',
        key: 'entityNames',
      },
      {
        title: 'SQL',
        dataIndex: 'sql',
        key: 'sql',
      },
      {
        title: 'Features',
        dataIndex: 'featureNames',
        key: 'featureNames',
      },
      {
        title: 'Actions',
        key: 'actions',
        slots: { customRender: 'custom' },
      }],

      formState: {
        name: '',
        entityNames: '',
        sql: ''
      }
    };
  },

  mounted() {
    this.initData();

  },

  methods: {
    initData() {
      this.loading = true;
      axios.get(`/api/featureviews`)
        .then(response => {
          this.featureViews = response.data;
        })
        .catch(error => {
          message.error(error);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    handleSubmit() {
      axios.post(`/api/featureviews`, {
        "name": this.formState.name,
        "entityNames": this.formState.entityNames,
        "sql": this.formState.sql
      })
      .then(response => {
        message.success(`Success to add feature view ${this.formState.name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error);
      });
    },

    handleDelete(name) {
      axios.delete(`/api/featureviews/${name}`)
      .then(response => {
        message.success(`Success to delete feature view: ${name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error);
      });
    },

  },
};
</script>