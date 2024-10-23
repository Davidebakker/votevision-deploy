<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute();
const partyName = route.params.partyName;
const party = ref(null);
async function fetchPartyDetails() {
  try {
    const response = await axios.get(`http://localhost:8080/api/elections/party/${partyName}`);
    party.value = response.data;
  } catch (error) {
    console.error('Error fetching party details:', error);
  }
}

onMounted(() => {
  fetchPartyDetails();
});
</script>

<template>
  <section v-if="party" class="container mx-auto p-6">
    <h1 class="text-2xl font-bold">{{ party.name }}</h1>
    <img :src="party.logo" alt="Party Logo" class="w-64 h-64 my-4" />
    <h2 class="text-lg font-semibold">Kandidaten:</h2>
<!--    <ul>-->
<!--      <li v-for="candidate in party.candidates" :key="candidate.candidateId">-->
<!--        {{ candidate.name }}-->
<!--      </li>-->
<!--    </ul>-->
  </section>
  <p v-else>Loading...</p>
</template>

<style scoped>

</style>
