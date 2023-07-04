<template>
<div>

  <br/>
  <h1>{{ $t('Entity') }}: {{ data.name }} </h1>
  <a-descriptions layout="vertical" bordered>
    <a-descriptions-item label="Name"> {{ data.name }}</a-descriptions-item>
    <a-descriptions-item label="Primary Keys">{{ data.primaryKeys }}</a-descriptions-item>
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
      axios.get(`/api/entities/${props.name}`)
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