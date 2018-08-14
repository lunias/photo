<template>
<div class="admin">
  <section class="hero is-medium is-primary is-bold">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">
          Admin
        </h1>
        <h2 class="subtitle">
          Primary bold subtitle
        </h2>
      </div>
    </div>
  </section>
  <upload></upload>
  <section class="hero is-medium is-primary is-bold">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">
          Admin
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
import Upload from './Upload.vue'
export default {
    name: 'Admin',
    components: { Upload },
    data () {
        return {

        }
    },
    mounted: function() {

    },
    methods: {

    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped> 

</style>
