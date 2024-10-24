<!--
  - Copyright (c) 2024 Leyramu. All rights reserved.
  - This project (Lersosa), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
  - For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
  - The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
  - By using this project, users acknowledge and agree to abide by these terms and conditions.
  -->

<template>

  <div
      :class="classObj"
      :style="{ '--current-color': theme }"
      class="app-wrapper">

    <div
        v-if="device === 'mobile' && sidebar.opened"
        class="drawer-bg"
        @click="handleClickOutside" />

    <sidebar
        v-if="!sidebar.hide"
        class="sidebar-container" />

    <div
        :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }"
        class="main-container">

      <div :class="{ 'fixed-header': fixedHeader }">

        <navbar @setLayout="setLayout" />

        <tags-view v-if="needTagsView" />
      </div>

      <app-main />

      <settings ref="settingRef" />
    </div>
  </div>
</template>

<script setup>
  import {useWindowSize} from '@vueuse/core'
  import Sidebar from './components/Sidebar/index.vue'
  import {AppMain, Navbar, Settings, TagsView} from './components/index'

  import useAppStore from '@/store/modules/app'
  import useSettingsStore from '@/store/modules/settings'

  const settingsStore = useSettingsStore()
  const theme = computed(() => settingsStore.theme);
  const sideTheme = computed(() => settingsStore.sideTheme);
  const sidebar = computed(() => useAppStore().sidebar);
  const device = computed(() => useAppStore().device);
  const needTagsView = computed(() => settingsStore.tagsView);
  const fixedHeader = computed(() => settingsStore.fixedHeader);

  const classObj = computed(() => ({
    hideSidebar: !sidebar.value.opened,
    openSidebar: sidebar.value.opened,
    withoutAnimation: sidebar.value.withoutAnimation,
    mobile: device.value === 'mobile'
  }))

  const {width, height} = useWindowSize();
  const WIDTH = 992; // refer to Bootstrap's responsive design

  watchEffect(() => {
    if (device.value === 'mobile' && sidebar.value.opened) {
      useAppStore().closeSideBar({withoutAnimation: false})
    }
    if (width.value - 1 < WIDTH) {
      useAppStore().toggleDevice('mobile')
      useAppStore().closeSideBar({withoutAnimation: true})
    } else {
      useAppStore().toggleDevice('desktop')
    }
  })

  function handleClickOutside() {
    useAppStore().closeSideBar({withoutAnimation: false})
  }

  const settingRef = ref(null);

  function setLayout() {
    settingRef.value.openSetting();
  }
</script>

<style
    lang="scss"
    scoped>
  @import "@/assets/styles/mixin.scss";
  @import "@/assets/styles/variables.module.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$base-sidebar-width});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px);
  }

  .sidebarHide .fixed-header {
    width: 100%;
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
