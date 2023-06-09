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

  <br />
  <div>
    <h1>Create Entity</h1>
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
        label="Primary keys"
        :rules="[{ required: true, message: 'Please input primary keys!' }]">
        <a-input v-model:value="formState.primaryKeys" />
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
      entities: [],

      loading: false,
      
      columns: [{
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: 'Primary Keys',
        dataIndex: 'primaryKeys',
        key: 'primaryKeys',
      },
      {
        title: 'Actions',
        key: 'actions',
        slots: { customRender: 'custom' },
      }],

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
          message.error(error.message);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    handleSubmit() {
      axios.post(`/api/entities`, {
        "name": this.formState.name,
        "primaryKeys": this.formState.primaryKeys
      })
      .then(response => {
        message.success(`Success to add entity ${this.formState.name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },

    handleDelete(name) {
      axios.delete(`/api/entities/${name}`)
      .then(response => {
        message.success(`Success to delete entity: ${name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },

  },
};
</script>