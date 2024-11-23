import { History } from '../entity/History';
import { AppDataSource } from '../data-source';
import { Student } from '../entity/Student';
import { Advantage } from '../entity/Advantage';
import { Professor } from '../entity/Professor';

interface CreateHistoryDTO {
	coins: number;
	type: string;
	date: Date;
	student?: { id: number };
	professor?: { id: number };
	advantage?: { id: number };
	description?: string;
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

	async listAllHistories(relation: string, id: number) {
		return await this.repository.find({
			where: {
				[relation]: { id },
			},
			relations: ['student', 'student.user', 'professor', 'professor.user', 'advantage'],
			order: {
				date: 'DESC',
			},
		});
	}

	async create(historyData: CreateHistoryDTO) {
		const historyBase = {
			coins: historyData.coins,
			type: historyData.type,
			date: historyData.date,
			description: historyData.description,
		};

		if (historyData.student) {
			const student = await AppDataSource.getRepository(Student).findOneBy({
				id: historyData.student.id,
			});
			if (student) {
				Object.assign(historyBase, { student });
			}
		}

		if (historyData.professor) {
			const professor = await AppDataSource.getRepository(Professor).findOneBy({
				id: historyData.professor.id,
			});
			if (professor) {
				Object.assign(historyBase, { professor });
			}
		}

		if (historyData.advantage) {
			const advantage = await AppDataSource.getRepository(Advantage).findOneBy({
				id: historyData.advantage.id,
			});
			if (advantage) {
				Object.assign(historyBase, { advantage });
			}
		}

		const history = this.repository.create(historyBase);
		const savedHistory = await this.repository.save(history);

		return await this.repository.findOne({
			where: { id: savedHistory.id },
			relations: ['student', 'professor', 'advantage'],
		});
	}
}
