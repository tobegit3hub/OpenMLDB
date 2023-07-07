<script setup>
</script>

<template>

  <div>
    <br />
    <h1 style="display: inline-block; vertical-align: middle; font-size: 30px;">
      {{ $t('Feature Platform') }}
    </h1>

    <br/><br/>


    <a-row :gutter="16">

      <a-col :span="6">
        <router-link to='/tables'>
          <a-card :title="$t('Data Tables')" :bordered="false">
            <h1>{{ tableCount }}</h1>
          </a-card>
        </router-link>
      </a-col>

      <a-col :span="6">
        <router-link to='/featureviews'>
          <a-card :title="$t('Feature Views')" :bordered="false">
            <h1>{{ featureViewCount }}</h1>
          </a-card>
        </router-link>
      </a-col>

      <a-col :span="6">
        <router-link to='/features'>
          <a-card :title="$t('Features')" :bordered="false">
            <h1>{{ featureCount }}</h1>
          </a-card>
        </router-link>
      </a-col>

      <a-col :span="6">
        <router-link to='/featureservices'>
          <a-card :title="$t('Feature Services')" :bordered="false">
            <h1>{{ featureServiceCount }}</h1>
          </a-card>
        </router-link>
      </a-col>
    </a-row>

  </div>

</template>

<script>
import axios from 'axios'

import VueECharts from 'vue-echarts';

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
