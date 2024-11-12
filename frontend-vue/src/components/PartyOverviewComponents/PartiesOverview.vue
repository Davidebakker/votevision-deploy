<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

const parties = ref([]);
async function fetchParties() {
  try {
    const response = await axios.get('http://localhost:8080/api/elections/parties');
    parties.value = response.data;
  } catch (error) {
    console.error('Error fetching party data:', error);
  }
}

onMounted(() => {
  fetchParties();
});
</script>

<template>
  <section class="bg-white dark:bg-gray-900">
    <div class="container px-6 py-10 mx-auto">
      <h1 class="text-2xl font-semibold text-center text-gray-800 capitalize lg:text-3xl dark:text-white">Parties</h1>

      <p class="mt-4 text-center text-gray-500 dark:text-gray-300">
        Bekijk hier de verschillende partijen en hun informatie
      </p>

      <div class="grid grid-cols-1 gap-8 mt-8 xl:mt-12 xl:gap-12 md:grid-cols-2 xl:grid-cols-3">
        <div v-for="party in parties" :key="party.partyId">
          <router-link :to="{ name: 'PartyDetails', params: { name: party.name } }" class="block">
          <img
              class="object-cover w-64 rounded-lg h-64"
              :src="party.logo"
              :alt="`Logo van ${party.name}`"
            />
            <h2 class="mt-4 text-xl font-semibold text-gray-800 capitalize dark:text-white">
              {{ party.name }}
            </h2>
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
</style>
