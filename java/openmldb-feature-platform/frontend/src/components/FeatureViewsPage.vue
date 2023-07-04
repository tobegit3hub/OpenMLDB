<template>

<div>
  <br/>
  <h1>{{ $t('Feature Views') }}</h1>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="featureViews" :loading="loading">
    <template #name="{ text, record }">
      <router-link :to="`/featureviews/${record.name}`">{{ text }}</router-link>
    </template>
    <template #db="{ text, record }">
      <router-link :to="`/databases/${record.db}`">{{ text }}</router-link>
    </template>  
    <!-- The delete column-->
    <template v-slot:custom="scope">
      <a-popconfirm
          title="Sure to delete?"
          @confirm="handleDelete(scope.record.name)">
        <a>{{ $t('Delete') }}</a>
      </a-popconfirm>
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
      featureViews: [],

      loading: false,
      
      columns: [{
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        slots: { customRender: 'name' }
      },
      {
        title: 'Entities',
        dataIndex: 'entityNames',
        key: 'entityNames',
      },
      {
        title: 'Database',
        dataIndex: 'db',
        key: 'db',
        slots: { customRender: 'db' }
      },
      {
        title: 'SQL',
        dataIndex: 'sql',
        key: 'sql',
      },
      {
        title: 'Features',
        dataIndex: 'featureNames',
        key: 'featureNames',
      },
      {
        title: 'Description',
        dataIndex: 'description',
        key: 'description',
      },
      {
        title: 'Actions',
        key: 'actions',
        slots: { customRender: 'custom' },
      }],

    };
  },

  mounted() {
    this.initData();

  },

  methods: {
    initData() {
      this.loading = true;
      axios.get(`/api/featureviews`)
        .then(response => {
          this.featureViews = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    handleDelete(name) {
      axios.delete(`/api/featureviews/${name}`)
      .then(response => {
        message.success(`Success to delete feature view: ${name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },

  },
};
</script>