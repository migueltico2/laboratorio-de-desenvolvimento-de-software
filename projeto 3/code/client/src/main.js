import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import vuetify from './vuetify'
import router from './router'

const app = createApp(App)

app.use(router)
app.use(vuetify)
app.mount('#app')
