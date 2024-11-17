import { AccountRepository } from '../repositories/AccountRepository';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { HistoryRepository } from '../repositories/HistoryRepository';
import { createHistoryPayloadForStudent } from '../util';

export class AccountService {
	constructor(
		private accountRepository: AccountRepository,
		private advantageRepository: AdvantageRepository,
		private historyRepository: HistoryRepository
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
}
