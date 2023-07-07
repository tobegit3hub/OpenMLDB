<template>

  <div>
    <br />
    <div>
      <h1>{{ $t('Create Feature') }}</h1>
      <!-- Create form -->
      <a-form
        :model="formState"
        name="basic"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 16 }">
        <a-form-item
          :label="$t('Feature View Name')"
          :rules="[{ required: true, message: 'Please input name!' }]">
          <a-input v-model:value="formState.name" />
        </a-form-item>
  
        <a-form-item
          :label="$t('Database')"
          :rules="[{ required: true, message: 'Please input database!' }]">
          <a-select v-model:value="formState.db">
            <option v-for="database in databases" :value="database">{{ database }}</option>
          </a-select>
        </a-form-item>
  
        <a-form-item
          :label="$t('Entity')"
          :rules="[{ required: false, message: 'Please input entity names!' }]">
          <a-select v-model:value="formState.entityNames">
            <option v-for="entity in entities" :value="entity.name">{{ entity.name }}</option>
          </a-select>
        </a-form-item>
  
        <a-form-item
          :label="$t('SQL')"
          :rules="[{ required: true, message: 'Please input SQL!' }]">
          <a-textarea v-model:value="formState.sql" rows="5"></a-textarea>
        </a-form-item>
        
        <p v-if="validatedFeatureNames.length > 0">
          {{ $t('Feature List') }}:
          <ul>
            <li v-for="featureName in validatedFeatureNames" :key="featureName">{{ featureName }}</li>
          </ul>
        </p>

        <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
            <a-button v-if="validatedFeatureNames.length == 0" type="primary" @click="validateForm()">{{ $t('Validate') }} {{ $t('SQL') }}</a-button>
            <a-button v-else type="primary" @click="handleSubmit()">{{ $t('Submit') }}</a-button>
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
        databases: [],
  
        validatedFeatureNames: [],
  
        formState: {
          name: '',
          entityNames: '',
          db: '',
          sql: ''
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
  
        axios.get(`/api/entities`)
          .then(response => {
            this.entities = response.data;
          })
          .catch(error => {
            message.error(error.message);
          })
          .finally(() => {});        
      },
  
      validateForm() {
        axios.post(`/api/featureviews/validate`, {
          "name": this.formState.name,
          "entityNames": this.formState.entityNames,
          "db": this.formState.db,
          "sql": this.formState.sql
        })
        .then(response => {
          message.success(`Success to validate feature view ${this.formState.name}`);
          this.validatedFeatureNames = response.data.split(",").map(str => str.trim());
        })
        .catch(error => {
          if (error.response.data) {
            message.error(error.response.data);
          } else {
            message.error(error.message);
          }
        });      
      },
  
      handleSubmit() {
        axios.post(`/api/featureviews`, {
          "name": this.formState.name,
          "entityNames": this.formState.entityNames,
          "db": this.formState.db,
          "sql": this.formState.sql
        })
        .then(response => {
          message.success(`Success to add feature view ${this.formState.name}`);
  
          // Redirect to FeatureView detail page
          this.$router.push(`/featureviews/${this.formState.name}`);
        })
        .catch(error => {
          if (error.response.data) {
            message.error(error.response.data);
          } else {
            message.error(error.message);
          }
        });
      },
  
    },
  };
  </script>