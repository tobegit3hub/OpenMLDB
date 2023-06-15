<template>
  <div>
  
    <br/>
    <h1>Feature View: {{ data.name }} </h1>
    <a-descriptions layout="vertical" bordered>
      <a-descriptions-item label="Name"> {{ data.name }}</a-descriptions-item>
      <a-descriptions-item label="Entities">{{ data.entityNames }}</a-descriptions-item>
      <a-descriptions-item label="Database">{{ data.db }}</a-descriptions-item>
      <a-descriptions-item label="SQL">{{ data.sql }}</a-descriptions-item>
      <a-descriptions-item label="Features">{{ data.featureNames }}</a-descriptions-item>
      <a-descriptions-item label="Descriptioin">{{ data.descriptioin }}</a-descriptions-item>
    </a-descriptions>
  
  </div>
  </template>
    
  <script>
  import axios from 'axios'
  import { message } from 'ant-design-vue';
  import { onMounted, ref } from 'vue';
  
  export default {
    props: {
      name: {
        type: String,
        required: true
      }
    },
  
    setup(props) {
      const data = ref("");
  
      const initData = () => {
        axios.get(`/api/featureviews/${props.name}`)
          .then(response => {
            data.value = response.data;
          })
          .catch(error => {
            message.error(error.message);
          })
          .finally(() => {
  
          });
        }
  
      onMounted(() => {
        initData();
      });
  
      return {
        data
      }
    }
    
  };
  </script>