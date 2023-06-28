<script setup>
</script>

<template>

<a-layout class="layout">
  <a-layout-sider :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0, top: 0, bottom: 0 }">
    <a-menu theme="dark" mode="inline" class="navi-menu">
      <a-menu-item key="1">
        <router-link to='/'>Overview</router-link>
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
  </a-layout-sider>

  <a-layout :style="{ marginLeft: '200px' }" style="padding: 20px 50px">
    <a-layout-content>
        <router-view></router-view>
    </a-layout-content>

    
    <a-layout-footer style="text-align: center">
      <br />
      <div>
        <a href="https://github.com/4paradigm/OpenMLDB" target="_blank">OpenMLDB</a> © 2021-2023
      </div>
    </a-layout-footer>
  </a-layout>

</a-layout>





</template>

<style>
.navi-menu .ant-menu-item {
  font-size: 16px;
  margin: 10px 10px;
}
</style>