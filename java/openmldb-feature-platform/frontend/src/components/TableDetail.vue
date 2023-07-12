<template>
<div>

  <br/>
  <h1>{{ $t('Table') }}: {{ data.table }} </h1>
  <a-descriptions layout="vertical" bordered>
    <a-descriptions-item label="Database"> 
      <router-link :to="`/databases/${data.db}`">{{ data.db }}</router-link>
    </a-descriptions-item>
    <a-descriptions-item label="Table">{{ data.table }}</a-descriptions-item>
    <a-descriptions-item label="Schema">{{ data.schema}}</a-descriptions-item>
  </a-descriptions>


  <br/>
  <a-button type="primary" @click="click_preview_data()">{{ $t('Preview Data') }}</a-button>

  <div v-if="isPreviewData">
    <br/>
    <h3>Preview Data (Limit 10 rows)</h3>
    <p v-html="tableData"></p>
  </div>

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
    },
    name: {
      type: String,
      required: true
    }
  },

  setup(props) {
    const data = ref("");

    const isPreviewData = ref(false);

    const tableData = ref("");

    const initData = () => {
      axios.get(`/api/tables/${props.db}/${props.name}`)
        .then(response => {
          data.value = response.data;
        })
        .catch(error => {
          message.error(error.message);
        })
        .finally(() => {

        });
      }

    const click_preview_data = () => {

      if (isPreviewData.value) {
        isPreviewData.value = false;

        return;
      }

      const sql = "SELECT * FROM " + props.db + "." + props.name + " LIMIT 10";

      axios.post(`/api/sql/execute`, {
        "sql": sql
      })
      .then(response => {
        message.success(`Success to execute SQL: ${sql}`);
        console.log(response.data);

        isPreviewData.value = true;

        tableData.value = response.data.replace(/\n/g, '<br>');

      })
      .catch(error => {
        console.log(error);
        if ("response" in error && "data" in error.response) {
          message.error(error.response.data);
        } else {
          message.error(error.message);
        }
      });
      
    }

    onMounted(() => {
      initData();
    });

    return {
      data,
      isPreviewData,
      click_preview_data,
      tableData
    }
  }
  
};
</script>