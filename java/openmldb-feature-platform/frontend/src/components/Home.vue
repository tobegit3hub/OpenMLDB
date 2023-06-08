<script setup>

import TableList from "./TableList.vue";
import EntitiesList from "./EntitiesList.vue";
import FeatureViewList from "./FeatureViewList.vue"
import FeatureList from "./FeatureList.vue"
import FeatureServiceList from "./FeatureServiceList.vue"

import { Button, DatePicker } from 'ant-design-vue';
</script>

<template>

<body>
  <div>
    <TableList />
  </div>

  <div>
    <EntitiesList />
  </div>
  
  <div>
    <FeatureViewList />
  </div>

  <div>
    <FeatureList />
  </div>

  <div>
    <FeatureServiceList />
  </div>
</body>

</template>

<style scoped>
</style>
