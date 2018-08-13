<template>
<div id="app">

  <section class="hero is-fullheight is-dark is-bold" v-if="['Home'].indexOf($route.name) > -1">
    <div class="hero-body">
      <div class="container has-text-centered">
        <img id="logo" src="@/assets/logo.png">
        <h1 class="title">
          Primary bold title
        </h1>
        <h2 class="subtitle">
          Primary bold subtitle
        </h2>
      </div>
    </div>
  </section>

  <nav id="photoNav" class="navbar is-fixed-bottom">

    <div class="navbar-brand">
      <router-link class="navbar-item" to="/">
        <img src="@/assets/logo.png" alt="andreagv.com" width="112" height="28">
      </router-link>
      <div class="navbar-burger burger" data-target="photoMenu">
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>
    
    <div id="photoMenu" class="navbar-menu">

      <!--
      <div class="navbar-start">
      </div>
      -->
      
      <div class="navbar-end">
        <router-link class="navbar-item" to="/blog">
          Blog
        </router-link>
        <router-link class="navbar-item" to="/portfolio">
          Portfolio
        </router-link>
        <router-link class="navbar-item" to="/contact">
          Contact
        </router-link>
        <div class="navbar-item has-dropdown is-hoverable">
          <a class="navbar-link">
            Account
          </a>
          <div class="navbar-dropdown is-boxed">
            <router-link class="navbar-item" to="/login">
              Login
            </router-link>
            <router-link class="navbar-item" to="/register">
              Register
            </router-link>
            <router-link class="navbar-item" to="/profile">
              Profile
            </router-link>
            <router-link class="navbar-item" to="/admin">
              Admin
            </router-link>
            <hr class="navbar-divider">
            <a class="navbar-item">
              Logout
            </a>
          </div>
        </div>
      </div>
    </div>

  </nav>

  <router-view/>

  <footer class="footer">
    <div class="content has-text-centered">
      <p>
        <strong>Andreagv.com</strong> by <a href="https://andreagv.com">Andrea Gallego-Valencia</a>. The source code is licensed
        <a href="http://opensource.org/licenses/mit-license.php">MIT</a>. The website content
        is licensed <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY NC SA 4.0</a>.
      </p>
    </div>
  </footer>

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
    mounted: function() {
        
        document.addEventListener('DOMContentLoaded', () => {
            
            // Get all "navbar-burger" elements
            const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0)
            
            // Check if there are any navbar burgers
            if ($navbarBurgers.length > 0) {
                
                // Add a click event on each of them
                $navbarBurgers.forEach( el => {
                    el.addEventListener('click', () => {
                        
                        // Get the target from the "data-target" attribute
                        const target = el.dataset.target;
                        const $target = document.getElementById(target)
                        
                        // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
                        el.classList.toggle('is-active')
                        $target.classList.toggle('is-active')
                    })
                })
            }
        })
        
        
        const html = document.documentElement
        const nav = document.getElementById("photoNav")
        const getDistance = () => nav.offsetTop        
        
        let fixedBottom = true
        let fixedTop = false
        let stickPoint = getDistance()
        
        window.onscroll = function(e) {
            
            let distance = getDistance() - window.pageYOffset
            let offset = window.pageYOffset
            
            if (offset >= nav.clientHeight) {
                html.classList.remove('has-navbar-fixed-bottom')
                nav.classList.remove('is-fixed-bottom')
            } else {
                html.classList.add('has-navbar-fixed-bottom')
                nav.classList.add('is-fixed-bottom')
            }
            
            if ((distance <= 0) && !fixedTop) {
                html.classList.add('has-navbar-fixed-top')
                nav.classList.add('is-fixed-top')
                nav.classList.add('is-transparent')
                fixedTop = true
                
            } else if (fixedTop && (offset <= stickPoint)) {
                html.classList.remove('has-navbar-fixed-top')
                nav.classList.remove('is-fixed-top')
                nav.classList.remove('is-transparent')
                fixedTop = false
            }
        }
    },
    methods: {

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

// Navbar
$navbar-height: 4.25rem;

// Footer
$footer-background-color: $dark;

// Import Bulma and Buefy styles
@import "~bulma";
@import "~buefy/src/scss/buefy";

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.footer p {
  color: $light;
}

.footer strong {
  color: $light;
}

</style>
