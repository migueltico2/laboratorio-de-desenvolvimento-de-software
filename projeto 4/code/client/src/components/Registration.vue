<template>
	<v-card
		class="mx-auto"
		min-width="80%"
		max-width="800"
		style="overflow: auto !important;"
	>
		<v-tabs
			v-model="activeTab"
			centered
		>
			<v-tab value="register">Register</v-tab>
			<v-tab value="login">Login</v-tab>
		</v-tabs>

		<v-card-text>
			<!-- Register Form -->
			<v-window v-model="activeTab">
				<v-window-item value="register">
					<v-form @submit.prevent="handleSubmit">
						<div class="container">
							<!-- User Type Selection -->
							<v-radio-group
								v-model="userType"
								row
							>
								<v-radio
									label="Institution"
									value="institution"
									class="radio"
								></v-radio>
								<v-radio
									label="Student"
									value="student"
									class="radio"
								></v-radio>
							</v-radio-group>

							<!-- Common Fields -->
							<v-text-field
								v-model="userFormData.name"
								label="Name"
								required
							></v-text-field>

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
								<v-text-field
									v-model="institutionFormData.CNPJ"
									label="CNPJ"
									:maxlength="14"
									required
								></v-text-field>

								<v-select
									v-model="institutionFormData.type"
									:items="institutionTypes"
									label="Type"
									required
								></v-select>
							</template>

							<!-- Student Fields -->
							<template v-if="userType === 'student'">
								<v-text-field
									v-model="studentFormData.CPF"
									label="CPF"
									:maxlength="11"
									required
								></v-text-field>

								<v-text-field
									v-model="studentFormData.RG"
									label="RG"
									:maxlength="8"
									required
								></v-text-field>

								<v-text-field
									v-model="studentFormData.address"
									label="Address"
									required
								></v-text-field>

								<v-text-field
									v-model="studentFormData.course"
									label="Course"
									required
								></v-text-field>

								<v-select
									v-model="studentFormData.studentInstitution"
									:items="institutions.data"
									item-title="user.name"
									item-value="id"
									label="Institution"
									required
								></v-select>
							</template>

							<v-btn
								block
								color="primary"
								type="submit"
								rounded="lg"
								class="register-btn"
							>
								Register
							</v-btn>
						</div>
					</v-form>
				</v-window-item>

				<!-- Login Form -->
				<v-window-item value="login">
					<v-form @submit.prevent="handleLogin">
						<div class="container">
							<v-radio-group
								v-model="loginType"
								row
							>
								<v-radio
									label="Institution"
									value="enterprise"
									class="radio"
								></v-radio>
								<v-radio
									label="Professor"
									value="professor"
									class="radio"
								></v-radio>
								<v-radio
									label="Student"
									value="student"
									class="radio"
								></v-radio>
							</v-radio-group>
							<v-text-field
								v-model="loginForm.email"
								label="Email"
								type="email"
								required
							></v-text-field>

							<v-text-field
								v-model="loginForm.password"
								label="Password"
								type="password"
								required
							></v-text-field>

							<v-btn
								block
								color="primary"
								type="submit"
								rounded="lg"
								class="register-btn"
							>
								Login
							</v-btn>
						</div>
					</v-form>
				</v-window-item>
			</v-window>
		</v-card-text>
	</v-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useFetchs } from '../composables/useFetchs';
import { useAuth } from '../composables/useAuth';
import { toast } from 'vue3-toastify';

const { login } = useAuth();
const emit = defineEmits(['user-registered']);
const activeTab = ref('register');
const userType = ref('institution');
const loginType = ref('enterprise');
const { getInstitutions, createEnterprise, createStudent, login: loginUser } = useFetchs();

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
	studentInstitution: null,
});

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

		login(userData);
	} catch (error) {
		toast.error('Error creating user');
		console.error('Registration error:', error);
	}
};

const handleLogin = async () => {
	try {
		let userData;
		userData = await loginUser(loginType.value, loginForm);
		login(userData);
		toast.success('Login successful');
	} catch (error) {
		toast.error('Login failed');
		console.error('Login error:', error);
	}
};

onMounted(async () => {
	institutions.value = await getInstitutions();
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

.container {
	padding: 8px;
}
</style>
