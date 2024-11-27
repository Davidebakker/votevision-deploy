<template>
  <div class="national-results">
    <h1 class="text-2xl font-semibold mb-4">National Election Results</h1>
    <label for="chartSelect" class="block mb-4">
      Kies welke data je wilt zien:
      <select id="chartSelect" v-model="selectedChart" @change="updateChart">
        <option value="municipality">Vote's per party</option>
        <option value="party">Vote's per Municipality</option>
        <option value="original">Original Data</option>
      </select>
    </label>
    <ag-charts :options="chartOptions" style="width: 100%; height: 400px;"></ag-charts>
  </div>
</template>

<script>
import { AgCharts } from "ag-charts-vue3";
import axios from "axios";
import { ref, onMounted } from "vue";

export default {
  name: "NationalElectionResultView",
  components: {
    "ag-charts": AgCharts,
  },
  setup() {
    const selectedChart = ref("party");
    const rawData = ref([]);
    const chartOptions = ref({
      data: [],
      series: [],
      axes: [],
      title: {
        text: "Loading...",
      },
    });

    // Data ophalen
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/elections/results"
        );
        console.log("API Response Data:", response.data);
        rawData.value = response.data;
        updateChart();
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    // Grafiekopties bijwerken op basis van selectie
    const updateChart = () => {
      let chartData = [];
      let title = "";

      if (selectedChart.value === "party") {
        const groupedByParty = rawData.value.reduce((acc, item) => {
          if (!acc[item.party]) {
            acc[item.party] = { party: item.party, votes: 0 };
          }
          acc[item.party].votes += item.votes;
          return acc;
        }, {});
        chartData = Object.values(groupedByParty);
        title = "Vote per municipality";
      } else if (selectedChart.value === "municipality") {
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
        title = "Vote's per Party";
      } else {
        chartData = rawData.value.map((item) => ({
          party: `${item.party} (${item.municipality})`,
          votes: item.votes,
        }));
        title = "Original Data";
      }

      console.log("Mapped Chart Data:", chartData);

      chartOptions.value = {
        data: chartData,
        title: {
          text: title,
        },
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
            // title: { text: selectedChart.value === "municipality" ? "Party" : "municipality" },
          },
          {
            type: "number",
            position: "left",
            title: { text: "Vote's" },
          },
        ],
      };
    };

    onMounted(fetchData);

    return {
      selectedChart,
      chartOptions,
      updateChart,
    };
  },
};
</script>

<style scoped>
.national-results {
  padding: 2rem;
  color: #2376a8;
}
</style>
