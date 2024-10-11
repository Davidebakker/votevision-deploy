<template>
  <div id="map-container">
    <div id="map"></div>
  </div>
</template>

<script>
export default {
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
      document.head.appendChild(countryMapScript);
    }
  }
}
</script>

<style scoped>
#map-container {
  display: flex;
  justify-content:flex-start;
  width: 300px;
  height: 500px;
  background-color: #aeaeae;
}

#map {
  width: 300px;
  height: 500px;
  background-color: #aeaeae;
}
</style>
