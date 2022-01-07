<template>
  <navbar />
  <div v-if="store.state.loggedIn == ''">
    <join />
  </div>
  <div v-else>
    <MainPanel />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Navbar from "./components/Navbar.vue";
import Join from "./components/Join.vue";
import { store } from "./store";
import MainPanel from "./components/MainPanel.vue";

@Options({
  components: {
    Navbar,
    Join,
    MainPanel,
  },
})
export default class App extends Vue {
  store = store;
}
</script>

<style>
.header {
  background-color: rgb(223, 59, 0);
  width: 100vw;
  align-items: flex-end;
}

html,
body {
  background-color: #2c3e50;
}

.navbar {
  background-color: #ffffff;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
