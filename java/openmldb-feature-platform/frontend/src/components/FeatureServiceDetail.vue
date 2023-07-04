<template>
  <div>
  
    <br/>
    <h1>{{ $t('Feature Service') }}: {{ data.name }} </h1>
    <a-descriptions layout="vertical" bordered>
      <a-descriptions-item label="Name"> {{ data.name }}</a-descriptions-item>
      <a-descriptions-item label="Feature list">{{ data.featureList }}</a-descriptions-item>
      <a-descriptions-item label="Database"><router-link :to="`/databases/${data.db}`">{{ data.db }}</router-link></a-descriptions-item>
      <a-descriptions-item label="SQL">{{ data.sql }}</a-descriptions-item>
      <a-descriptions-item label="Deployment">{{ data.deployment }}</a-descriptions-item>
    </a-descriptions>
  
    <br/><br/>
    <h1>{{ $t('Features') }}</h1>
    <a-table :columns="columns" :data-source="features">
      <template #featureView="{ text, record }">
        <router-link :to="`/featureviews/${record.featureViewName}`">{{ text }}</router-link>
      </template>
      <template #name="{ text, record }">
        <router-link :to="`/features/${record.featureViewName}/${record.featureName}`">{{ text }}</router-link>
      </template>
    </a-table>

    <br/>
    <h1>{{ $t('Dependent') }} {{ $t('Tables') }}</h1>
    <a-table :columns="tableColumns" :data-source="tables">
      <template #db="{ text, record }">
        <router-link :to="`/databases/${record.db}`">{{ text }}</router-link>
      </template>
      <template #table="{ text, record }">
        <router-link :to="`/tables/${record.db}/${record.table}`">{{ text }}</router-link>
      </template>
    </a-table>

    <br/>
    <h1>{{ $t('Request') }} {{ $t('Schema') }}</h1>
    <p>{{ requestSchema }}</p>

    <h1>{{ $t('Request') }} {{ $t('Demo Data') }}</h1>
    <p>{{ requestDemoData }}</p>

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

      const tables = ref([]);

      const tableColumns = [{
          title: 'Database',
          dataIndex: 'db',
          key: 'db',
          slots: { customRender: 'db' }
        },
        {
          title: 'Table',
          dataIndex: 'table',
          key: 'table',
          slots: { customRender: 'table' }
        }];

          
      const requestSchema = ref("");
      const requestDemoData = ref("");

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

        axios.get(`/api/featureservices/${props.name}/tables`)
          .then(response => {            
            response.data.forEach(str => {
              let [db, table] = str.split('.');
              tables.value.push({"db": db, "table": table});
            });
          })
          .catch(error => {
            message.error(error.message);
          });

        axios.get(`/api/featureservices/${props.name}/request/schema`)
          .then(response => {
            requestSchema.value = response.data;

          })
          .catch(error => {
            message.error(error.message);
          });

        axios.get(`/api/featureservices/${props.name}/request/demo`)
          .then(response => {
            requestDemoData.value = response.data;
          })
          .catch(error => {
            message.error(error.message);
          });          
      }
  
      onMounted(() => {
        initData();
      });
  
      return {
        data,
        features,
        columns,
        tables,
        tableColumns,
        requestSchema,
        requestDemoData
      }
    }
    
  };
  </script>