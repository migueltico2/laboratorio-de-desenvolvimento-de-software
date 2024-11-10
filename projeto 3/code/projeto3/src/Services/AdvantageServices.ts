import {
	IEnterpriseInterface,
	CreateUserEnterpriseDTO,
	UpdateEnterpriseDTO,
} from '../Interfaces/IEnterpriseInterfaces';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { Advantage } from '../entity/Advantage';

export class AdvantageService {
	private advantageRepository: AdvantageRepository;

	constructor() {
		this.advantageRepository = new AdvantageRepository();
	}

	async create(advantageData: Partial<Advantage>) {
		if (!advantageData.name || !advantageData.coins || !advantageData.description) {
			throw new Error('Missing required fields');
		}

		if (advantageData.coins <= 0) {
			throw new Error('Coins must be greater than 0');
		}

		return await this.advantageRepository.create(advantageData);
	}

	async listAllAdvantages() {
		return await this.advantageRepository.listAllAdvantages();
	}
}
