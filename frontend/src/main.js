// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import Buefy from 'buefy'
import 'buefy/lib/buefy.css'
import 'bulma-modal-fx/dist/css/modal-fx.css'

Vue.config.productionTip = false

Vue.use(VueAxios, axios.create({
  baseURL: '/api'
}))

Vue.use(Buefy)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
