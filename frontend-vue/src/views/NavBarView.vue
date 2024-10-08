<template>
  <div id="app" class="app-container">
    <Navbar />
  </div>
</template>

<script>
import Navbar from '@/components/NavBarComponents/NavBar.vue'
export default {
  name: 'App',
  components: {
    Navbar
  }
}
</script>

<style scoped>
.app-container {
  padding: 0;
  margin: 0;
}
</style>
