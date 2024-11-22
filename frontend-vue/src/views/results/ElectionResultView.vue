<template>
  <div class="flex flex-col items-center bg-gray-900 min-h-screen text-white">
    <div id="map-container" class="w-full flex justify-center p-6">
      <!-- The map -->
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

    <!--Candidate Data -->
    <div class="w-full max-w-4xl p-6 mt-6 bg-gray-800 rounded-lg shadow-lg">
      <h2 class="text-2xl font-semibold mb-4">Candidate Data</h2>
      <ag-grid-vue
        style="width: 100%; height: 500px;"
        class="ag-theme-quartz-dark"
        :columnDefs="columnDefs"
        @grid-ready="onGridReady"
        :rowData="rowData"
        :defaultColDef="defaultColDef"
        :pagination="true"
        :paginationPageSize="paginationPageSize">
      </ag-grid-vue>
    </div>
  </div>
</template>

<script>
import { AgGridVue } from 'ag-grid-vue3';
import { ref, onMounted } from 'vue';
import axios from 'axios'

;

export default {
  components: {
    'ag-grid-vue': AgGridVue
  },
  setup() {
    const columnDefs = ref([
      { field: "firstName", headerName: "First Name", editable: true },
      { field: "lastName", headerName: "Last Name" },
      { field: "locality", headerName: "Locality" },
      {
        field: "party",
        headerName: "Party",
        valueGetter: params => params.data.party ? params.data.party.name : 'No Party'
      }
    ]);


    const rowData = ref(null);
    const defaultColDef = ref({ sortable: true, filter: true, floatingFilter: true });
    const paginationPageSize = ref(10);

    onMounted(async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/elections/candidate');
        rowData.value = response.data;
      } catch (error) {
        console.error("Error fetching data", error);
      }
    });

    const onGridReady = (params) => {
      params.api.sizeColumnsToFit();
    };

    return {
      columnDefs,
      rowData,
      defaultColDef,
      paginationPageSize,
      onGridReady
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
      console.log("Map is ready and functional");
    }
  }
};
</script>

<style scoped>

#map {
}
</style>
