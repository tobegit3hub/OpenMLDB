<template>

<div>
  <br/>
  <h1>Feature Services</h1>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="featureServices" :loading="loading">
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
    <h1>Create Feature Service</h1>
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
        label="Feature list"
        :rules="[{ required: true, message: 'Please input feature list!' }]">
        <a-input v-model:value="formState.featureList" />
      </a-form-item>
      
      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">Submit</a-button>
      </a-form-item>
    </a-form>
  </div>

  <br />
  <div>
    <h1>Create From Deployment</h1>
    <!-- Create form deployment -->
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
        <a-button type="primary" html-type="submit">Submit</a-button>
      </a-form-item>
    </a-form>
  </div>

  <br />
  <div>
    <h1>Test Feature Service</h1>
    <p>Follow the <a target="_blank" href="https://openmldb.ai/docs/zh/main/quickstart/sdk/rest_api.html#id3">docs of OpenMLDB APIServer</a> to prepare test data. eg. {"input": [["abc", 123]]}</p>
    <!-- Test form -->
    <a-form
      :model="testFormState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      @submit="handleTestFormSubmit">
      <a-form-item
        label="Feature service"
        :rules="[{ required: true, message: 'Please input feature service name!' }]">
        <a-select id="itemSelect" v-model:value="testFormState.name">
          <option v-for="featureViewItem in featureServices" :value="featureViewItem.name">{{ featureViewItem.name }}</option>
        </a-select>
      </a-form-item>

      <a-form-item
        label="Test data"
        :rules="[{ required: true, message: 'Please input test data!' }]">
        <a-input v-model:value="testFormState.testData" aria-placeholder="sdfs"/>
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
      },
      {
        title: 'Feature List',
        dataIndex: 'featureList',
        key: 'featureList',
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
        slots: { customRender: 'custom' },
      }],

      formState: {
        name: '',
        featureList: '',
      },

      createFromDeploymentFormState: {
        name: '',
        db: '',
        deploymentName: ''
      },

      testFormState: {
        name: "",
        testData: "",
      }
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

    handleSubmit() {
      axios.post(`/api/featureservices`, {
        "name": this.formState.name,
        "featureList": this.formState.featureList
      })
      .then(response => {
        message.success(`Success to add feature service ${this.formState.name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
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

    handleTestFormSubmit() {
      axios.post(`/api/featureservices/${this.testFormState.name}/request`,
        JSON.parse(this.testFormState.testData)
      )
      .then(response => {
        message.success(`Success to request feature service ${this.testFormState.name}`);

        if (response.data.code == 0) {
          Modal.success({
            title: 'Request result',
            content: h('div', {}, [
              h('p', JSON.stringify(response.data.data)),
            ]),
            onOk() {},
          });
        } else {
          message.error(response.data.msg);
        }
      })
      .catch(error => {
        message.error(error.message);
      });
    },

  },
};
</script>