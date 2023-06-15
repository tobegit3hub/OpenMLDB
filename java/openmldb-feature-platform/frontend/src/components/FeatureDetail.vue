<template>
<div>

  <br/>
  <h1>Feature: {{ data.featureName }} </h1>
  <a-descriptions layout="vertical" bordered>
    <a-descriptions-item label="Feature view name"><router-link :to="`/featureviews/${data.featureViewName}`">{{ data.featureViewName }}</router-link></a-descriptions-item>
    <a-descriptions-item label="Feature name">{{ data.featureName }}</a-descriptions-item>
    <a-descriptions-item label="Type">{{ data.type}}</a-descriptions-item>
    <a-descriptions-item label="Description">{{ data.description}}</a-descriptions-item>
  </a-descriptions>

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';
import { onMounted, ref } from 'vue';

export default {
  props: {
    featureViewName: {
      type: String,
      required: true
    },
    featureName: {
      type: String,
      required: true
    }
  },

  setup(props) {
    const data = ref("");

    const initData = () => {
      axios.get(`/api/features/${props.featureViewName}/${props.featureName}`)
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