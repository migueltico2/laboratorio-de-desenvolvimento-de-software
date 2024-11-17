<template>
	<v-app class="app" theme="dark">
		<v-app-bar app v-if="isLoggedIn">
			<v-app-bar-title>Student Rewards</v-app-bar-title>
			<v-spacer></v-spacer>
			<v-btn to="/profile">Perfil</v-btn>
			<v-btn to="/advantages" v-if="userType != 'professor'">Vantagens</v-btn>
			<v-btn to="/students" v-else>Alunos</v-btn>
			<v-btn to="/account" v-if="userType != 'enterprise'">Conta</v-btn>
			<v-btn @click="logout">Sair</v-btn>
		</v-app-bar>

		<v-main class="main">
			<router-view />
		</v-main>
	</v-app>
</template>

<script setup>
import { computed } from 'vue';
import { useAuth } from './composables/useAuth';

const { logout, isLoggedIn, savedLocalStorage } = useAuth();

savedLocalStorage();
const user = computed(() => {
	return useAuth().user;
});
const userType = computed(() => {
	return user.value.type === 'institution' ? 'enterprise' : user.value.type;
});
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
	padding: 4rem 2rem 2rem;
	text-align: center;
	overflow-y: auto;
}
</style>
