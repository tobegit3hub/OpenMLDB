<template>
    <div>
      <h2>Entities List</h2>
      <ul>
        <li v-for="entity in entities" :key="entity.name">
          Name: {{ entity.name }}, Primary keys: {{ entity.primaryKeys }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
  export default {
    data() {
      return {
        entities: [],
      };
    },
    mounted() {
      this.fetchEntities();
    },
    methods: {
      fetchEntities() {
        fetch("http://127.0.0.1:8888/api/entities")
          .then((response) => response.json())
          .then((data) => {
            this.entities = data;
          })
          .catch((error) => {
            console.error("Error fetching entities:", error);
          });
      },
    },
  };
  </script>
  