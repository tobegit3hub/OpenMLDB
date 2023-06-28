<template>
<div>

  <br/>
  <h1>Table: {{ data.table }} </h1>
  <a-descriptions layout="vertical" bordered>
    <a-descriptions-item label="Database"> 
      <router-link :to="`/databases/${data.db}`">{{ data.db }}</router-link>
    </a-descriptions-item>
    <a-descriptions-item label="Table">{{ data.table }}</a-descriptions-item>
    <a-descriptions-item label="Schema">{{ data.schema}}</a-descriptions-item>
  </a-descriptions>

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

    onMounted(() => {
      initData();
    });

    return {
      data
    }
  }
  
};
</script>