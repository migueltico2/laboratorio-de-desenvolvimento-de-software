import { Professor } from '../entity/Professor';
import { AppDataSource } from '../data-source';
import { Request, Response } from 'express';
import { ProfessorService } from '../Services/ProfessorServices';

export class ProfessorController {
	private professorRepository = AppDataSource.getRepository(Professor);
	private professorService = new ProfessorService();

	async sendCoinsToStudent(request: Request, response: Response) {
		const professorId = parseInt(request.params.id);
		const { studentId, coins } = request.body;
		const professor = await this.professorService.sendCoinsToStudent(studentId, coins, professorId);
		return response.json(professor);
	}

	async getHistory(request: Request, response: Response) {
		const professorId = parseInt(request.params.id);
		const history = await this.professorService.getHistory(professorId);
		return response.json(history);
	}

	async create(request: Request, response: Response) {
		try {
			const professor = await this.professorService.create(request.body);
			return response.status(201).json({
				...professor.user,
				professor: professor,
				type: 'professor',
			});
		} catch (error) {
			return response.status(400).json({
				status: 'error',
				message: error.message,
			});
		}
	}
}
