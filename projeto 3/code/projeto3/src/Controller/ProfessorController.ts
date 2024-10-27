import { Request, Response } from 'express';
import { AppDataSource } from '../data-source';
import { Professor } from '../entity/Professor';
import { User } from '../entity/User';

export class ProfessorController {
	private professorRepository = AppDataSource.getRepository(Professor);
	private userRepository = AppDataSource.getRepository(User);
	// GET - Fetch all professors
	async getAll(request: Request, response: Response) {
		const professors = await this.professorRepository.find();
		return response.json(professors);
	}

	// POST - Create a new professor
	async create(request: Request, response: Response) {
		const newProfessor = this.professorRepository.create(request.body);
		const results = await this.professorRepository.save(newProfessor);
		return response.json(results);
	}

	// DELETE - Remove a professor
	async delete(request: Request, response: Response) {
		const id = request.params.id;
		const result = await this.professorRepository.delete(id);
		return response.json(result);
	}

	// PUT - Update a professor
	async update(request: Request, response: Response) {
		const id = parseInt(request.params.id);
		const updateData = request.body;

		try {
			const result = await this.professorRepository.update(id, updateData);

			if (result.affected === 0) {
				return response.status(404).json({ message: 'Professor not found' });
			}

			const updatedProfessor = await this.professorRepository.findOne({ where: { id } });
			return response.json(updatedProfessor);
		} catch (error) {
			return response.status(400).json({ message: 'Error updating professor', error: error.message });
		}
	}
}
