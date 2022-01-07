<template>
  <div class="card">
    <div class="card-content">
      <div class="content columns">
        <div class="column">
          <h3>{{ record.title }}</h3>
          <h4>Reżyseria: {{ record.director }}</h4>
          <p>Rok: {{ record.year }}</p>
          <p>{{ record.description }}</p>
        </div>
        <div class="column is-one-fifth is-flex">
          <div class="buttons is-centered">
            <button class="button" @click="rateMovie">Oceń</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <br />
</template>

<script lang="ts">
import { Movie } from "@/store";
import { Options, Vue } from "vue-class-component";
import { Emit, Prop } from "vue-property-decorator";

@Options({
  components: {},
})
export default class MovieCard extends Vue {
  @Prop()
  record!: Movie;

  @Emit()
  rateMovie(): Movie {
    return this.record;
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
