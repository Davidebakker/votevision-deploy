<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

interface Party {
  partyId: number;
  name: string;
  logo: string;
  seats: number;
  description: string;
}

const parties = ref<Party[]>([]);
const searchQuery = ref('');
const sortOption = ref('seatsDesc');

async function fetchParties() {
  try {
    const response = await axios.get<Party[]>('http://localhost:8080/api/elections/parties');
    parties.value = response.data;
  } catch (error) {
    console.error('Error fetching party data:', error);
  }
}

const filteredAndSortedParties = computed(() => {
  const filteredParties = parties.value.filter((party) =>
    party.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );

  switch (sortOption.value) {
    case 'nameAsc':
      return filteredParties.sort((a, b) => a.name.localeCompare(b.name));
    case 'nameDesc':
      return filteredParties.sort((a, b) => b.name.localeCompare(a.name));
    case 'seatsAsc':
      return filteredParties.sort((a, b) => a.seats - b.seats);
    case 'seatsDesc':
      return filteredParties.sort((a, b) => b.seats - a.seats);
    default:
      return filteredParties;
  }
});

onMounted(() => {
  fetchParties();
});
</script>

<template>
  <section class="bg-white dark:bg-gray-900">
    <div class="container px-6 py-10 mx-auto">
      <h1 class="text-2xl font-semibold text-center text-gray-800 capitalize lg:text-3xl dark:text-white">
        Parties
      </h1>
      <p class="mt-4 text-center text-gray-500 dark:text-gray-300">
        View the different parties and their information here
      </p>

      <div class="mt-4 text-center">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search for a party..."
          class="px-4 py-2 border rounded-lg dark:border-gray-600 dark:bg-gray-700 dark:text-white"
        />
      </div>

      <div class="mt-4 text-center">
        <select v-model="sortOption" class="px-4 py-2 border rounded-lg dark:border-gray-600 dark:bg-gray-700 dark:text-white">
          <option value="nameAsc">Sort by Name (A-Z)</option>
          <option value="nameDesc">Sort by Name (Z-A)</option>
          <option value="seatsAsc">Sort by Seats (Low to High)</option>
          <option value="seatsDesc">Sort by Seats (High to Low)</option>
        </select>
      </div>

      <div class="grid grid-cols-1 gap-8 mt-8 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
        <div v-for="party in filteredAndSortedParties" :key="party.partyId" class="flex flex-col items-center">
          <router-link
            :to="{ name: 'PartyDetails', params: { name: party.name } }"
            class="router-link-wrapper">
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
.router-link-wrapper img {
  border-radius: 1.25rem;
}

.router-link-wrapper {
  border-radius: 1.25rem;
  padding: 2.5rem;
}

body.dark input[type="text"],
body.dark select {
  background-color: #2d3748;
  color: #edf2f7;
  border: 1px solid #4a5568;
}

body.dark input[type="text"]:focus,
body.dark select:focus {
  border-color: #63b3ed;
  outline: none;
}

body.dark input[type="text"]:hover,
body.dark select:hover {
  background-color: #4a5568;
}
</style>
