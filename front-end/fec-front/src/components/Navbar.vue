<template>
  <nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <!-- <a class="navbar-item" href="/">
        <div class="field has-addons">
          <p class="control">
            <span class="button is-warning is-medium is-hovered">
              <span class="icon">
                <font-awesome-icon icon="film" class="film" />
              </span>

              <font-awesome-icon icon="horse-head" class="head" />
            </span>
          </p>
          <p class="control">
            <span class="button is-dark is-medium is-hovered">
              <font-awesome-icon icon="plus" class="and" />
              <span>&nbsp;&nbsp;</span>
              <font-awesome-icon icon="snowflake" class="chill" />
            </span>
          </p>
        </div>
      </a> -->
      <a class="navbar-item" href="/">
        <span class="title app-name">Filmłebendczil</span>
      </a>
    </div>

    <div class="navbar-menu">
      <div class="navbar-start"></div>

      <div class="navbar-end">
        <div class="navbar-item">
          <div v-if="store.state.loggedIn == ''" class="buttons">
            <!-- <button @click="logInClicked" class="button is-primary">
              Zaloguj
            </button> -->
          </div>
          <div v-else class="buttons">
            <div class="tag is-large mr-1 mb-2">
              {{ store.state.loggedIn }}
            </div>
            <button @click="logOutClicked" class="button is-primary">
              Wyloguj
            </button>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { store } from "../store";

@Options({
  components: {
    FontAwesomeIcon,
  },
  props: {},
})
export default class Navbar extends Vue {
  store = store;

  logInClicked(): void {
    store.setLoggedIn("Karol K");
  }

  logOutClicked(): void {
    store.clear();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.app-name {
  color: #2c3e50;
}
/* .pl,
.en {
  display: inline;
  margin-top: auto;
  margin-bottom: auto;
} */

/* .en {
  background-color: black;
  padding-right: 10px;
}
.pl {
  background-color: #ee6b00;
  padding-left: 10px;
}

.film,
.head {
  color: black;
}

.and,
.chill {
  color: white;
} */
</style>
