<script setup lang="ts">
import HelloWorld from './views/NavBarView.vue'
</script>


<template>
  <header>
    <div class="wrapper">
      <HelloWorld msg="You did it!" />
    </div>
  </header>

  <RouterView />
</template>

<style scoped>

</style>
