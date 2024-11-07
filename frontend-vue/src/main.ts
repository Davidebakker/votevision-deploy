import './assets/main.css'
import './assets/tailwind.css'
import '@fortawesome/fontawesome-free/css/all.css'
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-quartz.css'; // Ensure this matches the theme you're using

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
