<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script setup>
const url = ref('https://lersosa.leyramu.top/');

function goto() {
  window.open(url.value);
}
</script>
