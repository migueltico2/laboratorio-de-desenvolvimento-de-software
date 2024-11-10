<template>
	{{ user.enterprises.id }}
	<v-card class="mx-auto" min-width="80%" max-width="800" style="overflow: auto !important">
		<v-btn v-if="permissions === 'enterprise'" color="primary" class="ma-4" @click="openDialog">
			Criar Vantagem
		</v-btn>

		<v-table>
			<thead>
				<tr>
					<th>Nome</th>
					<th>Descrição</th>
					<th>Preço</th>
					<th v-if="permissions === 'enterprise'">Ações</th>
					<th v-else>Detalhes</th>
				</tr>
			</thead>
			<tbody>
				<template v-if="loading">
					<tr>
						<td colspan="4" class="text-center">
							<v-progress-circular indeterminate></v-progress-circular>
						</td>
					</tr>
				</template>
				<template v-else-if="advantages.length === 0">
					<tr>
						<td colspan="4" class="text-center pa-5">
							<v-icon icon="mdi-alert" size="large" color="warning" class="mb-2" />
							<div>Nenhuma vantagem encontrada</div>
						</td>
					</tr>
				</template>
				<template v-else>
					<tr v-for="advantage in paginatedAdvantages" :key="advantage.id">
						<td>{{ advantage.name }}</td>
						<td>{{ advantage.description }}</td>
						<td>R$ {{ advantage.coins }}</td>
						<td v-if="permissions === 'enterprise'">
							<v-btn size="small" variant="text" icon="mdi-pencil" @click="openDialog(advantage)" />
							<v-btn
								size="small"
								color="error"
								variant="text"
								icon="mdi-delete"
								@click="openDeleteDialog(advantage)"
							/>
						</td>
						<td v-else>
							<v-btn
								size="small"
								variant="text"
								icon="mdi-information"
								@click="openDetailsDialog(advantage)"
							/>
						</td>
					</tr>
				</template>
			</tbody>
		</v-table>

		<v-pagination v-model="page" :length="pageCount" :total-visible="7" class="mt-4"></v-pagination>

		<!-- Create/Edit Dialog -->
		<v-dialog v-model="dialog" max-width="500px">
			<v-card>
				<v-card-title>
					<span>{{ formTitle }}</span>
				</v-card-title>

				<v-card-text>
					<v-container>
						<v-text-field v-model="editedItem.name" label="Nome"></v-text-field>
						<v-textarea v-model="editedItem.description" label="Descrição"></v-textarea>
						<v-text-field v-model="editedItem.coins" label="Preço" prefix="R$" type="number"></v-text-field>
						<v-file-input
							v-model="editedItem.image"
							prepend-icon="mdi-camera"
							label="Imagem"
						></v-file-input>
						<v-col cols="12" v-if="editedItem.image">
							<v-img :src="editedItem.image" max-height="200"></v-img>
						</v-col>
					</v-container>
				</v-card-text>

				<v-card-actions>
					<v-spacer></v-spacer>
					<v-btn color="error" variant="text" @click="closeDialog">Cancelar</v-btn>
					<v-btn color="primary" variant="text" @click="save" :loading="formLoading">Salvar</v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>

		<!-- Delete Dialog -->
		<v-dialog v-model="deleteDialog" max-width="500px">
			<v-card>
				<v-card-title>Você tem certeza que deseja deletar este item?</v-card-title>
				<v-card-actions>
					<v-spacer></v-spacer>
					<v-btn color="error" variant="text" @click="closeDeleteDialog">Cancelar</v-btn>
					<v-btn color="primary" variant="text" @click="deleteItemConfirm">OK</v-btn>
					<v-spacer></v-spacer>
				</v-card-actions>
			</v-card>
		</v-dialog>

		<!-- Details Dialog -->
		<v-dialog v-model="detailsDialog" max-width="500px">
			<v-card>
				<v-card-title>
					<span>Detalhes da Vantagem</span>
				</v-card-title>

				<v-card-text>
					<v-container>
						<v-row>
							<v-col cols="12"> <strong>Nome:</strong> {{ selectedItem.name }} </v-col>
							<v-col cols="12"> <strong>Descrição:</strong> {{ selectedItem.description }} </v-col>
							<v-col cols="12"> <strong>Preço:</strong> R$ {{ selectedItem.coins }} </v-col>
							<v-col cols="12" v-if="selectedItem.image">
								<v-img :src="selectedItem.image" max-height="200"></v-img>
							</v-col>
						</v-row>
					</v-container>
				</v-card-text>

				<v-card-actions>
					<v-spacer></v-spacer>
					<v-btn color="primary" variant="text" @click="closeDetailsDialog"> Fechar </v-btn>
					<v-btn color="success" variant="text" @click="redeemAdvantage"> Resgatar </v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>
	</v-card>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue';
