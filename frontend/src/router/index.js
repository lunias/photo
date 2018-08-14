import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Login from '@/components/Login'
import Register from '@/components/Register'
import Profile from '@/components/Profile'
import Portfolio from '@/components/Portfolio'
import Blog from '@/components/Blog'
import Contact from '@/components/Contact'
import Admin from '@/components/Admin'

Vue.use(Router)

export default new Router({
    mode: 'history',
    linkExactActiveClass: 'is-active',
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
                guest: true
            }
        },
        {
            path: '/register',
            name: 'Register',
            component: Register,
            meta: {
                guest: true
            }
        },
        {
            path: '/profile',
            name: 'Profile',
            component: Profile,
            meta: {
                requiresAuth: true
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
            meta: {
                requiresAuth: true,
                is_admin : true
            }
        }
    ]
})
