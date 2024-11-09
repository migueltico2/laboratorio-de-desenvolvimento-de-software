<template>
	<div>
		{{ advantages }}
		<div v-for="advantage in advantages" :key="advantage.id">
			<div class="advantage-container">
				<!-- <div class="image-container">
					<img :src="`data:image/jpeg;base64,${advantage.image}`" alt="Advantage image" />
				</div> -->
				<div class="advantage-info">
					<h3>{{ advantage.name }}</h3>
					<p>{{ advantage.description }}</p>
					<p class="price">R$ {{ advantage.price }}</p>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useFetchs } from '../composables/useFetchs';

const { listAdvantages } = useFetchs();

const advantages = ref([]);

onMounted(async () => {
	const advantagesData = await listAdvantages();
	advantages.value = advantagesData;
	console.log(advantagesData);
});
</script>

<style scoped>
.advantage-container {
	display: flex;
	flex-direction: row;
}
</style>
