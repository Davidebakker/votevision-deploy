<template>
  <div>
    <h1>{{ party.name }}</h1>
    <img :src="party.logo" alt="Party Logo" />
    <p>Aantal zetels: {{ party.seats }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: ['name'],
  data() {
    return {
      party: {}, // Hier komen de details van de partij in
    };
  },
  created() {
    this.fetchPartyDetails();
  },
  methods: {
    async fetchPartyDetails() {
      console.log("Fetching details for party:", this.name);
      try {
        const response = await axios.get(`http://localhost:8080/api/elections/party/${this.name}`);

        this.party = response.data;
        console.log("Party details:", this.party); // Log de ontvangen partijgegevens
      } catch (error) {
        console.error("Er is een fout opgetreden bij het ophalen van de partijdetails:", error);
      }
    },
  },
};
</script>
