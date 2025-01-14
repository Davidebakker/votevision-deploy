<template>
  <div class="h-screen flex flex-col md:flex-row overflow-hidden bg-gray-100">
    <NavBar />
    <div class="flex-grow p-4 overflow-auto">
      <router-view />
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBarComponents/NavBar.vue'

export default {
  name: 'NavBarView',
  components: {
    NavBar
  }
}
</script>

<style scoped>
/* Ensure the container spans full height */
html, body, #app {
  height: 100%;
  margin: 0;
}
</style>
