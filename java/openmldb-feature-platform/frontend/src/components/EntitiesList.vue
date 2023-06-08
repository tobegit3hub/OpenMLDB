<template>
    <div>
      <h2>Entities</h2>
      <ul>
        <li v-for="entity in entities" :key="entity.name">
          Name: {{ entity.name }}, Primary keys: {{ entity.primaryKeys }}
        </li>
      </ul>
    </div>
  </template>
  
<script>
import axios from 'axios'

  export default {
    data() {
      return {
        entities: [],
      };
    },
    mounted() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        axios.get(`/api/entities`)
          .then(response => {
            this.entities = response.data;
          })
          .catch(error => {
            console.log(error);
          });
      },
    },
  };
  </script>
  