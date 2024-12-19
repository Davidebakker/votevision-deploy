<template>
  <div class="national-results bg-gray-50 min-h-screen px-4 sm:px-6 lg:px-8 py-8">
    <div class="max-w-4xl mx-auto">
      <h1 class="text-3xl md:text-4xl font-bold mb-2 text-gray-800">National Election Results</h1>
      <p class="text-gray-600 mb-6 text-sm sm:text-base">Compare different datasets by switching between views.</p>

      <div class="flex flex-wrap gap-3 mb-6">
        <button
          @click="handleSwitch('municipality')"
          :class="selectedChart === 'municipality'
            ? 'bg-blue-600 text-white font-semibold rounded-lg px-4 py-2 transition-all'
            : 'bg-gray-200 text-gray-800 hover:bg-gray-300 font-semibold rounded-lg px-4 py-2 transition-all'"
        >
          Vote's per Party
        </button>
        <button
          @click="handleSwitch('party')"
          :class="selectedChart === 'party'
            ? 'bg-blue-600 text-white font-semibold rounded-lg px-4 py-2 transition-all'
            : 'bg-gray-200 text-gray-800 hover:bg-gray-300 font-semibold rounded-lg px-4 py-2 transition-all'"
        >
          Vote's per Municipality
        </button>
        <button
          @click="handleSwitch('original')"
          :class="selectedChart === 'original'
            ? 'bg-blue-600 text-white font-semibold rounded-lg px-4 py-2 transition-all'
            : 'bg-gray-200 text-gray-800 hover:bg-gray-300 font-semibold rounded-lg px-4 py-2 transition-all'"
        >
          Original Data
        </button>
      </div>

      <div class="shadow-lg bg-white p-6 rounded-lg overflow-hidden">
        <ag-charts :options="chartOptions" style="width: 100%; height: 400px;"></ag-charts>
      </div>
    </div>
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
      theme: {
        palette: {
          fills: ["#2196f3", "#66bb6a", "#ffca28", "#ab47bc", "#ef5350"],
          strokes: ["#1e88e5"],
        },
        overrides: {
          cartesian: {
            title: {
              fontSize: 18,
              fontWeight: 'bold',
              fontFamily: 'sans-serif',
              padding: { top: 20, bottom: 20 },
            },
            axes: {
              category: {
                label: {
                  fontSize: 12,
                  fontFamily: 'sans-serif'
                }
              },
              number: {
                label: {
                  fontSize: 12,
                  fontFamily: 'sans-serif'
                }
              }
            },
            series: {
              bar: {
                label: {
                  fontSize: 12,
                  fontFamily: 'sans-serif'
                }
              }
            }
          }
        }
      },
    });

    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/elections/results");
        rawData.value = response.data;
        updateChart();
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

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

      chartOptions.value = {
        ...chartOptions.value,
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
          },
          {
            type: "number",
            position: "left",
            title: { text: "Vote's" },
          },
        ],
      };
    };

    const handleSwitch = (value) => {
      selectedChart.value = value;
      updateChart();
    };

    onMounted(fetchData);

    return {
      selectedChart,
      chartOptions,
      updateChart,
      handleSwitch,
    };
  },
};
</script>

<style scoped>
.national-results {
  font-family: sans-serif;
}
</style>
