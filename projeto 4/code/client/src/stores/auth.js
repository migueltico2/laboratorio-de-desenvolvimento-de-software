import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: JSON.parse(localStorage.getItem('userData')) || {},
        isLoggedIn: !!localStorage.getItem('userData')
    }),

    actions: {
        updateUser(userData) {
            this.user = userData;
            this.isLoggedIn = true;
            localStorage.setItem('userData', JSON.stringify(userData));
        },

        login(userData) {
            this.user = userData;
            this.isLoggedIn = true;
            console.log(userData);
            localStorage.setItem('userData', JSON.stringify(userData));
        },

        logout() {
            this.user = {};
            this.isLoggedIn = false;
            localStorage.removeItem('userData');
        },

        checkAuth() {
            const userData = localStorage.getItem('userData');
            if (userData) {
                this.user = JSON.parse(userData);
                this.isLoggedIn = true;
            }
        }
    }
}) 