<template>
  <div class="app-container flex min-h-full">
    <Navbar />

    <div class="content flex-grow p-8 overflow-y-auto">
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
  min-height: 100%;
  height: 100%;
  position: fixed;
}

.content {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
