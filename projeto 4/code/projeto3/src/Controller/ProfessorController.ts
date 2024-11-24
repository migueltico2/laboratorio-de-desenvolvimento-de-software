import { Professor } from '../entity/Professor';
import { AppDataSource } from '../data-source';
import { Request, Response } from 'express';
import { ProfessorService } from '../Services/ProfessorServices';

export class ProfessorController {
	private professorRepository = AppDataSource.getRepository(Professor);
	private professorService = new ProfessorService();

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
