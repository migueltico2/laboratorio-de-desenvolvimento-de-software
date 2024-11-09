import { Account } from '../entity/Account';
import { AppDataSource } from '../data-source';
import { Request, Response } from 'express';

export class AccountController {
	private accountRepository = AppDataSource.getRepository(Account);

	async create(request: Request, response: Response) {
		const newAccount = this.accountRepository.create(request.body);
		const results = await this.accountRepository.save(newAccount);
		return response.json(results);
	}

	async checkAccount(request: Request, response: Response) {
		const id = parseInt(request.params.id);
		const account = await this.accountRepository.findOne({ where: { id } });
		return response.json(account);
	}
}
