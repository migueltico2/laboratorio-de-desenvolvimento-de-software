<template>
	<div style="width: 100%">
		<Registration
			v-if="!isLoggedIn"
			@user-registered="handleUserRegistered"
		/>
		<UserDashboard
			v-else
			:userData="currentUser"
			@logout="handleLogout"
			@update-user="handleUpdateUser"
		/>
	</div>
</template>

<script setup>
import { ref } from 'vue';
import Registration from './components/Registration.vue';
import UserDashboard from './components/UserDashboard.vue';

const isLoggedIn = ref(false);
const currentUser = ref(null);

const handleUserRegistered = (userData) => {
	currentUser.value = userData;
	isLoggedIn.value = true;
	localStorage.setItem('userData', JSON.stringify(userData));
};

const handleLogout = () => {
	isLoggedIn.value = false;
	currentUser.value = null;
	localStorage.removeItem('userData');
};

const savedLocalStorage = () => {
	const userData = localStorage.getItem('userData');
	if (userData) {
		currentUser.value = JSON.parse(userData);
		isLoggedIn.value = true;
	}
};
savedLocalStorage();
</script>

<style scoped></style>
