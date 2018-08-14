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
        <div class="container scroll-down" v-scroll-to="'#routerView'">
          <div class="chevron"></div>
          <div class="chevron"></div>
          <div class="chevron"></div>
          <span class="scroll-down-text">scroll down</span>
        </div>
      </div>
    </div>
  </section>

  <nav id="photoNav" class="navbar is-fixed-top">

    <div class="navbar-brand" v-scroll-to="'#app'">
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
        <div id="accountMenu" class="navbar-item has-dropdown is-hoverable">
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

  <span v-scroll-to="'#app'">
    <a id="toTop" class="cd-top">Top</a>
  </span>

  <router-view id="routerView"/>

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
        
        const nav = document.getElementById('photoNav')
        const toTop = document.getElementById('toTop')
        
        const getStickPoint = () => nav.offsetTop + nav.offsetHeight
        
        window.addEventListener('scroll', function(e) {                        
            
            if (window.pageYOffset > getStickPoint()) {
                toTop.classList.add('cd-is-visible')
                //toTop.classList.add('cd-fade-out')
            } else {
                toTop.classList.remove('cd-is-visible')
                toTop.classList.remove('cd-fade-out')
            }
        });
    },
    methods: {

    },
    computed: {

    }
}
</script>

<style lang="scss">
// Import Google Font
@import url(https://fonts.googleapis.com/css?family=Sawarabi+Mincho);
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
$navbar-item-hover-color: $info;
$navbar-item-active-color: $info;
$navbar-dropdown-item-hover-color: $info;
$navbar-dropdown-item-active-color: $info;

// Footer
$footer-background-color: $dark;

// Import Bulma and Buefy styles
@import "~bulma";
@import "~buefy/src/scss/buefy";

#app {
  font-family: 'Sawarabi Mincho', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.footer p {
  color: $light;
}

.footer strong {
  color: $light;
}

.cd-top {
    display: inline-block;
    height: 30px;
    width: 30px;
    position: fixed;
    bottom: 100px;
    right: 10px;
    z-index: 2000;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
    /* image replacement properties */
    overflow: hidden;
    text-indent: 100%;
    white-space: nowrap;
    background: rgba(205, 205, 205, 0.8) url(./assets/arrows.svg) no-repeat center 50%;
    background-size: 40px 40px;
    visibility: hidden;
    opacity: 0;
    -webkit-transition: opacity .3s 0s, visibility 0s .3s;
    -moz-transition: opacity .3s 0s, visibility 0s .3s;
    transition: opacity .3s 0s, visibility 0s .3s;
}

.cd-top.cd-is-visible, .cd-top.cd-fade-out, .no-touch .cd-top:hover {
    -webkit-transition: opacity .3s 0s, visibility 0s 0s;
    -moz-transition: opacity .3s 0s, visibility 0s 0s;
    transition: opacity .3s 0s, visibility 0s 0s;
}

.cd-top.cd-is-visible {
    /* the button becomes visible */
    visibility: visible;
    opacity: 1;
}

.cd-top.cd-fade-out {
    /* if the user keeps scrolling down, the button is out of focus and becomes less visible */
    opacity: .5;
}

.no-touch .cd-top:hover {
    background-color: #F0563D;
    opacity: 1;
}

@media only screen and (min-width: 768px) {
    .cd-top {
	      right: 20px;
	      bottom: 20px;
    }
}

@media only screen and (min-width: 1024px) {
    .cd-top {
	      height: 60px;
	      width: 60px;
	      right: 30px;
	      bottom: 30px;
    }
}

@media (max-width: 775px) {
    .cd-top {
	      bottom: 10px;
    }
}

.scroll-down {
  position: relative;
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.chevron {
  position: absolute;
  width: 28px;
  height: 8px;
  opacity: 0;
  transform: scale3d(0.5, 0.5, 0.5);
  animation: move 3s ease-out infinite;
}

.chevron:first-child {
  animation: move 3s ease-out 1s infinite;
}

.chevron:nth-child(2) {
  animation: move 3s ease-out 2s infinite;
}

.chevron:before,
.chevron:after {
  content: ' ';
  position: absolute;
  top: 10px;
  height: 100%;
  width: 51%;
  background: #fff;
}

.chevron:before {
  left: 0;
  transform: skew(0deg, 30deg);
}

.chevron:after {
  right: 0;
  width: 50%;
  transform: skew(0deg, -30deg);
}

@keyframes move {
  25% {
    opacity: 1;

  }
  33% {
    opacity: 1;
    transform: translateY(30px);
  }
  67% {
    opacity: 1;
    transform: translateY(40px);
  }
  100% {
    opacity: 0;
    transform: translateY(55px) scale3d(0.5, 0.5, 0.5);
  }
}

.scroll-down-text {
  display: block;
  margin-top: 75px;
  margin-left: -30px;
  font-family: "Helvetica Neue", "Helvetica", Arial, sans-serif;
  font-size: 12px;
  color: #fff;
  text-transform: uppercase;
  white-space: nowrap;
  opacity: .25;
  animation: pulse 2s linear alternate infinite;
}

@keyframes pulse {
  to {
    opacity: 1;
  }
}
</style>
