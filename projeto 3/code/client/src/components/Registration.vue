<template>
	<v-card class="mx-auto" max-width="800">
		<v-card-title>User Registration</v-card-title>
		<v-card-subtitle>Register as an institution or student</v-card-subtitle>

		<v-form @submit.prevent="handleSubmit">
			<v-card-text>
				<v-container>
					<!-- User Type Selection -->
					<v-radio-group v-model="userType" row>
						<v-radio label="Institution" value="institution" class="radio"></v-radio>
						<v-radio label="Student" value="student" class="radio"></v-radio>
					</v-radio-group>

					<!-- Common Fields -->
					<v-text-field v-model="userFormData.name" label="Name" required></v-text-field>

					<v-text-field v-model="userFormData.email" label="Email" type="email" required></v-text-field>

					<v-text-field
						v-model="userFormData.password"
						label="Password"
						type="password"
						required
					></v-text-field>

					<!-- Institution Fields -->
					<template v-if="userType === 'institution'">
						<v-text-field v-model="institutionFormData.CNPJ" label="CNPJ" required></v-text-field>

						<v-select
							v-model="institutionFormData.type"
							:items="institutionTypes"
							label="Type"
							required
						></v-select>
					</template>

					<!-- Student Fields -->
					<template v-if="userType === 'student'">
						<v-text-field v-model="studentFormData.CPF" label="CPF" required></v-text-field>

						<v-text-field v-model="studentFormData.RG" label="RG" required></v-text-field>

						<v-text-field v-model="studentFormData.address" label="Address" required></v-text-field>

						<v-text-field v-model="studentFormData.course" label="Course" required></v-text-field>

						<v-select
							v-model="studentFormData.studentInstitution"
							:items="institutions.data"
							item-title="user.name"
							item-value="id"
							label="Institution"
							required
						></v-select>
					</template>
				</v-container>
			</v-card-text>

			<v-card-actions>
				<v-btn block color="black" type="submit" rounded="lg" class="register-btn"> Register </v-btn>
			</v-card-actions>
		</v-form>
	</v-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useToast } from 'vue-toastification';
import { useFetchs } from '../composables/useFetchs';

const emit = defineEmits(['user-registered']);
const toast = useToast();
const userType = ref('institution');
const { getInstitutions, createEnterprise, createStudent } = useFetchs();

const institutionTypes = [
	{ title: 'Partner', value: 'partner' },
	{ title: 'Institution', value: 'institution' },
];

const institutions = ref([]);

const userFormData = reactive({
	name: '',
	email: '',
	password: '',
});

const institutionFormData = reactive({
	CNPJ: '',
	type: '',
});

const studentFormData = reactive({
	CPF: '',
	RG: '',
	address: '',
	course: '',
});

const handleSubmit = async () => {
	try {
		let userData;

		if (userType.value === 'institution') {
			userData = await createEnterprise({ ...userFormData, ...institutionFormData });
			toast.success('Institution created successfully');
		} else {
			userData = await createStudent({ ...userFormData, ...studentFormData });
			toast.success('Student created successfully');
		}

		// Emite o evento com os dados do usuário
		emit('user-registered', {
			type: userType.value,
			...userFormData,
			...(userType.value === 'institution' ? institutionFormData : studentFormData),
		});
	} catch (error) {
		toast.error('Error creating user');
		console.error('Registration error:', error);
	}
};

onMounted(async () => {
	institutions.value = await getInstitutions();
	console.log(institutions.value.data);
});
</script>

<style scoped>
.radio {
	margin-right: 10px;
	opacity: 100 !important;
}

.register-btn {
	margin-bottom: 10px;
	background-color: rgb(60, 175, 241);
}
</style>
