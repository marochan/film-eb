<template>
  <div class="section">
    <div class="join card">
      <div class="card-content">
        <div class="container">
          <h1 class="title has-text-centered">Rekomendacja filmów</h1>
          <h4 class="title has-text-centered">
            Kliknij poniżej aby zalogować się używając konta Google
          </h4>
          <div class="buttons is-centered">
            <div id="gSignup"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { store } from "@/store";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
declare const google: any;

interface SignIn {
  clientId: string;
  credential: string;
  select_by: string;
}

@Options({
  components: {},
  props: {},
})
export default class Welcome extends Vue {
  store = store;
  async joinClicked(response: SignIn): Promise<void> {
    console.log(response);
    try {
      let base = this.store.state.apiUrl;
      const url = new URL("/join", base);

      const verified = await fetch(url.toString(), {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${response.credential}`,
        },
      });
      if (!verified.ok) {
        throw new Error(await verified.text());
      }

      const user = await verified.json();

      store.setToken(response.credential);
      store.setLoggedIn(user.name);
    } catch (e) {
      console.log(e);
    }
  }
  mounted(): void {
    google.accounts.id.initialize({
      client_id:
        "405815393608-333ae8if6up2dn84je64ph76d5c211d2.apps.googleusercontent.com",
      callback: this.joinClicked,
    });
    google.accounts.id.renderButton(
      document.getElementById("gSignup"),
      { theme: "outline", size: "large" } // customization attributes
    );
    google.accounts.id.prompt(); // also display the One Tap dialog
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
