<template>
  <div class="national-results">
    <h1 class="text-2xl font-semibold mb-4">National Election Results</h1>
    <label for="chartSelect" class="block mb-4">
      Kies welke data je wilt zien:
      <select id="chartSelect" v-model="selectedChart">
        <option value="party">Stemmen per Partij</option>
        <option value="municipality">Stemmen per Gemeente</option>
        <option value="original">Originele Data</option>
      </select>
    </label>
    <button class="bg-blue-500 text-white px-4 py-2 rounded" @click="updateChart">
      Update Grafiek
    </button>
    <div id="myChart" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script>
import { AgCharts } from "ag-charts-community";
import axios from "axios";
import { onMounted, ref, watch } from "vue";

export default {
  name: "NationalElectionResultView",
  setup() {
    const chartInstance = ref(null);
    const selectedChart = ref("party");
    const rawData = ref([]);

    // Data ophalen
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/elections/results"
        );
        console.log("API Response Data:", response.data);
        rawData.value = response.data; // Opslaan originele data
        updateChart(); // Update grafiek na data ophalen
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    // Grafiek updaten
    const updateChart = () => {
      let chartData = [];
      let title = "";

      if (selectedChart.value === "party") {
        // Groepeer data per partij
        const groupedByParty = rawData.value.reduce((acc, item) => {
          if (!acc[item.party]) {
            acc[item.party] = { party: item.party, votes: 0 };
          }
          acc[item.party].votes += item.votes;
          return acc;
        }, {});
        chartData = Object.values(groupedByParty);
        title = "Stemmen per Partij";
      } else if (selectedChart.value === "municipality") {
        // Groepeer data per gemeente
        const groupedByMunicipality = rawData.value.reduce((acc, item) => {
          if (!acc[item.municipality]) {
            acc[item.municipality] = { municipality: item.municipality, votes: 0 };
          }
          acc[item.municipality].votes += item.votes;
          return acc;
        }, {});
        chartData = Object.values(groupedByMunicipality).map((item) => ({
          party: item.municipality,
          votes: item.votes,
        }));
        title = "Stemmen per Gemeente";
      } else {
        // Gebruik originele data
        chartData = rawData.value.map((item) => ({
          party: `${item.party} (${item.municipality})`,
          votes: item.votes,
        }));
        title = "Originele Data";
      }

      console.log("Mapped Chart Data:", chartData);

      const chartOptions = {
        container: document.getElementById("myChart"),
        title: {
          text: title,
        },
        data: chartData,
        series: [
          {
            type: "bar",
            xKey: "party",
            yKey: "votes",
            label: {
              formatter: ({ value }) => String(value),
            },
            tooltip: {
              renderer: ({ datum, xKey, yKey }) => ({
                title: String(datum[xKey]),
                content: `${datum[yKey]} stemmen`,
              }),
            },
          },
        ],
        axes: [
          {
            type: "category",
            position: "bottom",
            title: { text: selectedChart.value === "municipality" ? "Gemeente" : "Partij" },
          },
          {
            type: "number",
            position: "left",
            title: { text: "Stemmen" },
          },
        ],
      };

      console.log("Chart Options:", chartOptions);

      // Grafiek updaten of opnieuw maken
      if (chartInstance.value) {
        try {
          chartInstance.value.update(chartOptions);
        } catch (error) {
          console.warn("Error updating chart, recreating...", error);
          chartInstance.value.destroy();
          chartInstance.value = AgCharts.create(chartOptions);
        }
      } else {
        chartInstance.value = AgCharts.create(chartOptions);
      }
    };


    // Herlaad de grafiek bij selectie van een andere optie
    watch(selectedChart, () => {
      updateChart();
    });

    // Data ophalen bij het laden van de component
    onMounted(fetchData);

    return {
      selectedChart,
      updateChart,
    };
  },
};
</script>

<style scoped>
.national-results {
  padding: 2rem;
  color: white;
}
</style>
