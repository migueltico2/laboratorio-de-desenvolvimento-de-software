import { History } from '../entity/History';
import { AppDataSource } from '../data-source';

export class HistoryRepository {
	private repository = AppDataSource.getRepository(History);

	constructor() {
		this.repository = AppDataSource.getRepository(History);
	}

	async listAllHistoriesByStudent(studentId: number) {
		return await this.repository.find({
			where: {
				student: { id: studentId },
			},
			relations: ['student'],
		});
	}

	async findById(id: number) {
		return await this.repository.findOneBy({ id });
	}

	async create(historyData: Partial<History>) {
		const history = this.repository.create(historyData);
		return await this.repository.save(history);
	}
}
