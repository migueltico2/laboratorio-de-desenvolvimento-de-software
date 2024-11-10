import { createRouter, createWebHistory } from 'vue-router'
import Registration from '../components/Registration.vue'
import Advantage from '../components/Advantage.vue'
import UserDashboard from '../components/UserDashboard.vue'
import { useAuth } from '../composables/useAuth';

const { isLoggedIn } = useAuth();

const routes = [
    {
        path: '/',
        name: 'Registration',
        component: Registration
    },
    {
        path: '/profile',
        name: 'Profile',
        component: UserDashboard
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

router.beforeEach((to, from, next) => {
	if (to.name !== 'Registration' && !isLoggedIn) {
		next({ name: 'Registration' });
    } else if (to.name === 'Registration' && isLoggedIn) {
        next({ name: 'Profile' });
    } else {
        next();
    }
});

export default router
