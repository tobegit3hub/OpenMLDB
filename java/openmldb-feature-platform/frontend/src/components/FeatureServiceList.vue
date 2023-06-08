<template>
    <div>
      <h2>FeatureServices</h2>
      <ul>
        <li v-for="featureService in featureServices" :key="featureService.name">
          Name: {{ featureService.name }}, FeatureViews: {{ featureService.featureViewNames }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
import axios from 'axios'

  export default {
    data() {
      return {
        featureServices: [],
      };
    },
    mounted() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        axios.get(`/api/featureservices`)
          .then(response => {
            this.featureServices = response.data;
          })
          .catch(error => {
            console.log(error);
          });
      },
    },
  };
  </script>
  