// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import VueAuth from '@websanova/vue-auth'
import VueScrollTo from 'vue-scrollto'
import Buefy from 'buefy'
import '@mdi/font/css/materialdesignicons.css'
import 'bulma-modal-fx/dist/css/modal-fx.css'

Vue.config.productionTip = false

Vue.router = router

const apiUrl = '/api'
const authUrl = '/auth' // because of baseURL this is actually /api/auth

Vue.use(VueAxios, axios.create({
  baseURL: apiUrl
}))

let axiosDriver = require('@websanova/vue-auth/drivers/http/axios.1.x.js')

axiosDriver._interceptor = function (req, res) {
  var _this = this
  if (req) {
    Vue.axios.interceptors.request.use(function (request) {
      req.call(_this, request)
      return request
    }, function (error) {
      return Promise.reject(error)
    })
  }

  if (res) {
    Vue.axios.interceptors.response.use(function (response) {
      res.call(_this, response)
      return response
    }, function (error) {
      let originalRequest = error.config
      if (originalRequest.url.endsWith(authUrl + '/refresh') || originalRequest.url.endsWith(authUrl + '/login')) {
        return Promise.reject(error)
      }
      if (error.response.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true
        return Vue.axios.get(authUrl + '/refresh')
          .then(res => {
            originalRequest.headers['Authorization'] = 'Bearer ' + app.$auth.token().split(';')[0]
            originalRequest.url = originalRequest.url.replace(apiUrl, '')
            return Vue.axios(originalRequest)
          }).catch(err => {
            console.log(err)
            app.$auth.logout({
              makeRequest: false,
              redirect: '/login'
            })
            return
          })
      }
      return Promise.reject(error)
    })
  }
}

Vue.use(VueAuth, {
  http: axiosDriver,
  router: require('@websanova/vue-auth/drivers/router/vue-router.2.x.js'),
  auth: {
    request: (req, token) => {
      let tokens = token.split(';')
      axiosDriver._setHeaders.call(
        this, req, { Authorization: 'Bearer ' +
                     tokens[req.url.endsWith(authUrl + '/refresh') ? 1 : 0] })
    },
    response: (res) => {
      let existingToken = app.$auth.token() || ';'
      let tokens = existingToken.split(';')
      if (res.data.token) {
        tokens[0] = res.data.token
      }
      if (res.data.refreshToken) {
        tokens[1] = res.data.refreshToken
      }
      return tokens.join(';')
    }
  },
  loginData: {
    url: authUrl + '/login'
  },
  refreshData: {
    enabled: false
  },
  fetchData: {
    url: authUrl + '/whoami',
    method: 'GET',
    enabled: true
  },
  parseUserData: (data) => {
    return {
      username: data.username,
      roles: data.authorities['photo'].reduce((roles, auth) => {
        roles.push(auth.authority)
        return roles
      }, [])
    }
  }
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
const app = new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

export { app, router }
