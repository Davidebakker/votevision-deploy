<template>
  <div id="map-container">
    <div id="map"></div>
    <div id="election-results" v-if="selectedProvince">
      <h3>Election Results for {{ selectedProvinceName }}</h3>
      <div v-if="isLoading">
        <p>Loading data...</p>
      </div>
      <div v-else-if="electionData">
        <p><strong>Winning Party:</strong> {{ electionData.winningParty }}</p>
        <p><strong>Total Votes:</strong> {{ electionData.totalVotes }}</p>
      </div>
      <div v-else-if="error">
        <p>{{ error }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      selectedProvince: null,
      selectedProvinceName: "",
      electionData: null,
      isLoading: false,
      error: null,
    };
  },
  mounted() {
    this.loadMapScripts();
  },
  methods: {
    loadMapScripts() {
      const mapDataScript = document.createElement('script');
      mapDataScript.src = '/maps/mapdata.js';
      mapDataScript.onload = this.loadCountryMap;
      document.head.appendChild(mapDataScript);
    },
    loadCountryMap() {
      const countryMapScript = document.createElement('script');
      countryMapScript.src = '/maps/countrymap.js';
      countryMapScript.onload = this.addMapFunctions;
      document.head.appendChild(countryMapScript);
    },
    addMapFunctions() {
      for (let stateKey in simplemaps_countrymap_mapdata.state_specific) {
        let province = simplemaps_countrymap_mapdata.state_specific[stateKey];
        let element = document.getElementById(stateKey);

        if (element) {
          element.addEventListener('click', () => {
            this.selectedProvince = stateKey;
            this.selectedProvinceName = province.name;
            this.fetchElectionData(stateKey);
          });
        }
      }
    },
    fetchElectionData(provinceId) {
      this.isLoading = true;
      this.electionData = null;
      this.error = null;
      axios.get(`/api/elections/province/${provinceId}`)
        .then(response => {
          this.electionData = response.data;
          this.isLoading = false;
        })
        .catch(error => {
          console.error("Error fetching election data:", error);
          this.error = "Failed to load election data. Please try again.";
          this.isLoading = false;
        });
    }
  }
};
</script>

<style scoped>
#map-container {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  height: 100%;
}

#map {
  width: 300px;
  height: 400px;
  border: 5px solid #f8f9fa;
  background-color: #1a1a1a;
  cursor: pointer;
}

#election-results {
  margin-right: 20px;
  color: white;
  padding: 10px;
  border: 2px solid #f8f9fa;
  background-color: rgba(0, 0, 0, 0.7);
  border-radius: 5px;
}
</style>
