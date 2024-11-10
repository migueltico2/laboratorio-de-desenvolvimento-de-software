import { AppDataSource } from '../data-source';
import { Advantage } from '../entity/Advantage';
import { Enterprise } from '../entity/Enterprise';

export class AdvantageService {
	private advantageRepository = AppDataSource.getRepository(Advantage);
	private enterpriseRepository = AppDataSource.getRepository(Enterprise);

	async create(advantageData: any): Promise<Advantage> {
		try {
			const enterprise = await this.enterpriseRepository.findOneBy({
				id: advantageData.enterprise_id,
			});

			if (!enterprise) {
				throw new Error('Enterprise not found');
			}

			const advantage = this.advantageRepository.create({
				name: advantageData.name,
				description: advantageData.description,
				coins: advantageData.coins,
				image: advantageData.image,
				enterprise: enterprise, // Associa a empresa à vantagem
			});

			return await this.advantageRepository.save(advantage);
		} catch (error) {
			throw new Error(`Error creating advantage: ${error.message}`);
		}
	}

	async listAllAdvantages() {
		return await this.advantageRepository.find();
	}

	async listAdvantagesByEnterprise(enterpriseId: number) {
		if (!enterpriseId) {
			throw new Error('Enterprise ID is required');
		}

		const advantages = await this.advantageRepository.find({
			where: { enterprise: { id: enterpriseId } },
		});

		if (!advantages.length) {
			throw new Error('No advantages found for this enterprise');
		}

		return advantages;
	}
}
