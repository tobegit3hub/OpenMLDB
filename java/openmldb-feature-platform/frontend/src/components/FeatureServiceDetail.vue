<template>
  <div>
  
    <br/>
    <h1>Feature Service: {{ data.name }} </h1>
    <a-descriptions layout="vertical" bordered>
      <a-descriptions-item label="Name"> {{ data.name }}</a-descriptions-item>
      <a-descriptions-item label="Feature list">{{ data.featureList }}</a-descriptions-item>
      <a-descriptions-item label="Database">{{ data.db }}</a-descriptions-item>
      <a-descriptions-item label="Sql">{{ data.sql }}</a-descriptions-item>
      <a-descriptions-item label="Deployment">{{ data.deployment }}</a-descriptions-item>
    </a-descriptions>
  
    <br/><br/>
    <h1>Features</h1>
    <a-table :columns="columns" :data-source="features">
      <template #featureView="{ text, record }">
        <router-link :to="`/featureviews/${record.featureViewName}`">{{ text }}</router-link>
      </template>
      <template #name="{ text, record }">
        <router-link :to="`/features/${record.featureViewName}/${record.featureName}`">{{ text }}</router-link>
      </template>
    </a-table>
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
  
      const features = ref([]);

      const columns = [{
          title: 'Feature View',
          dataIndex: 'featureViewName',
          key: 'featureViewName',
          slots: { customRender: 'featureView' }
        },
        {
          title: 'Feature Name',
          dataIndex: 'featureName',
          key: 'featureName',
          slots: { customRender: 'name' }
        },
        {
          title: 'Type',
          dataIndex: 'type',
          key: 'type',
        },
        {
          title: 'Description',
          dataIndex: 'description',
          key: 'description',
      }];

      const initData = () => {
        axios.get(`/api/featureservices/${props.name}`)
          .then(response => {
            data.value = response.data;

            // Request features from feature view
            axios.get(`/api/features?featureServiceName=${data.value.name}`)
              .then(response => {
                features.value = response.data;
              })
              .catch(error => {
                message.error(error.message);
              });

          })
          .catch(error => {
            message.error(error.message);
          })
        }
  
      onMounted(() => {
        initData();
      });
  
      return {
        data,
        features,
        columns
      }
    }
    
  };
  </script>