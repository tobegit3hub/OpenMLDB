<script setup>
</script>

<template>

<a-layout class="layout">
  <a-layout-sider :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0, top: 0, bottom: 0 }">
    <a-menu theme="dark" mode="inline" class="navi-menu">
      <a-menu-item key="1">
        <router-link to='/'>{{ $t('Overview') }}</router-link>
      </a-menu-item>
      <a-menu-item key="2">
        <router-link to='/tables'>{{ $t('Feature Tables') }}</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to='/entities'>{{ $t('Entities') }}</router-link>
      </a-menu-item>
      <a-menu-item key="4">
        <router-link to='/features'>{{ $t('Features') }}</router-link>
      </a-menu-item>
      <a-menu-item key="5">
        <router-link to='/featureviews'>{{ $t('Feature Views') }}</router-link>
      </a-menu-item>
      <a-menu-item key="6">
        <router-link to='/featureservices'>{{ $t('Feature Services') }}</router-link>
      </a-menu-item>
      <a-menu-item key="7">
        <router-link to='/sql'>{{ $t('SQL') }}</router-link>
      </a-menu-item>
      <a-menu-item key="8">
        <router-link to='/tutorial'>{{ $t('Tutorial') }}</router-link>
      </a-menu-item>
      <a-sub-menu key="sub4">
        <template #title>{{ $t('Languages') }}</template>
        <a-menu-item key="100" @click="changeLocale('en')">English</a-menu-item>
        <a-menu-item key="101" @click="changeLocale('zh')">中文</a-menu-item>
      </a-sub-menu>
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

<script>
export default {
  methods: {
    changeLocale(locale) {
      this.$i18n.locale = locale;
    }
  }
}
</script>

<style>
.navi-menu .ant-menu-item {
  font-size: 16px;
  margin: 10px 10px;
}
</style>