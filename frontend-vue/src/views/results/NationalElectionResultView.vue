<template>
  <div class="nationalElectionResult">
    <h1>This gives the national election results</h1>
  </div>

  <NationalResult />
</template>

<style scoped>

</style>

<script>
import NationalResult from '@/components/NationalResultItem.vue'
export default {
  name: 'App',
  components: {
    NationalResult
  }
}
</script>