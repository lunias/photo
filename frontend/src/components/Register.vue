<template>
<div class="register">
  <section class="hero is-dark is-fullheight">
    <div class="hero-body">
      <div class="container">
        <div class="column is-6 is-offset-3">
          <div class="box">
            <div class="has-text-centered">
              <figure class="avatar">
                <img src="@/assets/user.jpg">
              </figure>
            </div>
            <form id="registerForm" v-on:submit.prevent>
              <b-field label="Name" custom-class="pad-right" grouped>
                  <b-input size="is-medium"
                           icon="account-card-details"
                           minlength="2"
                           maxlength="32"
                           placeholder="First" expanded autofocus></b-input>
                  <b-input size="is-medium"
                           icon="account-card-details"
                           minlength="2"
                           maxlength="64"
                           placeholder="Last" expanded></b-input>
              </b-field>
              <b-field label="Email">
                <b-input ref="email"
                         type="email"
                         size="is-medium"
                         icon="email"
                         minlength="5"
                         maxlength="256"
                         placeholder="Your email address"></b-input>
              </b-field>
              <b-field label="Username">
                <b-input type="text"
                         size="is-medium"
                         icon="account"
                         minlength="5"
                         maxlength="32"
                         placeholder="Your desired username">
                </b-input>
              </b-field>
              <b-field label="Password">
                <b-input type="password"
                         size="is-medium"
                         icon="key"
                         minlength="8"
                         maxlength="128"
                         placeholder="Password" password-reveal>
                </b-input>
              </b-field>
              <b-field label="Repeat password">
                <b-input type="password"
                         size="is-medium"
                         icon="key"
                         minlength="8"
                         maxlength="128"
                         placeholder="Repeat password" password-reveal>
                </b-input>
              </b-field>
              <div class="field has-text-centered">
                <b-checkbox v-model="rememberMe">
                  Remember me
                </b-checkbox>
              </div>
              <button class="button is-block is-primary is-large is-fullwidth" @click="register">Register</button>
            </form>
          </div>
          <p class="has-text-grey has-text-centered">
            <router-link to="/login">Login</router-link> &nbsp;·&nbsp;
            <router-link to="/forgot">Forgot Password?</router-link> &nbsp;·&nbsp;
            <router-link to="/help">Need Help?</router-link>
          </p>
        </div>
      </div>
    </div>
  </section>
</div>
</template>

<script>
export default {
    name: 'Register',
    data () {
        return {
            rememberMe: false
        }
    },
    mounted: function() {
        this.$refs.email.focus()
    },
    methods: {
        register() {
            console.log("registering")
        }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss"> 
#register {
  font-size: 12px;
}
.avatar {
  margin-top: -70px;
  padding-bottom: 20px;
}
.avatar img {
  padding: 5px;
  background: #363636;
  border-radius: 50%;
  -webkit-box-shadow: 0 2px 3px rgba(10,10,10,.1), 0 0 0 1px rgba(10,10,10,.1);
  box-shadow: 0 2px 3px rgba(10,10,10,.1), 0 0 0 1px rgba(10,10,10,.1);
}
.pad-right {
  padding-right: 20px;
}
</style>
