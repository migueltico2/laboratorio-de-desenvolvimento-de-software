<template>
	<v-card class="mx-auto" min-width="80%" max-width="800">
		<v-card-title class="d-flex justify-space-between align-center">
			<div>
				<h2>Perfil do Usuário</h2>
				<p class="text-subtitle-1">Visualize e edite suas informações</p>
			</div>
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

							<v-text-field
								v-if="userType === 'enterprise'"
								v-model="userData.CNPJ"
								label="CNPJ"
								:readonly="!isEditing"
								required
							></v-text-field>

							<v-text-field
								v-else
								v-model="userData.CPF"
								:maxlength="11"
								label="CPF"
								:readonly="!isEditing"
								required
							></v-text-field>
						</v-col>

						<!-- Coluna Direita -->
						<v-col cols="12" md="6">
							<template v-if="userType === 'student'">
								<v-text-field
									v-model="userData.RG"
									length="8"
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
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { useFetchs } from '../composables/useFetchs';
import { useAuth } from '../composables/useAuth';
import { toast } from 'vue3-toastify';

const emit = defineEmits(['logout', 'updateUser']);

const { user, logout, updateUser: updateUserAuth } = useAuth();
const userType = user.type === 'institution' ? 'enterprise' : user.type;

const isEditing = ref(false);
const dialog = ref(false);
const { deleteUser, updateUser } = useFetchs();

const userData = reactive({
	user_id: user.id,
	id: user.students?.id || user.professors?.id || user.enterprises?.id || user.id,
	institution_id: user.students?.institution?.id || '',
	type: user.type,
	name: user.name,
	email: user.email,
	CNPJ: user.CNPJ || '',
	institutionType: user.institutionType || '',
	CPF: user.CPF || '',
	RG: user.RG || '',
	address: user.address || '',
	course: user.course || '',
});

watch(
	() => user,
	(newValue) => {
		Object.assign(userData, newValue);
	},
	{ deep: true }
);

const handleSave = async () => {
	isEditing.value = false;
	updateUserAuth(userData);
	await updateUser(userData, userType);
	toast.success('Suas informações foram atualizadas com sucesso!');
};

const handleDelete = async () => {
	dialog.value = false;
	if (user.enterprises) {
		await deleteUser(user.enterprises.id, userData.user_id, 'enterprise');
	} else if (user.students) {
		await deleteUser(user.students.id, userData.user_id, 'student');
	}
	toast.error('Sua conta foi excluída permanentemente.');
	logout();
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
