<template>
    <div>
      <h2>Table List</h2>
      <ul>
        <li v-for="table_name in tables" :key="table_name">
          Table: {{ table_name }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
  export default {
    data() {
      return {
        tables: [],
      };
    },
    mounted() {
      this.fetchEntities();
    },
    methods: {
      fetchEntities() {
        fetch("http://127.0.0.1:8888/api/tables")
          .then((response) => response.json())
          .then((data) => {
            this.tables = data;
          })
          .catch((error) => {
            console.error("Error fetching entities:", error);
          });
      },
    },
  };
  </script>
  