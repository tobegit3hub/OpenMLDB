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
  export default {
    data() {
      return {
        featureServices: [],
      };
    },
    mounted() {
      this.fetchEntities();
    },
    methods: {
      fetchEntities() {
        fetch("http://127.0.0.1:8888/api/featureservices")
          .then((response) => response.json())
          .then((data) => {
            this.featureServices = data;
          })
          .catch((error) => {
            console.error("Error fetching entities:", error);
          });
      },
    },
  };
  </script>
  