import { createRouter, createWebHistory } from 'vue-router'
import Registration from '../components/Registration.vue'
import Advantage from '../components/Advantage.vue'

const routes = [
    {
        path: '/',
        name: 'Registration',
        component: Registration
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/advantages',
        name: 'Advantage',
        component: Advantage
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
