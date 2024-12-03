<script>
import axios from 'axios';

export default {
  props: ['name'],
  data() {
    return {
      party: {},
    };
  },
  created() {
    this.fetchPartyDetails();
  },
  methods: {
    async fetchPartyDetails() {
      console.log("Fetching details for party:", this.name);
      try {
        const response = await axios.get(`http://localhost:8080/api/elections/party/d66`);
        this.party = response.data;
      } catch (error) {
        if (error.response && error.response.status === 404) {
          console.error("De partij bestaat niet. Controleer de ingevoerde naam.");
        } else {
          console.error("Er is een fout opgetreden:", error);
        }
      }

    },
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
  </div>
</template>

<style scoped>
body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f7fafc;
}

@media (max-width: 768px) {
  .party-logo {
    max-width: 150px;
    max-height: 150px;
  }

  .party-info {
    font-size: 1.1rem;
  }
}

p {
  margin: 10px 0;
}
</style>
