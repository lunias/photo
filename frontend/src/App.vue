<template>
  <div id="app">
    <img src="./assets/logo.png">
    <button @click="callRestService()">
      CALL Spring Boot REST backend service</button>
    <h3>{{ response }}</h3>
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

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
