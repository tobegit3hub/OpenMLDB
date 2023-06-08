<script setup>
import TableList from "./TableList.vue";
import EntitiesData from "./EntitiesData.vue";
import FeatureViewList from "./FeatureViewList.vue"
import FeatureList from "./FeatureList.vue"
import FeatureServiceList from "./FeatureServiceList.vue"
</script>

<template>

<a-layout class="layout">
  <a-layout-header>
    <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
      <a-menu-item key="1">
        <router-link to='/'>Home</router-link>
      </a-menu-item>
      <a-menu-item key="2">
        <router-link to='/tables'>Tables</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to='/entities'>Entities</router-link>
      </a-menu-item>
      <a-menu-item key="4">
        <router-link to='/features'>Features</router-link>
      </a-menu-item>
      <a-menu-item key="5">
        <router-link to='/featureviews'>Feature Views</router-link>
      </a-menu-item>
      <a-menu-item key="6">
        <router-link to='/featureservices'>Feature Services</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</a-layout>

<router-view></router-view>

</template>

<style scoped>
</style>