<template>
    <div>
      <h2>FeatureViews</h2>
      <ul>
        <li v-for="featureView in featureViews" :key="featureView.name">
          Name: {{ featureView.name }}, EntityNames: {{ featureView.entityNames }}, SQL: {{ featureView.sql }}
        </li>
      </ul>
    </div>
  </template>
  
<script>

import axios from 'axios'

  export default {
    data() {
      return {
        featureViews: [],
      };
    },
    mounted() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        axios.get(`/api/featureviews`)
          .then(response => {
            this.featureViews = response.data;
          })
          .catch(error => {
            console.log(error);
          });
      },
    },
  };
  </script>
  