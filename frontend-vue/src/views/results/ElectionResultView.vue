<template>
  <div class="flex flex-col items-center bg-gray-900 min-h-screen text-white">
    <div id="map-container" class="w-full flex justify-center p-6">
      <!-- The map will be dynamically injected here -->
      <div id="map" class="w-full lg:w-3/4 xl:w-1/2 h-[600px]"></div>
    </div>
    <div id="election-summary" class="w-full max-w-4xl p-6 mt-6 bg-gray-800 rounded-lg shadow-lg">
      <h2 class="text-2xl font-semibold mb-4">Overall Election Information in the Netherlands 2023</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="bg-gray-700 p-4 rounded-lg shadow-md">
          <h3 class="text-xl font-bold">Total Voting Locations</h3>
          <p class="text-lg mt-2">19,226 voting locations across the country.</p>
        </div>
        <div class="bg-gray-700 p-4 rounded-lg shadow-md">
          <h3 class="text-xl font-bold">Average Turnout Rate</h3>
          <p class="text-lg mt-2">Estimated to be around 76.5% nationwide.</p>
        </div>
        <div class="bg-gray-700 p-4 rounded-lg shadow-md">
          <h3 class="text-xl font-bold">Most Popular Party</h3>
          <p class="text-lg mt-2">"Party PVV" with 32% of the votes.</p>
        </div>
        <div class="bg-gray-700 p-4 rounded-lg shadow-md">
          <h3 class="text-xl font-bold">Highest Turnout Province</h3>
          <p class="text-lg mt-2">Zuid-Holland with 82% turnout.</p>
        </div>
      </div>
    </div>
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
      countryMapScript.onload = this.addMapFunctions;
      document.head.appendChild(countryMapScript);
    },
    addMapFunctions() {
      // Optional: Add any custom functionality to the map here if needed
      console.log("Map is ready and functional");
    }
  }
};
</script>

<style scoped>
/* Remove the previous styling to let TailwindCSS handle everything */
#map {
  /* Set to full width and appropriate height using TailwindCSS */
}
</style>
