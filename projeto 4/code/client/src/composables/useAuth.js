import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

export const useAuth = () => {
    const router = useRouter();
    let authStore = null;
    
    try {
        authStore = useAuthStore();
    } catch (error) {
        console.warn('Auth store not ready yet');
        return {
            user: ref(null),
            isLoggedIn: ref(false),
            logout: () => {},
            savedLocalStorage: () => {},
            login: () => {},
            updateUser: () => {}
        };
    }

    const updateUser = (userData) => {
        authStore.updateUser(userData);
    };

    const login = (userData) => {
        authStore.login(userData);
        router.push({ name: 'Profile' });
    };

    const logout = () => {
        authStore.logout();
        router.push('/');
    };

    const savedLocalStorage = () => {
        authStore.checkAuth();
    };

    return {
        user: authStore.user,
        isLoggedIn: computed(() => authStore.isLoggedIn),
        logout,
        savedLocalStorage,
        login,
        updateUser
    };
};