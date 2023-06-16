<script setup>
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
      <a-menu-item key="7">
        <router-link to='/sql'>SQL</router-link>
      </a-menu-item>
      <a-menu-item key="8">
        <router-link to='/tutorial'>Tutorial</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>

  <a-layout-content style="padding: 0 50px">
      <router-view></router-view>
   </a-layout-content>

   <a-layout-footer style="text-align: center">
    <div>
      <a href="https://github.com/4paradigm/OpenMLDB" target="_blank">OpenMLDB</a> © 2021-2023
    </div>
  </a-layout-footer>

</a-layout>




</template>

<style scoped>
</style>