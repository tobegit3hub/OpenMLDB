<script setup>

import TableList from "./components/TableList.vue";
import EntitiesList from "./components/EntitiesList.vue";
import FeatureViewList from "./components/FeatureViewList.vue"
import FeatureList from "./components/FeatureList.vue"
import FeatureServiceList from "./components/FeatureServiceList.vue"

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
