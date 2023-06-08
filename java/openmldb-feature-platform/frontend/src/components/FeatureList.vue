<template>
    <div>
      <h2>Features</h2>
      <ul>
        <li v-for="feature in features" :key="feature.featureName">
          FeatureViewName: {{ feature.featureViewName }}, FeatureName: {{ feature.featureName }}, Type: {{ feature.type }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
import axios from 'axios'

  export default {
    data() {
      return {
        features: [],
      };
    },
    mounted() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        axios.get(`/api/features`)
          .then(response => {
            this.features = response.data;
          })
          .catch(error => {
            console.log(error);
          });
      },
    },
  };
  </script>
  