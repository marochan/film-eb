<template>
  <div class="section content">
    <div class="columns movies">
      <div class="column">
        <div class="search card">
          <header class="card-header">
            <p class="card-header-title">Wyszukaj film</p>
          </header>
          <div class="card-content">
            <div class="field is-grouped">
              <p class="control is-expanded">
                <input
                  v-model="phrase"
                  class="input"
                  type="text"
                  placeholder="Wpisz tytuł"
                />
              </p>
              <p class="control">
                <button @click="search" class="button is-primary">
                  Szukaj
                </button>
              </p>
            </div>
            <div v-for="result in store.state.search" :key="result.title">
              <MovieCard :record="result" @rate-movie="showRateModal" />
            </div>
          </div>
        </div>
      </div>
      <div class="column">
        <div class="recommend card">
          <div class="card-header">
            <p class="card-header-title">Rekomendacje</p>
          </div>
          <div class="card-content">
            <div v-for="result in store.state.recommended" :key="result.title">
              <MovieCard :record="result" @rate-movie="showRateModal" />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="modal" :class="{ 'is-active': rateOpen }">
      <div class="modal-background"></div>
      <div class="modal-card">
        <header class="modal-card-head">
          <h2 class="modal-card-title">Oceń film</h2>
          <button
            class="delete"
            aria-label="close"
            @click="closeRateModal"
          ></button>
        </header>
        <section v-if="rated" class="modal-card-body">
          <h3>Tytuł: {{ rated.title }}</h3>
          <h4>Reżyseria: {{ rated.director }}</h4>
          <p>Opis: {{ rated.description }}</p>

          <h4>Ocena</h4>
          <div class="buttons has-addons is-centered">
            <button class="button">1</button>
            <button class="button">2</button>
            <button class="button">3</button>
            <button class="button">4</button>
            <button class="button">5</button>
          </div>
        </section>
        <footer class="modal-card-foot">
          <button class="button is-success">Zapisz</button>
          <button class="button" @click="closeRateModal">Anuluj</button>
        </footer>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { store, Movie } from "@/store";
import MovieCard from "./MovieCard.vue";

@Options({
  components: {
    MovieCard,
  },
  props: {},
})
export default class MainPanel extends Vue {
  store = store;
  phrase = "";
  rateOpen = false;
  rated: Movie | null = null;

  async mounted(): Promise<void> {
    try {
      let base = this.store.state.apiUrl;
      let token = this.store.state.token;
      const url = new URL("/recommended", base);
      let recommended = await fetch(url.toString(), {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!recommended.ok) {
        throw new Error(await recommended.text());
      }

      this.store.setRecommended(await recommended.json());
    } catch (e) {
      console.log(e);
    }
  }

  async search(): Promise<void> {
    try {
      let base = this.store.state.apiUrl;
      const url = new URL("/search", base);
      url.searchParams.append("searchPhrase", this.phrase);

      let results = await fetch(url.toString());
      this.store.setSearchResults(await results.json());
    } catch (e) {
      console.log(e);
    }
  }

  showRateModal(rated: Movie): void {
    this.rated = rated;
    this.rateOpen = true;
  }

  closeRateModal(): void {
    this.rated = null;
    this.rateOpen = false;
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
