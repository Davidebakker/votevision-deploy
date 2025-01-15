<script>
import axios from 'axios';
import { onMounted, ref, watch } from 'vue';

export default {
  props: ['name'],
  setup(props) {
    const party = ref({});
    const fetchPartyDetails = async () => {
      console.log("Fetching details for party:", props.name);
      try {
        const response = await axios.get(`http://localhost:8080/api/elections/party/${props.name}`);
        party.value = response.data;
      } catch (error) {
        if (error.response && error.response.status === 404) {
          console.error("De partij bestaat niet. Controleer de ingevoerde naam.");
        } else {
          console.error("Er is een fout opgetreden:", error);
        }
      }
    };

    onMounted(() => {
      if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        document.documentElement.classList.add('dark'); // Voeg de 'dark' class toe aan de html tag
      }
      fetchPartyDetails();
    });

    watch(() => props.name, fetchPartyDetails);

    return { party };
  },
};
</script>

<template>
  <div class="min-h-screen bg-gray-200 dark:bg-gray-900 flex flex-col items-center py-10">
    <div class="w-full bg-gray-700 text-white py-6 px-8 mb-12 text-center">
      <h1 class="text-4xl font-extrabold">{{ party.name }}</h1>
    </div>

    <div class="w-full max-w-6xl bg-white dark:bg-gray-800 shadow-lg rounded-2xl overflow-hidden">
      <div class="flex flex-col md:flex-row items-center p-8">
        <div class="flex justify-center md:w-1/3 mb-6 md:mb-0">
          <img :src="party.logo" alt="Party Logo" class="w-40 h-40 md:w-60 md:h-60 object-cover rounded-lg">
        </div>
        <div class="md:w-2/3 md:pl-10">
          <p class="text-xl font-medium text-gray-700 dark:text-gray-300 mb-4">
            <span class="font-bold text-gray-800 dark:text-white">Number of seats:</span> {{ party.seats }}
          </p>
          <div class="mt-4 text-lg text-gray-600 dark:text-gray-200 leading-relaxed">
            <p>{{ party.description }}</p>
          </div>
        </div>
      </div>
    </div>

    <div class="w-full max-w-6xl bg-white dark:bg-gray-800 shadow-lg rounded-2xl overflow-hidden mt-8 p-8">
      <h2 class="text-2xl font-semibold mb-6 text-gray-600 dark:text-gray-200">Candidates:</h2>
      <div class="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="candidate in party.candidates"
          :key="candidate.firstName + candidate.lastName"
          class="border p-4 rounded-lg shadow-sm hover:bg-gray-100 dark:hover:bg-gray-700 transition"
        >
          <p class="font-bold text-gray-600 dark:text-gray-300">
            {{ candidate.firstName }} {{ candidate.initials }} {{ candidate.lastNamePrefix }} {{ candidate.lastName }}
          </p>
          <p class="text-gray-600 dark:text-gray-400">{{ candidate.locality }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f7fafc;
}

p {
  margin: 10px 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

/* Styles for input fields and dropdown */
input[type="text"],
select {
  padding: 8px 12px;
  margin: 8px 0;
  border-radius: 0.375rem;
  background-color: #f7fafc;
  border: 1px solid #d1d5db;
  color: #2d3748;
}

input[type="text"]:focus,
select:focus {
  border-color: #4c51bf;
  outline: none;
}

/* Dark mode styles for input fields and dropdown */
body.dark input[type="text"],
body.dark select {
  background-color: #2d3748;
  color: #edf2f7;
  border: 1px solid #4a5568;
}

body.dark input[type="text"]:focus,
body.dark select:focus {
  border-color: #63b3ed;
}
</style>
