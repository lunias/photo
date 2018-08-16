<template>
<div class="forgot">
  <section class="hero is-dark is-fullheight">
    <div class="hero-body">
      <div class="container">
        <div class="column is-6 is-offset-3">
          <div class="box">
            <form v-on:submit.prevent>
              <b-field label="Email">
                <b-input ref="email"
                         type="email"
                         size="is-medium"
                         icon="email"
                         minlength="5"
                         maxlength="256"
                         placeholder="Your email address" autofocus></b-input>
              </b-field>
              <button class="button is-block is-info is-large is-fullwidth" @click="submit">Reset Password</button>
            </form>
          </div>
          <p class="has-text-grey has-text-centered">
            <router-link to="/login">Login</router-link> &nbsp;·&nbsp;
            <router-link to="/register">Register</router-link> &nbsp;·&nbsp;
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
            
        }
    },
    mounted: function() {
        this.$refs.email.focus()
    },
    methods: {
        submit() {
            console.log("requesting password reset")
        }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped> 
#forgot {
  font-size: 12px;
}
</style>
