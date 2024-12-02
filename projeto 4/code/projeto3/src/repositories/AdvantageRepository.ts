import { Advantage } from '../entity/Advantage';
import { AppDataSource } from '../data-source';

export class AdvantageRepository {
	private repository = AppDataSource.getRepository(Advantage);

	constructor() {
		this.repository = AppDataSource.getRepository(Advantage);
	}

	async listAllAdvantages() {
		return await this.repository.find();
	}

	async findById(id: number) {
		return await this.repository.findOneBy({ id });
	}

	async create(advantageData: Partial<Advantage>) {
		const advantage = this.repository.create(advantageData);
		return await this.repository.save(advantage);
	}

	async findAdvantageById(id: number) {
		return await this.repository.findOne({
			where: { id },
		});
	}

	async listAdvantagesByEnterprise(enterpriseId: number) {
		return await this.repository.find({
			where: {
				enterprise: { id: enterpriseId },
			},
			relations: ['enterprise'],
		});
	}
}
