<template>
	<div style="width: 100%;">
		<Registration v-if="!isLoggedIn" @user-registered="handleUserRegistered" />
		<UserDashboard v-else :userData="currentUser" @logout="handleLogout" />
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
};

const handleLogout = () => {
	isLoggedIn.value = false;
	currentUser.value = null;
};
</script>

<style scoped></style>
