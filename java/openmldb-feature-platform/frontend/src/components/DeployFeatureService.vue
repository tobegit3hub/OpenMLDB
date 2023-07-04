<template>

<div>

  <br />
  <div>
    <h1>{{ $t('Create') }} {{ $t('Feature Service') }}</h1>
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

      loading: false,
      
      formState: {
        name: '',
        featureList: ''
      },

    };
  },

  mounted() {
    this.initData();

  },

  methods: {
    initData() {
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

  },
};
</script>