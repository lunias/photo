import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Login from '@/components/Login'
import Register from '@/components/Register'
import Forgot from '@/components/Forgot'
import Profile from '@/components/Profile'
import Portfolio from '@/components/Portfolio'
import Blog from '@/components/Blog'
import Contact from '@/components/Contact'
import Admin from '@/components/Admin'

Vue.use(Router)

export default new Router({
    mode: 'history',
    linkExactActiveClass: 'is-active',
    scrollBehavior (to, from, savedPosition) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                if (savedPosition) {
                    return savedPosition
                }
                return resolve({ x: 0, y: 0 })
            }, 0)
        })
    },
    routes: [
        {
            path: '/',
            name: 'Home',
            component: Home
        },
        {
            path: '/login',
            name: 'Login',
            component: Login,

            meta: {
                auth: false
            }
        },
        {
            path: '/register',
            name: 'Register',
            component: Register,
            meta: {
                auth: false
            }
        },
        {
            path: '/forgot',
            name: 'Forgot',
            component: Forgot,
            meta: {
                auth: false
            }
        },
        {
            path: '/profile',
            name: 'Profile',
            component: Profile,
            meta: {
              auth: false
            }
        },
        {
            path: '/portfolio',
            name: 'Portfolio',
            component: Portfolio
        },
        {
            path: '/blog',
            name: 'Blog',
            component: Blog
        },
        {
            path: '/contact',
            name: 'Contact',
            component: Contact
        },
        {
            path: '/admin',
            name: 'Admin',
            component: Admin,
            /*
            meta: {
                auth: false  //'admin'
            }
            */
        }
    ]
})
