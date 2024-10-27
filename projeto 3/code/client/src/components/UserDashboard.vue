<template>
	<v-card class="mx-auto" max-width="800">
		<v-card-title class="d-flex justify-space-between align-center">
			<div>
				<h2>Perfil do Usuário</h2>
				<p class="text-subtitle-1">Visualize e edite suas informações</p>
			</div>
			<v-btn variant="outlined" @click="handleLogout" prepend-icon="mdi-logout"> Logout </v-btn>
		</v-card-title>

		<v-form @submit.prevent="handleSave">
			<v-card-text>
				<v-container>
					<v-row>
						<!-- Coluna Esquerda -->
						<v-col cols="12" md="6">
							<v-text-field
								v-model="userData.name"
								label="Nome"
								:readonly="!isEditing"
								required
							></v-text-field>

							<v-text-field
								v-model="userData.email"
								label="Email"
								type="email"
								:readonly="!isEditing"
								required
							></v-text-field>

							<template v-if="userType === 'institution'">
								<v-text-field
									v-model="userData.CNPJ"
									label="CNPJ"
									:readonly="!isEditing"
									required
								></v-text-field>
							</template>

							<template v-else>
								<v-text-field
									v-model="userData.CPF"
									label="CPF"
									:readonly="!isEditing"
									required
								></v-text-field>
							</template>
						</v-col>

						<!-- Coluna Direita -->
						<v-col cols="12" md="6">
							<template v-if="userType === 'institution'">
								<v-select
									v-model="userData.institutionType"
									:items="institutionTypes"
									label="Tipo de Instituição"
									:readonly="!isEditing"
									required
								></v-select>
							</template>

							<template v-else>
								<v-text-field
									v-model="userData.RG"
									label="RG"
									:readonly="!isEditing"
									required
								></v-text-field>

								<v-textarea
									v-model="userData.address"
									label="Endereço"
									:readonly="!isEditing"
									required
								></v-textarea>

								<v-text-field
									v-model="userData.course"
									label="Curso"
									:readonly="!isEditing"
									required
								></v-text-field>
							</template>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>

			<v-card-actions class="justify-space-between pa-4">
				<div>
					<v-btn v-if="!isEditing" color="primary" class="mr-4" @click="isEditing = true">
						Editar Perfil
					</v-btn>
					<v-btn v-else color="primary" class="mr-4" type="submit"> Salvar Alterações </v-btn>

					<v-dialog v-model="dialog" width="500">
						<template v-slot:activator="{ props }">
							<v-btn color="error" v-bind="props"> Excluir Conta </v-btn>
						</template>

						<v-card>
							<v-card-title>Você tem certeza?</v-card-title>
							<v-card-text>
								Esta ação não pode ser desfeita. Isso excluirá permanentemente sua conta e removerá seus
								dados de nossos servidores.
							</v-card-text>
							<v-card-actions>
								<v-spacer></v-spacer>
								<v-btn color="grey" variant="text" @click="dialog = false"> Cancelar </v-btn>
								<v-btn color="error" @click="handleDelete"> Sim, excluir conta </v-btn>
							</v-card-actions>
						</v-card>
					</v-dialog>
				</div>
			</v-card-actions>
		</v-form>
	</v-card>
	{{ props.userData }}
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { useToast } from 'vue-toastification';
import { useFetchs } from '../composables/useFetchs';

const props = defineProps({
	userData: {
		type: Object,
		required: true,
	},
});

const emit = defineEmits(['logout']);

const userType = props.userData.students ? 'student' : 'institution';

const toast = useToast();
const isEditing = ref(false);
const dialog = ref(false);
const { deleteUser } = useFetchs();
const institutionTypes = [
	{ title: 'Parceiro', value: 'partner' },
	{ title: 'Instituição', value: 'institution' },
];

const userData = reactive({
	type: props.userData.type,
	name: props.userData.name,
	email: props.userData.email,
	CNPJ: props.userData.enterprises?.CNPJ || '',
	institutionType: props.userData.enterprises?.institutionType || '',
	CPF: props.userData.students?.CPF || '',
	RG: props.userData.students?.RG || '',
	address: props.userData.students?.address || '',
	course: props.userData.students?.course || '',
});

watch(
	() => props.userData,
	(newValue) => {
		Object.assign(userData, newValue);
	},
	{ deep: true }
);

const handleSave = async () => {
	isEditing.value = false;
	await updateUser(userData);
	toast.success('Suas informações foram atualizadas com sucesso!');
};

const handleDelete = async () => {
	dialog.value = false;
	if (props.userData.enterprises) {
		console.log('userData.enterprises', props.userData.enterprises.id);
		await deleteUser(props.userData.enterprises.id, 'enterprise');
	} else if (props.userData.students) {
		console.log('userData.students', props.userData.students.id);
		await deleteUser(props.userData.students.id, 'student');
	}
	toast.error('Sua conta foi excluída permanentemente.');
	emit('logout');
};

const handleLogout = () => {
	toast.success('Você foi desconectado com sucesso.');
	emit('logout');
};
</script>

<style scoped>
.v-card {
	margin-top: 2rem;
}

.v-btn {
	text-transform: none;
}
</style>
