<template>
  <div class="app-container flex">
    <Navbar />

    <div class="content flex-grow p-8">
      <router-view />
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/NavBarComponents/NavBar.vue'

export default {
  name: 'NavBarView',
  components: {
    Navbar
  }
}
</script>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
}

.content {
  flex-grow: 1;
  padding: 20px;
}
</style>
