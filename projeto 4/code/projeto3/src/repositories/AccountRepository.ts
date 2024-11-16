import { Account } from '../entity/Account';
import { AppDataSource } from '../data-source';

export class AccountRepository {
	private repository = AppDataSource.getRepository(Account);

	constructor() {
		this.repository = AppDataSource.getRepository(Account);
	}

	async listAllAccounts() {
		return await this.repository.find();
	}

	async findById(id: number) {
		return await this.repository.findOneBy({ id });
	}

	async removeCoins(id: number, coins: number) {
		const account = await this.findById(id);
		account.coins -= coins;
		return await this.repository.save(account);
	}

	async addCoins(id: number, coins: number) {
		const account = await this.findById(id);
		account.coins += coins;
		return await this.repository.save(account);
	}

	async create(accountData: Partial<Account>) {
		const account = this.repository.create(accountData);
		return await this.repository.save(account);
	}
}
