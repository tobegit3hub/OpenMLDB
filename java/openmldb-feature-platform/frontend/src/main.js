import './assets/main.css'

import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

axios.defaults.baseURL = import.meta.env.VITE_API_ENDPOINT;

const app = createApp(App)
app.use(router)
app.use(Antd)
app.mount('#app')
