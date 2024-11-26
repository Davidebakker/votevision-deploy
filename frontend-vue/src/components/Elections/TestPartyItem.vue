<script>
import { useRoute } from 'vue-router';
import axios from 'axios'

;
import { onMounted } from 'vue'

export default {
  setup() {
    const route = useRoute();
    const partyName = route.params.partyName;

    async function fetchPartyData() {
      try {
        const response = await axios.get(`http://localhost:8080/api/elections/party/${partyName}`);
        console.log(response.data);
      } catch (error) {
        console.error("Error fetching party data:", error);
      }
    }

    onMounted(() => fetchPartyData())

    return {};
  }
}
</script>



<template>

</template>

<style scoped>

</style>