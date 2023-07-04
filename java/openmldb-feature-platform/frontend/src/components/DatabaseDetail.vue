<template>

  <div>
    <br/>
    <h1>{{ $t('Database') }}: {{ db }}</h1>
    <!-- Tables table -->
    <a-table :columns="columns" :data-source="tables">
      <template #table="{ text, record }">
        <router-link :to="`/tables/${record.db}/${record.table}`">{{ text }}</router-link>
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
    db: {
      type: String,
      required: true
    }
  },

  setup(props) {
    const tables = ref([]);

    const columns = [{
      title: 'Table',
      dataIndex: 'table',
      key: 'table',
      slots: { customRender: 'table' }
    },
    {
      title: 'Schema',
      dataIndex: 'schema',
      key: 'schema',
    }];

    const initData = () => {
      axios.get(`/api/databases/${props.db}/tables`)
          .then(response => {
            tables.value = response.data;
          })
          .catch(error => {
            message.error(error.message);
          })
          .finally(() => {});
      }

    onMounted(() => {
      initData();
    });

    return {
      tables,
      columns
    }
  }
  
};
</script>