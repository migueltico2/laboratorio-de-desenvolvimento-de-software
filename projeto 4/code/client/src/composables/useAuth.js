import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

export const useAuth = () => {
    const router = useRouter();
    const authStore = useAuthStore();

    const updateUser = (userData) => {
        authStore.updateUser(userData);
    };

    const login = (userData) => {
        authStore.login(userData);
        router.push('/profile');
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
        isLoggedIn: authStore.isLoggedIn,
        logout,
        savedLocalStorage,
        login,
        updateUser
    };
};