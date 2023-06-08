<script setup>

import TableList from "./TableList.vue";
import EntitiesData from "./EntitiesData.vue";
import FeatureViewList from "./FeatureViewList.vue"
import FeatureList from "./FeatureList.vue"
import FeatureServiceList from "./FeatureServiceList.vue"

</script>

<template>

  <div>
    <TableList />
  </div>

  <div>
    <EntitiesData />
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

</template>

<style scoped>
</style>