import { useFetchs } from '../composables/useFetchs';
import { useAuth } from '../composables/useAuth';
import { toast } from 'vue3-toastify';

const { listAdvantages, createAdvantage } = useFetchs();
const { user } = useAuth();

const advantages = ref([]);
const page = ref(1);
const itemsPerPage = 10;
const dialog = ref(false);
const deleteDialog = ref(false);
const detailsDialog = ref(false);
const loading = ref(true);
const formLoading = ref(false);
const selectedItem = ref({});

const defaultItem = {
	name: '',
	description: '',
	coins: 0,
	image: null,
};

const editedIndex = ref(-1);
const editedItem = ref({ ...defaultItem });

const formTitle = computed(() => {
	return editedIndex.value === -1 ? 'Nova Vantagem' : 'Editar Vantagem';
});

const pageCount = computed(() => {
	return Math.ceil(advantages.value.length / itemsPerPage);
});

const paginatedAdvantages = computed(() => {
	const start = (page.value - 1) * itemsPerPage;
	const end = start + itemsPerPage;
	return advantages.value.slice(start, end);
});

const permissions = computed(() => {
	return user.type === 'institution' ? 'enterprise' : user.type;
});

const openDialog = (item) => {
	editedIndex.value = item ? advantages.value.indexOf(item) : -1;
	editedItem.value = item ? Object.assign({}, item) : { ...defaultItem };
	dialog.value = true;
};

const closeDialog = () => {
	dialog.value = false;
	editedItem.value = { ...defaultItem };
	editedIndex.value = -1;
};

const openDeleteDialog = (item) => {
	editedIndex.value = advantages.value.indexOf(item);
	editedItem.value = Object.assign({}, item);
	deleteDialog.value = true;
};

const closeDeleteDialog = () => {
	deleteDialog.value = false;
	editedItem.value = { ...defaultItem };
	editedIndex.value = -1;
};

const openDetailsDialog = (item) => {
	selectedItem.value = Object.assign({}, item);
	detailsDialog.value = true;
};

const closeDetailsDialog = () => {
	detailsDialog.value = false;
	selectedItem.value = {};
};

const redeemAdvantage = () => {
	// TODO: Implement advantage redemption
	toast.info('Funcionalidade em desenvolvimento');
};

const save = async () => {
	if (!editedItem.value.name || !editedItem.value.description || !editedItem.value.coins || !editedItem.value.image) {
		toast.error('Preencha todos os campos!');
		return;
	}

	const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
	if (!allowedTypes.includes(editedItem.value.image.type)) {
		toast.error('Por favor, selecione apenas arquivos de imagem (JPG, PNG, GIF, WEBP)');
		return;
	}

	try {
		formLoading.value = true;
		if (editedIndex.value > -1) {
			Object.assign(advantages.value[editedIndex.value], editedItem.value);
			toast.success('Vantagem atualizada com sucesso!');
		} else {
			const formData = new FormData();
			formData.append('name', editedItem.value.name);
			formData.append('description', editedItem.value.description);
			formData.append('coins', editedItem.value.coins.toString());
			formData.append('enterprise_id', 8);
			formData.append('image', editedItem.value.image);

			const newAdvantage = await createAdvantage(formData);
			advantages.value.push(newAdvantage);
			toast.success('Vantagem criada com sucesso!');
		}
	} catch (error) {
		console.error(error);
		toast.error('Erro ao salvar vantagem!');
	} finally {
		formLoading.value = false;
		closeDialog();
	}
};

const deleteItemConfirm = async () => {
	advantages.value.splice(editedIndex.value, 1);
	closeDeleteDialog();
	toast.success('Vantagem deletada com sucesso!');
};

const fetchAdvantages = async () => {
	try {
		console.log(user.enterprises.id);
		const advantagesData = await listAdvantages(permissions.value === 'enterprise' ? user.enterprises.id : null);
		advantages.value = advantagesData;
	} catch (error) {
		toast.error('Erro ao carregar vantagens');
	} finally {
		loading.value = false;
	}
};

onMounted(async () => {
	await fetchAdvantages();
});
</script>

<style scoped>
td {
	text-align: start;
}
</style>
