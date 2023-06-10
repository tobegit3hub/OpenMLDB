<template>

<div>
  <br/>
  <h1>Tutorial</h1>
  <p>These show the basic cases of using OpenMLDB Feature Platform for feature extration.</p>

  <a-collapse v-model:activeKey="activeKey" accordion>
    <a-collapse-panel key="1" header="Single Table And Single Feature View">
      <pre>
// Create test db and tables      
client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");

// Create feature view
client.createFeatureView("featureview1", "", "SELECT name, age + 10 AS new_age FROM test_db.user");

// Create feature service
client.createFeatureService("featureservice1", "featureview1");

// Test feature service
client.requestFeatureService("featureservice1", "{\"input\": [[\"abc\", 22]]}");

// Cleanup resources
client.deleteFeatureService("featureservice1");
client.deleteFeatureView("featureview1");
client.executeSql("DROP TABLE test_db.user");
client.executeSql("DROP DATABASE test_db");
      </pre>
    </a-collapse-panel>
    <a-collapse-panel key="2" header="Multiple Tables And Single Feature View">
      <pre>
// Create test db and tables
client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");
client.executeSql("CREATE TABLE IF NOT EXISTS test_db.trade (user_name string, amount float)");

// Create feature view
client.createFeatureView("featureview1", "", "SELECT name, age, amount FROM test_db.user LAST JOIN test_db.trade ON test_db.user.name = test_db.trade.user_name");

// Create feature service
client.createFeatureService("featureservice1", "featureview1");

// Test feature service
client.requestFeatureService("featureservice1", "{\"input\": [[\"abc\", 22]]}");

// Cleanup resources
client.deleteFeatureService("featureservice1");
client.deleteFeatureView("featureview1");
client.executeSql("DROP TABLE test_db.user");
client.executeSql("DROP TABLE test_db.trade");
client.executeSql("DROP DATABASE test_db");
      </pre>
    </a-collapse-panel>
    <a-collapse-panel key="3" header="Single Table And Multiple Feature Views">
      <pre>
// Create test db and tables
client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");

// Create entity
client.createEntity("name", "name");

// Create feature view
client.createFeatureView("featureview1", "name", "SELECT name FROM test_db.user");
client.createFeatureView("featureview2", "name", "SELECT age + 10 AS new_age FROM test_db.user");

// Create feature service
client.createFeatureService("featureservice1", "featureview1, featureview2");

// Test feature service
client.requestFeatureService("featureservice1", "{\"input\": [[\"abc\", 22]]}");

// Cleanup resources
client.deleteFeatureService("featureservice1");
client.deleteFeatureView("featureview1");
client.deleteFeatureView("featureview2");
client.deleteEntity("name");
client.executeSql("DROP TABLE test_db.user");
client.executeSql("DROP DATABASE test_db");
      </pre>
    </a-collapse-panel>
    <a-collapse-panel key="4" header="Multiple Tables And Multiple Feature Views">
      <pre>
// Create test db and tables
client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");
client.executeSql("CREATE TABLE IF NOT EXISTS test_db.trade (user_name string, amount float)");

// Create entity
client.createEntity("name", "name");

// Create feature view
client.createFeatureView("featureview1", "name", "SELECT age + 10 AS new_age FROM test_db.user");
client.createFeatureView("featureview2", "name", "SELECT name, age, amount FROM test_db.user LAST JOIN test_db.trade ON test_db.user.name = test_db.trade.user_name");

// Create feature service
client.createFeatureService("featureservice1", "featureview1, featureview2");

// Test feature service
client.requestFeatureService("featureservice1", "{\"input\": [[\"abc\", 22]]}");

// Cleanup resources
client.deleteFeatureService("featureservice1");
client.deleteFeatureView("featureview1");
client.deleteFeatureView("featureview2");
client.deleteEntity("name");
client.executeSql("DROP TABLE test_db.user");
client.executeSql("DROP TABLE test_db.trade");
client.executeSql("DROP DATABASE test_db");
      </pre>
    </a-collapse-panel>
  </a-collapse>

</div>
</template>
  
<script>
import axios from 'axios'
import { message } from 'ant-design-vue';
import { defineComponent } from 'vue';
import { LeftCircleOutlined, RightCircleOutlined } from '@ant-design/icons-vue';

export default {
  components() {
    return {
      LeftCircleOutlined,
      RightCircleOutlined,
    }
  },

  data() {
    return {
    };
  },

  mounted() {
  },

  methods: {
  },
};
</script>
