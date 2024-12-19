// main.ts
import './assets/main.css';
import './assets/tailwind.css';
import '@fortawesome/fontawesome-free/css/all.css';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-quartz.css';
import { createApp } from 'vue';
import { createPinia } from 'pinia';

import App from './App.vue';
import router from './router';

const app = createApp(App);

// Check if `window.agCharts` is loaded; if not, log a warning
if (!window.agCharts) {
  console.warn("Warning: agCharts library not loaded. Make sure the script URL is correct in index.html.");
}

app.use(createPinia());
app.use(router);

app.mount('#app');
