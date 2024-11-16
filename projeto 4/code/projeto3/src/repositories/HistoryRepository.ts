import { History } from '../entity/History';
import { AppDataSource } from '../data-source';
import { Student } from '../entity/Student';
import { Advantage } from '../entity/Advantage';

interface CreateHistoryDTO {
	coins: number;
	type: string;
	date: Date;
	student: { id: number };
	advantage?: { id: number };
}

export class HistoryRepository {
	private repository = AppDataSource.getRepository(History);

	async listAllHistoriesByStudent(studentId: number) {
		return await this.repository.find({
			where: {
				student: { id: studentId },
			},
			relations: ['student', 'advantage'],
			order: {
				date: 'DESC',
			},
		});
	}

	async create(historyData: CreateHistoryDTO) {
		const student = await AppDataSource.getRepository(Student).findOneBy({
			id: historyData.student.id,
		});

		let advantage = null;
		if (historyData.advantage) {
			advantage = await AppDataSource.getRepository(Advantage).findOneBy({
				id: historyData.advantage.id,
			});
		}

		const history = this.repository.create({
			coins: historyData.coins,
			type: historyData.type,
			date: historyData.date,
			student: student,
			advantage: advantage,
		});

		const savedHistory = await this.repository.save(history);

		return await this.repository.findOne({
			where: { id: savedHistory.id },
			relations: ['student', 'advantage'],
		});
	}
}
