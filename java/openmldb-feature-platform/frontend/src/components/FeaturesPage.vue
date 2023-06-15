<template>

<div>
  <br/>
  <h1>Features</h1>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="features" :loading="loading">
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

export default {
  data() {
    return {
      features: [],

      loading: false,
      
      columns: [{
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
      }],

      formState: {
        sql: '',
      }
    };
  },

  mounted() {
    this.initData();
  },

  methods: {
    initData() {
      this.loading = true;
      axios.get(`/api/features`)
        .then(response => {
          this.features = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {
          this.loading = false;
        });
    }

  },
};
</script>