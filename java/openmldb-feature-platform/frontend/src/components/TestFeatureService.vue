<template>

<div>
  
  <br />
  <div>
    <h1>{{ $t('Test') }} {{ $t('Feature Service') }}</h1>
    <p>Follow the <a target="_blank" href="https://openmldb.ai/docs/zh/main/quickstart/sdk/rest_api.html#id3">docs of OpenMLDB APIServer</a> to prepare test data. eg. {"input": [["foo", 123]]}</p>
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

      <div v-if="testFormState.name != ''">
        <br/>
        <h1>{{ $t('Request') }} {{ $t('Schema') }}</h1>
        <p>{{ requestSchema }}</p>

        <h1>{{ $t('Request') }} {{ $t('Demo Data') }}</h1>
        <p>{{ requestDemoData }}</p>
      </div>

      <a-form-item
        label="Test data"
        :rules="[{ required: true, message: 'Please input test data!' }]">
        <a-input v-model:value="testFormState.testData" aria-placeholder="sdfs"/>
      </a-form-item>
      
      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">{{ $t('Submit') }}</a-button>
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

      testFormState: {
        name: "",
        testData: "",
      },

      demoData: "",
    };
  },

  mounted() {
    this.initData();

  },

  methods: {
    initData() {
      this.loading = true;

      console.log("------------------------ tobe");
      console.log(this.$route.query.featureservice);
      if (this.$route.query.featureservice != null) {
        this.testFormState.name = this.$route.query.featureservice;
      }


      axios.get(`/api/featureservices`)
        .then(response => {
          this.featureServices = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {});
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