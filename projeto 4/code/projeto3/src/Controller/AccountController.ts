import { Request, Response } from 'express';
import { AccountService } from '../Services/AccountService';
import { AccountRepository } from '../repositories/AccountRepository';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { HistoryRepository } from '../repositories/HistoryRepository';

export class AccountController {
	private accountService: AccountService;

	constructor() {
		const accountRepository = new AccountRepository();
		const advantageRepository = new AdvantageRepository();
		const historyRepository = new HistoryRepository();
		this.accountService = new AccountService(accountRepository, advantageRepository, historyRepository);
	}

	buyAdvantage = async (request: Request, response: Response) => {
		try {
			const accountId = parseInt(request.params.id);
			const { advantageId, coins, studentId } = request.body;
			const result = await this.accountService.buyAdvantage(advantageId, coins, accountId, studentId);
			return response.json(result);
		} catch (error) {
			return response.status(400).json({
				status: 'error',
				message: error.message || 'Unexpected error occurred',
			});
		}
	};
}
