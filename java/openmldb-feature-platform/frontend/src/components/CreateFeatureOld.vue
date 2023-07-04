<template>

<div>
  <br />
  <div>
    <h1>{{ $t('Create Feature') }}</h1>
    <!-- Create form -->

    <div>
    <a-steps :current="currentStep">
      <a-step title="Step 1" />
      <a-step title="Step 2" />
      <a-step title="Step 3" />
    </a-steps>

    <div v-if="currentStep === 0">
      <!-- Step 1 Form Fields -->
      <a-form-item label="Field 1">
        <a-input v-model="form.field1" />
      </a-form-item>
      <a-form-item label="Field 2">
        <a-input v-model="form.field2" />
      </a-form-item>

      <div style="margin-top: 16px;">
        <a-button type="primary" @click="nextStep">Next</a-button>
      </div>
    </div>

    <div v-if="currentStep === 1">
      <!-- Step 2 Form Fields -->
      <a-form-item label="Field 3">
        <a-input v-model="form.field3" />
      </a-form-item>
      <a-form-item label="Field 4">
        <a-input v-model="form.field4" />
      </a-form-item>

      <div style="margin-top: 16px;">
        <a-button style="margin-right: 8px;" @click="prevStep">Previous</a-button>
        <a-button type="primary" @click="nextStep">Next</a-button>
      </div>
    </div>

    <div v-if="currentStep === 2">
      <!-- Step 3 Form Fields -->
      <a-form-item label="Field 5">
        <a-input v-model="form.field5" />
      </a-form-item>
      <a-form-item label="Field 6">
        <a-input v-model="form.field6" />
      </a-form-item>

      <div style="margin-top: 16px;">
        <a-button style="margin-right: 8px;" @click="prevStep">Previous</a-button>
        <a-button type="primary" @click="submitForm">Submit</a-button>
      </div>
    </div>
  </div>
  


    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      @submit="handleSubmit">
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
      
      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" @click="validateForm()">{{ $t('Validate') }}</a-button>

        &nbsp;&nbsp;&nbsp;<a-button type="primary" html-type="submit">{{ $t('Submit') }}</a-button>
      </a-form-item>
    </a-form>

    <p v-if="validatedFeatureNames.length > 0">
      {{ $t('Feature List') }}:
      <ul>
        <li v-for="featureName in validatedFeatureNames" :key="featureName">{{ featureName }}</li>
      </ul>
    </p>

  </div>

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';

export default {
  data() {
    return {
      currentStep: 0,
      form: {
        field1: '',
        field2: '',
        field3: '',
        field4: '',
        field5: '',
        field6: ''
      },

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

    nextStep() {
      if (this.currentStep < 2) {
        this.currentStep++;
      }
    },
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--;
      }
    },
    submitForm() {
      // Handle form submission
      console.log(this.form);
    },

    validateForm() {
      console.log("Try to validate form");
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
        message.error(error.message);
      });
    },

  },
};
</script>