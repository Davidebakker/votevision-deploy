<template>
  <div class="app-container flex">
    <Navbar />

    <div class="flex-1">
      <RouterView />
    </div>
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/components/NavBarComponents/NavBar.vue'
</script>

<style scoped>
.app-container {
  display: flex;
  height: 100%;
  width: 100%;
}

.flex-1 {
  flex-grow: 1;
  background-color: #1a1a1a;
}
</style>

