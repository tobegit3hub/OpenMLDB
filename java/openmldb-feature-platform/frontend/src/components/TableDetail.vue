<template>
<div>

  <br/>
  <h1>{{ $t('Table') }}: {{ data.table }} </h1>
  <a-descriptions layout="vertical" bordered>
    <a-descriptions-item :label="$t('Database')"> 
      <router-link :to="`/databases/${data.db}`">{{ data.db }}</router-link>
    </a-descriptions-item>
    <a-descriptions-item :label="$t('Table')">{{ data.table }}</a-descriptions-item>
    <a-descriptions-item :label="$t('Schema')">{{ data.schema}}</a-descriptions-item>
  </a-descriptions>

  <br/>
  <a-button type="primary" @click="click_preview_data()">{{ $t('Preview Data') }}</a-button>

  <div v-if="isPreviewData">
    <br/>
    <h3>{{$t('Preview Data')}} ({{$t('Limit')}} 10 {{$t('Rows')}})</h3>
    <p v-html="tableData"></p>
  </div>

</div>
</template>

<script>
import axios from 'axios';
import { message } from 'ant-design-vue';

export default {
  props: {
    db: {
      type: String,
      required: true,
    },
    name: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      data: "",
      isPreviewData: false,
      tableData: "",
    };
  },
  methods: {
    initData() {
      axios
        .get(`/api/tables/${this.db}/${this.name}`)
        .then((response) => {
          this.data = response.data;
        })
        .catch((error) => {
          message.error(error.message);
        });
    },
    click_preview_data() {
      if (this.isPreviewData) {
        this.isPreviewData = false;
        return;
      }

      const sql = "SELECT * FROM " + this.db + "." + this.name + " LIMIT 10";

      axios
        .post(`/api/sql/execute`, {
          sql: sql,
        })
        .then((response) => {
          message.success(`Success to execute SQL: ${sql}`);
          console.log(response.data);

          this.isPreviewData = true;
          this.tableData = response.data.replace(/\n/g, '<br>');
        })
        .catch((error) => {
          console.log(error);
          if ('response' in error && 'data' in error.response) {
            message.error(error.response.data);
          } else {
            message.error(error.message);
          }
        });
    },
  },
  mounted() {
    this.initData();
  },
};
</script>
