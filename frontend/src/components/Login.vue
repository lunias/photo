<template>
<div class="login">
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
            <form v-on:submit.prevent>
              <b-field label="Email">
                <b-input ref="email"
                         type="email"
                         size="is-medium"
                         icon="email"
                         minlength="5"
                         maxlength="256"
                         v-model="username"
                         @keyup.enter="login(username, password)"
                         placeholder="Your email address" autofocus></b-input>
              </b-field>
              <b-field label="Password">
                <b-input type="password"
                         size="is-medium"
                         icon="key"
                         minlength="8"
                         maxlength="128"
                         v-model="password"
                         @keyup.enter="login(username, password)"
                         placeholder="Password" password-reveal>
                </b-input>
              </b-field>
              <div class="field has-text-centered">
                <b-checkbox v-model="rememberMe">
                  Remember me
                </b-checkbox>
              </div>
              <button class="button is-block is-info is-large is-fullwidth" @click="login(username, password)">Login</button>
            </form>
          </div>
          <p class="has-text-grey has-text-centered">
            <router-link to="/register">Register</router-link> &nbsp;·&nbsp;
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
    name: 'Login',
    data () {
        return {
            rememberMe: false,
            username: '',
            password: '',
            error: null,
            animated: false
        }
    },
    mounted: function() {
        this.$refs.email.focus()
    },
    methods: {
      login (username, password) {
      console.log("got username " + username + " and password " + password)
        this.$auth.login({
          data: {
            username: username,
            password: password,
            applications: ['photo']
          },
          redirect: '/',
          success: () => {
          },
          error: (error) => {
            this.animated = true
            setTimeout(() => {
              this.animated = false
              this.error = error
            }, 500)
          }
        })
      },
      clearError () {
        this.error = null
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped> 
#login {
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
</style>
