<template>
	<div>
		<Registration v-if="!isLoggedIn" @user-registered="handleUserRegistered" />
		<UserDashboard v-else :userData="currentUser" />
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
</script>

<style scoped></style>
