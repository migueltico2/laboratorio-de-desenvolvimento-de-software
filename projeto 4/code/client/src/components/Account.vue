<template>
  <v-card class="mx-auto mb-4 text-start" style="overflow: unset" min-width="80%" max-width="800">
    <v-card-title> Saldo Atual </v-card-title>
    <v-card-text>
      <p class="text-h4 font-weight-bold">R$ {{ accountBalance }}</p>
    </v-card-text>
  </v-card>
  <v-card class="mx-auto text-start overflow-y-auto" max-height="calc(90% - 104px)" min-width="80%" max-width="800">
    <v-card-title> Extrato </v-card-title>
    <v-card-text>
      <v-table>
        <thead>
          <tr>
            <th>Data</th>
            <th>Descrição</th>
            <th>Valor</th>
            <th>Status</th>
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
          <template v-else-if="transactions.length === 0">
            <tr>
              <td colspan="4" class="text-center pa-5">
                <v-icon
                  icon="mdi-alert"
                  size="large"
                  color="warning"
                  class="mb-2"
                />
                <div>Nenhuma transação encontrada</div>
              </td>
            </tr>
          </template>
          <template v-else>
            <tr v-for="transaction in paginatedTransactions" :key="transaction.id">
              <td>{{ formatDate(transaction.date) }}</td>
              <td>{{ transaction.description }}</td>
              <td
                :class="
                  transaction.type === 'credit' ? 'text-success' : 'text-error'
                "
              >
                {{ transaction.type === "credit" ? "+" : "-" }} R$
                {{ transaction.amount }}
              </td>
              <td>
                <v-chip
                  :color="getStatusColor(transaction.status)"
                  size="small"
                >
                  {{ transaction.status }}
                </v-chip>
              </td>
            </tr>
          </template>
        </tbody>
      </v-table>
      <v-pagination
        v-if="transactions.length"
        v-model="page"
        :length="pageCount"
        :total-visible="7"
        class="mt-4"
      />
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useAuth } from "../composables/useAuth";
import { useFetchs } from "../composables/useFetchs";
import { toast } from "vue3-toastify";

const { user } = useAuth();
const { findAccount } = useFetchs();
const loading = ref(true);
const accountBalance = ref(0);
const transactions = ref([]);
const page = ref(1);
const itemsPerPage = 10;

const pageCount = computed(() => {
	return Math.ceil(transactions.value.length / itemsPerPage);
});

const paginatedTransactions = computed(() => {
	const start = (page.value - 1) * itemsPerPage;
	const end = start + itemsPerPage;
	return transactions.value.slice(start, end);
});

const formatDate = (date) => {
  return new Date(date).toLocaleDateString("pt-BR");
};

const getStatusColor = (status) => {
  const colors = {
    completed: "success",
    pending: "warning",
    failed: "error",
  };
  return colors[status] || "grey";
};

const fetchAccountData = async () => {
  try {
    loading.value = true;
    // TODO: Implement API call to fetch account data
    // Mock data for now
    const account = await findAccount(user.value.relation, user.value.id);
    accountBalance.value = account.balance;
    transactions.value = account.transactions;
  } catch (error) {
    toast.error("Erro ao carregar dados da conta");
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  await fetchAccountData();
});
</script>

<style scoped>
.text-success {
  color: #4caf50;
}

.text-error {
  color: #ff5252;
}

td {
  text-align: start;
}
</style>
