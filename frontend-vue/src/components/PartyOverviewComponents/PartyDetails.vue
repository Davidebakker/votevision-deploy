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

    onMounted(fetchPartyDetails);

    watch(() => props.name, fetchPartyDetails);

    return { party };
  },
};
</script>

<template>
  <div class="min-h-screen bg-gray-200 flex flex-col items-center justify-start py-10">
    <div class="w-full bg-gray-700 text-white py-6 px-8 mb-12 text-center">
      <h1 class="text-4xl font-extrabold">{{ party.name }}</h1>
    </div>

    <div class="w-full max-w-6xl bg-white shadow-lg rounded-2xl overflow-hidden">
      <div class="flex flex-col md:flex-row items-center p-8">

        <div class="flex justify-center md:w-1/3 mb-6 md:mb-0">
          <img :src="party.logo" alt="Party Logo" class="w-60 h-60 object-cover">
        </div>

        <div class="md:w-2/3 md:pl-10">
          <p class="text-xl font-medium text-gray-700 mb-4">
            <span class="font-bold text-gray-800">Aantal zetels:</span> {{ party.seats }}
          </p>

          <div class="mt-4 text-lg text-gray-600 leading-relaxed">
            <p>{{ party.description }}</p>
          </div>
        </div>
      </div>
    </div>

    <div class="w-full max-w-6xl bg-white shadow-lg rounded-2xl overflow-hidden mt-8 p-8">
      <h2 class="text-2xl font-semibold mb-6">Kandidaten:</h2>
      <ul>
        <li v-for="candidate in party.candidates" :key="candidate.firstName + candidate.lastName" class="mt-4">
          <div class="border p-4 rounded-lg shadow-sm hover:bg-gray-100 transition">
            <p class="font-bold">{{ candidate.firstName }} {{ candidate.initials }} {{ candidate.lastName }}</p>
            <p class="text-gray-600">{{ candidate.locality }}</p>
          </div>
        </li>
      </ul>
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
</style>

