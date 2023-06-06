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
  export default {
    data() {
      return {
        features: [],
      };
    },
    mounted() {
      this.fetchEntities();
    },
    methods: {
      fetchEntities() {
        fetch("http://127.0.0.1:8888/api/features")
          .then((response) => response.json())
          .then((data) => {
            this.features = data;
          })
          .catch((error) => {
            console.error("Error fetching entities:", error);
          });
      },
    },
  };
  </script>
  