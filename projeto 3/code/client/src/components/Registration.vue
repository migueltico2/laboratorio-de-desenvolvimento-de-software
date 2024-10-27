<template>
	<v-card class="mx-auto" max-width="800">
		<v-tabs v-model="activeTab" centered>
			<v-tab value="register">Register</v-tab>
			<v-tab value="login">Login</v-tab>
		</v-tabs>

		<v-card-text>
			<!-- Register Form -->
			<v-window v-model="activeTab">
				<v-window-item value="register">
					<v-form @submit.prevent="handleSubmit">
						<v-container>
							<!-- User Type Selection -->
							<v-radio-group v-model="userType" row>
								<v-radio label="Institution" value="institution" class="radio"></v-radio>
								<v-radio label="Student" value="student" class="radio"></v-radio>
							</v-radio-group>

							<!-- Common Fields -->
							<v-text-field v-model="userFormData.name" label="Name" required></v-text-field>

							<v-text-field
								v-model="userFormData.email"
								label="Email"
								type="email"
								required
							></v-text-field>

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

							<v-btn block color="primary" type="submit" rounded="lg" class="register-btn">
								Register
							</v-btn>
						</v-container>
					</v-form>
				</v-window-item>

				<!-- Login Form -->
				<v-window-item value="login">
					<v-form @submit.prevent="handleLogin">
						<v-container>
							<v-text-field v-model="loginForm.email" label="Email" type="email" required></v-text-field>

							<v-text-field
								v-model="loginForm.password"
								label="Password"
								type="password"
								required
							></v-text-field>

							<v-btn block color="primary" type="submit" rounded="lg" class="register-btn"> Login </v-btn>
						</v-container>
					</v-form>
				</v-window-item>
			</v-window>
		</v-card-text>
	</v-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useToast } from 'vue-toastification';
import { useFetchs } from '../composables/useFetchs';

const emit = defineEmits(['user-registered']);
const toast = useToast();
const activeTab = ref('register');
const userType = ref('institution');
const { getInstitutions, createEnterprise, createStudent, login } = useFetchs();

const institutionTypes = [
	{ title: 'Partner', value: 'partner' },
	{ title: 'Institution', value: 'institution' },
];

const institutions = ref([]);

// Form data for registration
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
	studentInstitution: null,
});

// Form data for login
const loginForm = reactive({
	email: '',
	password: '',
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

		emit('user-registered', {
			type: userType.value,
			...userFormData,
			...(userType.value === 'institution' ? institutionFormData : studentFormData),
			...(userType.value === 'institution' ? institutionFormData : studentFormData),
		});
	} catch (error) {
		toast.error('Error creating user');
		console.error('Registration error:', error);
	}
};

const handleLogin = async () => {
	try {
		const userData = await login(loginForm);
		emit('user-registered', userData);
		toast.success('Login successful');
	} catch (error) {
		toast.error('Login failed');
		console.error('Login error:', error);
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

.v-window {
	margin-top: 20px;
}
</style>
