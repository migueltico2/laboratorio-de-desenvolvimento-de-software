import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import vuetify from "./vuetify";
import router from "./router";
import { createPinia } from "pinia";
import "@mdi/font/css/materialdesignicons.css";
import Vue3Toastify from "vue3-toastify";
import "vue3-toastify/dist/index.css";

const app = createApp(App);
const pinia = createPinia();

app.use(router);
app.use(vuetify);
app.use(pinia);
app.use(Vue3Toastify, {
  theme: "colored",
  autoClose: 3000,
});
app.mount("#app");
