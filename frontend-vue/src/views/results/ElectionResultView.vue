<template>
  <div id="map" style="width: 100%; height: 500px"></div>
</template>

<script>
export default {
  mounted() {
    // Dynamisch de scriptbestanden inladen
    this.loadMapScripts()
  },
  methods: {
    loadMapScripts() {
      // Voeg de mapdata.js toe
      const mapDataScript = document.createElement('script')
      mapDataScript.src = '/maps/mapdata.js' // Zorg ervoor dat dit naar de juiste locatie wijst
      mapDataScript.onload = this.loadCountryMap // Laad de kaart zodra dit script geladen is
      document.head.appendChild(mapDataScript)
    },
    loadCountryMap() {
      // Voeg het countrymap.js bestand toe
      const countryMapScript = document.createElement('script')
      countryMapScript.src = '/maps/countrymap.js' // Zorg ervoor dat dit naar de juiste locatie wijst
      document.head.appendChild(countryMapScript)
    }
  }
}
</script>

<style scoped>
#map {
  width: 50%;
  height: 50%;
  margin: 0 auto;
}
</style>
