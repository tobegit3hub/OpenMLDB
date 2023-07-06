<script setup>
</script>

<template>

  <div>
    <br />
    <a-image
      height="40px"
      src="/openmldb_logo.png" alt="OpenMLDB"
      style="display: inline-block; vertical-align: middle;"
    />
    <h1 style="display: inline-block; vertical-align: middle; font-size: 30px;">
      &nbsp;&nbsp;&nbsp;{{ $t('Feature Platform') }}
    </h1>

    <br/><br/>
    <a-row>
    <a-col :span="11">
      <h2><router-link to='/tables'>{{ $t('Data Tables') }} ({{ tableCount }})</router-link></h2>
      <a-table :pagination="{ pageSize: 2 }" :columns="tableColumns" :data-source="tables" />
    </a-col>
    
    <a-col :span="1"></a-col>
    <a-col :span="12">
      <h2><router-link to='/features'>{{ $t('Features') }} ({{ featureCount }})</router-link></h2>
      <a-table :pagination="{ pageSize: 2 }" :columns="featureColumns" :data-source="features" />
    </a-col>

    <a-col :span="11">
      <h2>
        <router-link to='/featureviews'>{{ $t('Feature Views') }} ({{ featureViewCount }})</router-link>
        &nbsp;&nbsp;<a-button type="primary"><router-link to='/features/create'>{{ $t('Create Feature') }}</router-link></a-button>
      </h2>
      
      <a-table :pagination="{ pageSize: 2 }" :columns="featureViewColumns" :data-source="featureviews" />
    </a-col>

    <a-col :span="1"></a-col>
    <a-col :span="12">
      <h2>
        <router-link to='/featureservices'>{{ $t('Feature Services') }} ({{ featureServiceCount }})</router-link>
        &nbsp;&nbsp;<a-button type="primary"><router-link to='/featureservices/deploy'>{{ $t('Create Service') }}</router-link></a-button>
      </h2>
      <a-table :pagination="{ pageSize: 2 }" :columns="featureServiceColumns" :data-source="featureservices" />
    </a-col>
  </a-row>
  </div>

  <!-- Tutorial Steps-->
  <br/><br/><br/>
  <div>
    <h1>{{ $t('How to use') }} OpenMLDB {{ $t('Feature Platform') }}
 <a target="_blank" href="https://wiki.4paradigm.com/pages/viewpage.action?pageId=136663122">[{{ $t('docs') }}]</a></h1>
    
    <br/>
    <div>
    <a-steps v-model:current="currentStep">
      <a-step v-for="item in steps" :key="item.title" :title="item.title" />
    </a-steps>
    <div class="steps-content">
      {{ steps[currentStep].content }}
    </div>
    <div class="steps-action">
      <a-button v-if="currentStep < steps.length - 1" type="primary" @click="next">{{ $t('Next') }}</a-button>
      <a-button
        v-if="currentStep == steps.length - 1"
        type="primary"
      >
      {{ $t('Done') }}
      </a-button>
      <a-button v-if="currentStep > 0" style="margin-left: 8px" @click="prev">{{ $t('Previous') }}</a-button>
    </div>
  </div>
  </div>

</template>

<script>
import axios from 'axios'

export default {
    data() {
      return {
        tables: [],
        features: [],
        featureviews: [],
        featureservices: [],
        
        tableCount: 0,
        featureCount: 0,
        featureViewCount: 0,
        featureServiceCount: 0,

        tableColumns: [{
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
        }],

        featureColumns: [{
          title: 'Feature View',
          dataIndex: 'featureViewName',
          key: 'featureViewName',
          slots: { customRender: 'featureView' }
        },
        {
          title: 'Feature Name',
          dataIndex: 'featureName',
          key: 'featureName',
          slots: { customRender: 'name' }
        },
        {
          title: 'Type',
          dataIndex: 'type',
          key: 'type',
        }],

        featureViewColumns: [{
          title: 'Name',
          dataIndex: 'name',
          key: 'name',
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
        }],

        featureServiceColumns: [{
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
          title: 'Deployment',
          dataIndex: 'deployment',
          key: 'deployment'
        }],

        currentStep: 0,

        steps: [{
          title: 'Import Data',
          content: 'Import the data into OpenMLDB cluster. Use SQLs like `CREATE TABLE` and `LOAD DATA`.',
        }, {
          title: 'Create Feature View',
          content: 'Create the FeatureView with SQL. For example, create with "SELECT name, age + 10 AS new_age FROM user" and the feature of "name" and "new_age" will be automatically extracted.',
        }, {
          title: 'Deploy Feature Service',
          content: 'Choose one or multiple FeatureViews to deploy as one service. It will automatically merge SQL and create OpenMLDB deloyment.',
        }, {
          title: 'Request Service',
          content: 'Request the online feature service with any HTTP clients. You can test the service in OpenMLDB Feture Platform as well.',
        }],

      };
    },

    mounted() {
      this.initData();
    },

    methods: {
      initData() {
        axios.get(`/api/tables`)
          .then(response => {
            this.tables = response.data;
            this.tableCount = this.tables.length;
          })
          .catch(error => {
            console.log(error.message);
          });

        axios.get(`/api/features`)
          .then(response => {
            this.features = response.data;
            this.featureCount = this.features.length;
          })
          .catch(error => {
            console.log(error.message);
          });

        axios.get(`/api/featureviews`)
          .then(response => {
            this.featureviews = response.data;
            this.featureViewCount = this.featureviews.length;
          })
          .catch(error => {
            console.log(error.message);
          });

        axios.get(`/api/featureservices`)
          .then(response => {
            this.featureservices = response.data;
            this.featureServiceCount = this.featureservices.length;
          })
          .catch(error => {
            console.log(error.message);
          });
      },

      next() {
        this.currentStep++;
      },

      prev() {
        this.currentStep--;
      },
    },
  };
</script>

<style scoped>
.steps-content {
  margin-top: 16px;
  border: 1px dashed #e9e9e9;
  border-radius: 6px;
  background-color: #fafafa;
  min-height: 200px;
  text-align: center;
  padding-top: 80px;
}

.steps-action {
  margin-top: 24px;
}

[data-theme='dark'] .steps-content {
  background-color: #2f2f2f;
  border: 1px dashed #404040;
}
</style>
