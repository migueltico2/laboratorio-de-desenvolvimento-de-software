<template>
	<v-app class="app" theme="dark">
		<v-app-bar app v-if="isLoggedIn">
			<v-app-bar-title>Student Rewards</v-app-bar-title>
			<v-spacer></v-spacer>
			<v-btn to="/">Home</v-btn>
			<v-btn to="/profile">Profile</v-btn>
			<v-btn to="/advantages">Advantages</v-btn>
			<v-btn @click="logout">Logout</v-btn>
		</v-app-bar>

		<v-main class="main">
			<router-view />
		</v-main>
	</v-app>
</template>

<script setup>
import { useAuth } from './composables/useAuth';

const { logout, isLoggedIn, savedLocalStorage } = useAuth();

savedLocalStorage();
</script>

<style scoped>
.app {
	width: 100vw;
	height: 100vh;
}

.main {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	width: 100%;
	height: 100%;
	padding: 2rem;
	text-align: center;
	overflow-y: auto;
}
</style>
