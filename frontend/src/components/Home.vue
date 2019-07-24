<template>
  <div class="home">
    <section class="hero is-medium is-primary is-bold">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">
            Andrea GV
          </h1>
          <h2 class="subtitle">
            Photography
          </h2>
        </div>
      </div>
    </section>
    <!--<div style="postion: relative;">-->
    <parallax :fixed="true" :speed-factor="0.5">
      <img id="bg" src="@/assets/bg.jpg">
    </parallax>
    <!--</div>-->
    <!--<div style="postion: relative;">-->
    <parallax :speed-factor="0.9">
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
    </parallax>
    <!--</div>-->
  <!--<div style="postion: relative;">-->
    <parallax>
    <img id="bg2" src="@/assets/bg2.jpg">
    </parallax>
    <!--</div>-->
  </div>
</template>

<script>
import modalFX from '../../node_modules/bulma-modal-fx/dist/js/modal-fx.min.js'
import Parallax from 'vue-parallaxy'
export default {
    name: 'Home',
    components: { Parallax },
    data () {
        return {
            fixedTop: false
        }
    },
    mounted: function() {
        
        const html = document.documentElement
        const nav = document.getElementById('photoNav')
        const accountMenu = document.getElementById('accountMenu')
        
        if (html.classList.contains('has-navbar-fixed-top')) {
            
            html.classList.remove('has-navbar-fixed-top')
            html.classList.add('has-navbar-fixed-bottom')
            
            nav.classList.remove('is-fixed-top')
            nav.classList.add('is-fixed-bottom')
            
            accountMenu.classList.add('has-dropdown-up')
        }
        
        const getDistance = () => nav.offsetTop
        
        const stickPoint = getDistance() + nav.offsetHeight
        
        window.onscroll = function(e) {
            
            let distance = getDistance() - window.pageYOffset
            let offset = window.pageYOffset
            
            if (offset >= nav.offsetHeight) {
                html.classList.remove('has-navbar-fixed-bottom')
                nav.classList.remove('is-fixed-bottom')
            } else {
                html.classList.add('has-navbar-fixed-bottom')
                nav.classList.add('is-fixed-bottom')
            }
            
            if ((distance <= 0) && !this.fixedTop) {
                html.classList.add('has-navbar-fixed-top')
                nav.classList.add('is-fixed-top')
                accountMenu.classList.remove('has-dropdown-up')
                this.fixedTop = true
                
            } else if (this.fixedTop && (offset <= stickPoint)) {
                html.classList.remove('has-navbar-fixed-top')
                nav.classList.remove('is-fixed-top')
                accountMenu.classList.add('has-dropdown-up')
                this.fixedTop = false
            }
        }
    },
    destroyed: function() {

        window.onscroll = function(e) { }

        const html = document.documentElement
        const nav = document.getElementById('photoNav')
        const accountMenu = document.getElementById('accountMenu')
        
        html.classList.remove('has-navbar-fixed-bottom')
        html.classList.add('has-navbar-fixed-top')
        
        nav.classList.remove('is-fixed-bottom')
        nav.classList.add('is-fixed-top')
        
        accountMenu.classList.remove('has-dropdown-up')

        this.fixedTop = true
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
