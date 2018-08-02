<template>
  <div id="app">
    <img id="logo" src="./assets/logo.png">
    <button @click="callRestService()" v-if="false">
      CALL Spring Boot REST backend service</button>
    <h3 v-if="false">{{ response }}</h3>
    <router-view/>
  </div>
</template>

<script>  
  export default {
    name: 'App',
    data () {
      return {
        response: [],
        errors: []
      }
    },
    methods: {
      callRestService () {
        this.$http.get(`/photos/hello`)
          .then(response => {
            // JSON responses are automatically parsed.
            this.response = response.data
          })
          .catch(e => {
            this.errors.push(e)
          })
      }
    }
}
</script>

<style lang="scss">
// Import Bulma's core
@import "~bulma/sass/utilities/_all";

// Set your colors
$primary: #613a43;
$link: #e9eddc;
$info: #d1a827;
$success: #849974;
$warning: #e3bab3;
$danger: #d73a31;
$dark: #363636;
$text: #4a4a4a;
$primary-invert: findColorInvert($primary);
$twitter: #4099FF;
$twitter-invert: findColorInvert($twitter);

// Setup $colors to use as bulma classes (e.g. 'is-twitter')
$colors: (
    "white": ($white, $black),
    "black": ($black, $white),
    "light": ($light, $light-invert),
    "dark": ($dark, $dark-invert),
    "primary": ($primary, $primary-invert),
    "info": ($info, $info-invert),
    "success": ($success, $success-invert),
    "warning": ($warning, $warning-invert),
    "danger": ($danger, $danger-invert),
    "twitter": ($twitter, $twitter-invert)
);

// Links
$link: $primary;
$link-invert: $primary-invert;
$link-focus-border: $primary;

// Import Bulma and Buefy styles
@import "~bulma";
@import "~buefy/src/scss/buefy";

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
#logo {
    position: relative;
    z-index: 9000;
}
</style>
