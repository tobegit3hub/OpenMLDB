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
import axios from 'axios'

  export default {
    data() {
      return {
        tableInfos: [],
      };
    },
    mounted() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        axios.get(`/api/tables`)
          .then(response => {
            this.tableInfos = response.data;
          })
          .catch(error => {
            console.log(error);
          });
      },
    },
  };
  </script>
  