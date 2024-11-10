import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import vuetify from './vuetify'
import router from './router'
import '@mdi/font/css/materialdesignicons.css'

const app = createApp(App)

app.use(router)
app.use(vuetify)
app.mount('#app')
