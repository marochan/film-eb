import { createApp } from "vue";
import App from "./App.vue";
import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faFilm,
  faHorseHead,
  faPlus,
  faIcicles,
  faSnowflake,
} from "@fortawesome/free-solid-svg-icons";
// import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faFilm, faHorseHead, faPlus, faIcicles, faSnowflake);

createApp(App).mount("#app");
