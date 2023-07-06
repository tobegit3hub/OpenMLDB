<template>

<div>
  <br/>
  <h1>
    {{ $t('Feature Services') }}
    &nbsp;&nbsp;<a-button type="primary"><router-link to='/featureservices/deploy'>{{ $t('Create Service') }}</router-link></a-button>
  </h1>

  <br/>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="featureServices" :loading="loading">
    <template #name="{ text, record }">
      <router-link :to="`/featureservices/${record.name}`">{{ text }}</router-link>
    </template>
    <template #db="{ text, record }">
      <router-link :to="`/databases/${record.db}`">{{ text }}</router-link>
    </template>  
    <!-- The delete column-->
    <template v-slot:action="scope">
      <a-button type="primary"><router-link :to="`/featureservices/test?featureservice=${scope.record.name}`">{{ $t('Test Service') }}</router-link></a-button>
      <br/>
      <a-popconfirm
          title="Sure to delete?"
          @confirm="handleDelete(scope.record.name)">
        <a-button type="primary" danger>{{ $t('Delete Service') }}</a-button>
      </a-popconfirm>
    </template>
  </a-table>

  <!-- Support create from deployment in other page
  <br />
  <div>
    <h1>{{ $t('Create') }} {{ $t('From') }} {{ $t('Deployment') }}</h1>
    <a-form
      :model="createFromDeploymentFormState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      @submit="handleCreateFromDeploymentSubmit">
      <a-form-item
        label="Name"
        :rules="[{ required: true, message: 'Please input name!' }]">
        <a-input v-model:value="createFromDeploymentFormState.name" />
      </a-form-item>

      <a-form-item
        label="Database"
        :rules="[{ required: true, message: 'Please input database!' }]">
        <a-input v-model:value="createFromDeploymentFormState.db" />
      </a-form-item>
      
      <a-form-item
        label="Deployment"
        :rules="[{ required: true, message: 'Please input deployment!' }]">
        <a-input v-model:value="createFromDeploymentFormState.deploymentName" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">{{ $t('Submit') }}</a-button>
      </a-form-item>
    </a-form>
  </div>
  -->

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';
import { Modal } from 'ant-design-vue';
import { h } from 'vue';

export default {
  data() {
    return {
      featureServices: [],

      loading: false,
      
      columns: [{
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        slots: { customRender: 'name' }
      },
      {
        title: 'Feature List',
        dataIndex: 'featureList',
        key: 'featureList',
      },
      {
        title: 'Database',
        dataIndex: 'db',
        key: 'db',
        slots: { customRender: 'db' }
      },
      {
        title: 'SQL',
        dataIndex: 'sql',
        key: 'sql',
      },
      {
        title: 'Deployment',
        dataIndex: 'deployment',
        key: 'deployment',
      },
      {
        title: 'Actions',
        key: 'actions',
        slots: { customRender: 'action' },
      }],

      createFromDeploymentFormState: {
        name: '',
        db: '',
        deploymentName: ''
      },
    };
  },

  mounted() {
    this.initData();

  },

  methods: {
    initData() {
      this.loading = true;
      axios.get(`/api/featureservices`)
        .then(response => {
          this.featureServices = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    handleCreateFromDeploymentSubmit() {
      axios.post(`/api/featureservices/deployments`, {
        "name": this.createFromDeploymentFormState.name,
        "db": this.createFromDeploymentFormState.featureList,
        "deploymentName": this.createFromDeploymentFormState.deploymentName
      })
      .then(response => {
        message.success(`Success to add feature service ${this.formState.name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },

    handleDelete(name) {
      axios.delete(`/api/featureservices/${name}`)
      .then(response => {
        message.success(`Success to delete feature service: ${name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },

  },
};
</script>