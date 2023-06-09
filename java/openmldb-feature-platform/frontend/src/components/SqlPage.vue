<template>

<div>
  <br />
  <div>
    <h1>Execute SQL</h1>
    <a-typography>
      <a-typography-paragraph>
        <p>Execute SQLs in OpenMLDB.</p>
        <p>eg. CREATE DATABASE IF NOT EXISTS db1</p>
        <p>eg. DROP DATABASE db1</p>
      </a-typography-paragraph>
    </a-typography>
    <!-- Create form -->
    <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      @submit="handleSubmit">
      <a-form-item
        label="SQL"
        :rules="[{ required: true, message: 'Please input SQL!' }]">
        <a-input v-model:value="formState.sql" />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
        <a-button type="primary" html-type="submit">Submit</a-button>
      </a-form-item>
    </a-form>
  </div>

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';

export default {
  data() {
    return {
      formState: {
        sql: '',
      }
    };
  },

  mounted() {

  },

  methods: {
    handleSubmit() {
      axios.post(`/api/sql/execute`, {
        "sql": this.formState.sql,
      })
      .then(response => {
        message.success(`Success to execute SQL: ${this.formState.sql}`);
        this.initData();
      })
      .catch(error => {
        message.error(error.message);
      });
    },
  },
};
</script>