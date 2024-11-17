import { Request, Response } from 'express';
import { AccountService } from '../Services/AccountService';
import { AccountRepository } from '../repositories/AccountRepository';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { HistoryRepository } from '../repositories/HistoryRepository';
import { StudentRepository } from '../repositories/StudentRepository';

export class AccountController {
	private accountService: AccountService;

	constructor() {
		const accountRepository = new AccountRepository();
		const advantageRepository = new AdvantageRepository();
		const historyRepository = new HistoryRepository();
		const studentRepository = new StudentRepository();
		this.accountService = new AccountService(
			accountRepository,
			advantageRepository,
			historyRepository,
			studentRepository
		);
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

	transferCoins = async (request: Request, response: Response) => {
		try {
			const fromAccountId = parseInt(request.params.fromId);
			const { toAccountId, coins, professorId, studentId } = request.body;

			const result = await this.accountService.transferCoins(
				fromAccountId,
				toAccountId,
				coins,
				professorId,
				studentId
			);

			return response.json(result);
		} catch (error) {
			return response.status(400).json({
				status: 'error',
				message: error.message || 'Unexpected error occurred',
			});
		}
	};

	findByRelation = async (request: Request, response: Response) => {
		const { relation, id } = request.params;
		const result = await this.accountService.findByRelation(relation, parseInt(id));
		// result.transactions = await this.accountService.;
		return response.json(result);
	};
}
