import { ref } from 'vue';
import { useRouter } from 'vue-router';

export const useAuth = () => {
	const router = useRouter();
	const user = ref(JSON.parse(localStorage.getItem('userData')));
    const isLoggedIn = ref(false);

    const updateUser = (userData) => {
        user.value = userData;
        localStorage.setItem('userData', JSON.stringify(userData));
    };

    const login = (userData) => {
        isLoggedIn.value = true;
        localStorage.setItem('userData', JSON.stringify(userData));
        router.push('/profile');
    };

    const logout = () => {
        localStorage.removeItem('userData');
        isLoggedIn.value = false;
        router.push('/');
    };

    const savedLocalStorage = () => {
        const userData = localStorage.getItem('userData');
        if (userData) {
            isLoggedIn.value = true;
        }
    };

	return { user, logout, isLoggedIn, savedLocalStorage, login, updateUser };
};