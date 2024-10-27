import { Student } from '../entity/Student';
import { AppDataSource } from '../data-source';
import { Request, Response } from 'express';

export class StudentController {
	private studentRepository = AppDataSource.getRepository(Student);

	async getAll(request: Request, response: Response) {
		const students = await this.studentRepository.find();
		return response.json(students);
	}

	async create(request: Request, response: Response) {
		const newStudent = this.studentRepository.create(request.body);
		const results = await this.studentRepository.save(newStudent);
		return response.json(results);
	}

	async delete(request: Request, response: Response) {
		const id = request.params.id;
		const result = await this.studentRepository.delete(id);
		return response.json(result);
	}

	async update(request: Request, response: Response) {
		const id = parseInt(request.params.id);
		const updateData = request.body;

		try {
			const result = await this.studentRepository.update(id, updateData);

			if (result.affected === 0) {
				return response.status(404).json({ message: 'Student not found' });
			}

			const updatedStudent = await this.studentRepository.findOne({ where: { id } });
			return response.json(updatedStudent);
		} catch (error) {
			return response.status(400).json({ message: 'Error updating student', error: error.message });
		}
	}
}
