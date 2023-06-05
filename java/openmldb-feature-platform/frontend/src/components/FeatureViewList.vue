<template>
    <div>
      <h2>FeatureViews</h2>
      <ul>
        <li v-for="featureView in featureViews" :key="featureView.name">
          Name: {{ featureView.name }}, EntityName: {{ featureView.entityName }}, SQL: {{ featureView.sql }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
  export default {
    data() {
      return {
        featureViews: [],
      };
    },
    mounted() {
      this.fetchEntities();
    },
    methods: {
      fetchEntities() {
        fetch("http://127.0.0.1:8888/api/featureviews")
          .then((response) => response.json())
          .then((data) => {
            this.featureViews = data;
          })
          .catch((error) => {
            console.error("Error fetching entities:", error);
          });
      },
    },
  };
  </script>
  