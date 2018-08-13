<template>
<div class="home">
  <section class="hero is-medium is-primary is-bold">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">
          Primary bold title
        </h1>
        <h2 class="subtitle">
          Primary bold subtitle
        </h2>
      </div>
    </div>
  </section>
  <section class="hero is-medium is-info is-bold">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">
          Primary bold title
        </h1>
        <h2 class="subtitle">
          Primary bold subtitle
        </h2>
      </div>
    </div>
  </section>
  <section class="hero is-medium is-light is-bold">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">
          Primary bold title
        </h1>
        <h2 class="subtitle">
          Primary bold subtitle
        </h2>
      </div>
    </div>
  </section>
</div>
</template>

<script>
import modalFX from '../../node_modules/bulma-modal-fx/dist/js/modal-fx.min.js'
import Upload from './Upload.vue'
export default {
    name: 'Home',
    components: { Upload },
    data () {
        return {
            photos: [],
            errors: []
        }
    },
    mounted: function() {
        this.getPhotos()
    },
    methods: {
        getPhotos () {
            this.$http.get(`/photos`)
                .then(response => {
                    this.photos = response.data.content
                })
                .catch(e => {
                    this.errors.push(e)
                })
        }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped> 

</style>
