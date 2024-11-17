import { AccountRepository } from '../repositories/AccountRepository';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { HistoryRepository } from '../repositories/HistoryRepository';
import { createHistoryPayloadForStudent, createGenericHistoryPayloadForTeacher } from '../util';
import { StudentRepository } from '../repositories/StudentRepository';

export class AccountService {
	constructor(
		private accountRepository: AccountRepository,
		private advantageRepository: AdvantageRepository,
		private historyRepository: HistoryRepository,
		private studentRepository: StudentRepository
	) {}

	async buyAdvantage(advantageId: number, coins: number, accountId: number, studentId: number) {
		const account = await this.accountRepository.findById(accountId);
		if (!account) {
			throw new Error('Account not found');
		}

		const advantage = await this.advantageRepository.findById(advantageId);
		if (!advantage) {
			throw new Error('Advantage not found');
		}

		if (account.coins < advantage.coins) {
			throw new Error('Not enough coins');
		}

		// Remover moedas da conta
		await this.accountRepository.removeCoins(accountId, coins);

		// Criar histórico
		if (studentId) {
			const historyEntry = createHistoryPayloadForStudent(studentId, advantageId, coins, 'compra');
			await this.historyRepository.create(historyEntry);
		}

		return account;
	}

	async transferCoins(
		fromAccountId: number,
		toAccountId: number,
		coins: number,
		professorId: number,
		studentId: number
	) {
		// Buscar contas
		const fromAccount = await this.accountRepository.findById(fromAccountId);
		const toAccount = await this.accountRepository.findById(toAccountId);

		if (!fromAccount) {
			throw new Error('Source account not found');
		}

		if (!toAccount) {
			throw new Error('Destination account not found');
		}

		// Verificar saldo
		if (fromAccount.coins < coins) {
			throw new Error('Not enough coins');
		}

		// Realizar transferência
		await this.accountRepository.removeCoins(fromAccountId, coins);
		await this.accountRepository.addCoins(toAccountId, coins);

		// Criar histórico
		const historyEntry = createGenericHistoryPayloadForTeacher(professorId, studentId, coins, 'transferencia');
		await this.historyRepository.create(historyEntry);

		return {
			message: 'Coins transferred successfully',
			fromAccount: {
				id: fromAccount.id,
				remainingCoins: fromAccount.coins,
			},
			toAccount: {
				id: toAccount.id,
				receivedCoins: coins,
			},
		};
	}
}
