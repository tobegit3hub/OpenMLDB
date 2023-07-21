<template>
  <div>
  
    <br/>
    <h1>
      {{ $t('Feature Service') }}: {{ data.name }} 
      &nbsp;&nbsp;<a-button type="primary"><router-link :to="`/featureservices/test?featureservice=${data.name}`">{{ $t('Test Service') }}</router-link></a-button>
    </h1>

    <br/>
    <a-descriptions layout="vertical" bordered>
      <a-descriptions-item :label="$t('Name')"> {{ data.name }}</a-descriptions-item>
      <a-descriptions-item :label="$t('Feature List')">{{ data.featureList }}</a-descriptions-item>
      <a-descriptions-item :label="$t('Database')"><router-link :to="`/databases/${data.db}`">{{ data.db }}</router-link></a-descriptions-item>
      <a-descriptions-item :label="$t('SQL')">{{ data.sql }}</a-descriptions-item>
      <a-descriptions-item :label="$t('Deployment')">{{ data.deployment }}</a-descriptions-item>
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
    <h1>{{ $t('Dependent Tables') }}</h1>
    <a-table :columns="tableColumns" :data-source="tables">
      <template #db="{ text, record }">
        <router-link :to="`/databases/${record.db}`">{{ text }}</router-link>
      </template>
      <template #table="{ text, record }">
        <router-link :to="`/tables/${record.db}/${record.table}`">{{ text }}</router-link>
      </template>
    </a-table>
  </div>
  </template>
    
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';

export default {
  props: {
    name: {
      type: String,
      required: true
    }
  },

  data() {
    return {
      data: {},
      features: [],

      columns: [{
          title: this.$t('Feature View'),
          dataIndex: 'featureViewName',
          key: 'featureViewName',
          slots: { customRender: 'featureView' }
        },
        {
          title: this.$t('Feature Name'),
          dataIndex: 'featureName',
          key: 'featureName',
          slots: { customRender: 'name' }
        },
        {
          title: this.$t('Type'),
          dataIndex: 'type',
          key: 'type',
        },
        {
          title: this.$t('Description'),
          dataIndex: 'description',
          key: 'description',
      }],

      tables: [],

      tableColumns: [{
          title: this.$t('Database'),
          dataIndex: 'db',
          key: 'db',
          slots: { customRender: 'db' }
        },
        {
          title: this.$t('Table'),
          dataIndex: 'table',
          key: 'table',
          slots: { customRender: 'table' }
        }],

          
      requestSchema: "",
      requestDemoData: ""
    };
  },

  methods: {
    initData() {
      axios.get(`/api/featureservices/${this.name}`)
        .then(response => {
          this.data = response.data;

          // Request features from feature view
          axios.get(`/api/features?featureServiceName=${this.data.name}`)
            .then(response => {
              this.features = response.data;
            })
            .catch(error => {
              message.error(error.message);
            });

        })
        .catch(error => {
          message.error(error.message);
        })

      axios.get(`/api/featureservices/${this.name}/tables`)
        .then(response => {            
          response.data.forEach(str => {
            let [db, table] = str.split('.');
            this.tables.push({"db": db, "table": table});
          });
        })
        .catch(error => {
          message.error(error.message);
        });

      axios.get(`/api/featureservices/${this.name}/request/schema`)
        .then(response => {
          this.requestSchema = response.data;

        })
        .catch(error => {
          message.error(error.message);
        });

      axios.get(`/api/featureservices/${this.name}/request/demo`)
        .then(response => {
          this.requestDemoData = response.data;
        })
        .catch(error => {
          message.error(error.message);
        });  
    }
  },

  mounted() {
    this.initData();
  }
  
}
</script>