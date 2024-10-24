<template>
  <router-view/>
</template>

<script setup>
import useSettingsStore from '@/store/modules/settings.js'
import {handleThemeStyle} from '@/utils/theme.js'

onMounted(() => {
  nextTick(() => {
    handleThemeStyle(useSettingsStore().theme)
  })
})
</script>
