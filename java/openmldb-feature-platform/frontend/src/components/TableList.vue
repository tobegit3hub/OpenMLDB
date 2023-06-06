<template>
    <div>
      <h2>Table List</h2>
      <ul>
        <li v-for="tableInfo in tableInfos" :key="tableInfo.db">
          Database: {{ tableInfo.db }}, Table: {{ tableInfo.table }}, Schema: {{ tableInfo.schema }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
  export default {
    data() {
      return {
        tableInfos: [],
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
            this.tableInfos = data;
          })
          .catch((error) => {
            console.error("Error fetching entities:", error);
          });
      },
    },
  };
  </script>
  