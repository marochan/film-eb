import { reactive } from "@vue/reactivity";

export interface Movie {
  id: string;
  title: string;
  director: string;
  description: string;
  year: number;
}

export const store = {
  debug: true,

  state: reactive({
    loggedIn: "",
    token: "",
    recommended: Array<Movie>(),
    apiUrl: "http://localhost",
    search: Array<Movie>(),
  }),

  setToken(newValue: string): void {
    this.state.token = newValue;
  },

  setLoggedIn(newValue: string): void {
    if (this.debug) {
      console.log("setLoggedIn triggered with", newValue);
    }

    this.state.loggedIn = newValue;
  },

  setRecommended(newValue: []): void {
    this.state.recommended = newValue;
  },

  setSearchResults(newValue: []): void {
    this.state.search = newValue;
  },

  clear(): void {
    this.state.loggedIn = "";
    this.state.token = "";
    this.state.search = [];
    this.state.recommended = [];
  },
};
