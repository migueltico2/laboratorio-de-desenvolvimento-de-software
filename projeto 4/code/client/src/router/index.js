import { createRouter, createWebHistory } from 'vue-router'
import Registration from '../components/Registration.vue'
import Advantage from '../components/Advantage.vue'
import UserDashboard from '../components/UserDashboard.vue'
import Account from '../components/Account.vue'
import { useAuth } from '../composables/useAuth';

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
    },
    {
        path: '/account',
        name: 'Account',
        component: Account
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const { isLoggedIn } = useAuth();

	if (to.name !== 'Registration' && !isLoggedIn.value) {
		next({ name: 'Registration' });
    } else if (to.name === 'Registration' && isLoggedIn.value) {
        next({ name: 'Profile' });
    } else {
        next();
    }
});

export default router
