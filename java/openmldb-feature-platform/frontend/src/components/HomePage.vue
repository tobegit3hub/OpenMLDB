<script setup>
</script>

<template>

  <div>
    <br />
    <h1>Welcome to OpenMLDB Feature Platform</h1>
    <p class="description">This is the welcome page for the Feaure Platform with your OpenMLDB data.</p>

    <br /><br />
    <a-row>
    <a-col :span="11">
      <h2><router-link to='/entities'>Entities ({{ entityCount }})</router-link></h2>
      <a-table :columns="entityColumns" :data-source="entities" />
    </a-col>
    
    <a-col :span="1"></a-col>
    <a-col :span="12">
      <h2><router-link to='/features'>Features ({{ featureCount }})</router-link></h2>
      <a-table :columns="featureColumns" :data-source="features" />
    </a-col>

    <a-col :span="11">
      <h2><router-link to='/featureviews'>Feature Views ({{ featureViewCount }})</router-link></h2>
      <a-table :columns="featureViewColumns" :data-source="featureviews" />
    </a-col>

    <a-col :span="1"></a-col>
    <a-col :span="12">
      <h2><router-link to='/featureservices'>Feature Services ({{ featureServiceCount }})</router-link></h2>
      <a-table :columns="featureServiceColumns" :data-source="featureservices" />
    </a-col>
  </a-row>
  </div>

</template>

<script>
import axios from 'axios'

  export default {
    data() {
      return {
        entities: [],
        features: [],
        featureviews: [],
        featureservices: [],
        
        entityCount: 0,
        featureCount: 0,
        featureViewCount: 0,
        featureServiceCount: 0,

        entityColumns: [{
          title: 'Name',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: 'PrimaryKeys',
          dataIndex: 'primaryKeys',
          key: 'primaryKeys',
        }],

        featureColumns: [{
          title: 'Feature View',
          dataIndex: 'featureViewName',
          key: 'featureViewName',
        },
        {
          title: 'Feature Name',
          dataIndex: 'featureName',
          key: 'featureName',
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
          title: 'SQL',
          dataIndex: 'sql',
          key: 'sql',
        },
        {
          title: 'Deployment',
          dataIndex: 'deployment',
          key: 'deployment',
        }],

      };
    },
    mounted() {
      this.initData();
  
    },
    methods: {
      initData() {
        axios.get(`/api/entities`)
          .then(response => {
            this.entities = response.data;
            this.entityCount = this.entities.length;
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
    },
  };
</script>

<style scoped>
</style>
