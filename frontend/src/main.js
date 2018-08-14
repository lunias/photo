// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import VueAuth from '@websanova/vue-auth'
import VueScrollTo from 'vue-scrollto'
import Buefy from 'buefy'
import '@mdi/font/css/materialdesignicons.css'
import 'bulma-modal-fx/dist/css/modal-fx.css'

Vue.config.productionTip = false

Vue.use(VueAxios, axios.create({
  baseURL: '/api'
}))

Vue.use(VueAuth, {
    auth: require('@websanova/vue-auth/drivers/auth/bearer.js'),
    http: require('@websanova/vue-auth/drivers/http/axios.1.x.js'),
    router: require('@websanova/vue-auth/drivers/router/vue-router.2.x.js')
});

Vue.use(Buefy)

Vue.filter('formatSize', function (size) {
    if (size > 1024 * 1024 * 1024 * 1024) {
        return (size / 1024 / 1024 / 1024 / 1024).toFixed(2) + ' TB'
    } else if (size > 1024 * 1024 * 1024) {
        return (size / 1024 / 1024 / 1024).toFixed(2) + ' GB'
    } else if (size > 1024 * 1024) {
        return (size / 1024 / 1024).toFixed(2) + ' MB'
    } else if (size > 1024) {
        return (size / 1024).toFixed(2) + ' KB'
    }
    return size.toString() + ' B'
})

Vue.use(VueScrollTo)

// You can also pass in the default options
Vue.use(VueScrollTo, {
    container: "body",
    duration: 500,
    easing: "ease",
    offset: 0,
    cancelable: true,
    onStart: false,
    onDone: false,
    onCancel: false,
    x: false,
    y: true
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
