<template>

<div>
  <br/>
  <h1>
    {{ $t('Entities') }}
    &nbsp;&nbsp;<a-button type="primary"><router-link to='/entities/create'>{{ $t('Create Entity') }}</router-link></a-button>
  </h1>

  <br/>
  <!-- Data table -->
  <a-table :columns="columns" :data-source="entities" :loading="loading">
    <template #name="{ text, record }">
      <router-link :to="`/entities/${record.name}`">{{ text }}</router-link>
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
      entities: [],

      loading: false,
      
      columns: [{
        title: this.$t('Name'),
        dataIndex: 'name',
        key: 'name',
        slots: { customRender: 'name' }
      },
      {
        title: this.$t('Primary Keys'),
        dataIndex: 'primaryKeys',
        key: 'primaryKeys',
      },
      {
        title: this.$t('Actions'),
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
      axios.get(`/api/entities`)
        .then(response => {
          this.entities = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    handleDelete(name) {
      axios.delete(`/api/entities/${name}`)
      .then(response => {
        message.success(`Success to delete entity: ${name}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },

  },
};
</script>