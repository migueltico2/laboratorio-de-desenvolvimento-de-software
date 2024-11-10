import { Request, Response } from 'express';
import { StudentService } from '../Services/StudentService';
import { StudentRepository } from '../repositories/StudentRepository';

export class StudentController {
	private studentService: StudentService;

	constructor() {
		const studentRepository = new StudentRepository();
		this.studentService = new StudentService(studentRepository);
	}

	getAll = async (request: Request, response: Response) => {
		try {
			const students = await this.studentService.findAll();
			return response.json(students);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	getById = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const student = await this.studentService.findById(id);
			return response.json(student);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	create = async (request: Request, response: Response) => {
		try {
			const student = await this.studentService.create(request.body);
			return response.json({
				...student.user,
				students: student,
				type: 'student',
			});
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	update = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const student = await this.studentService.update(id, request.body);
			return response.json(student);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	delete = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			await this.studentService.delete(id);
			return response.status(204).send();
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	private handleError(error: any, response: Response) {
		console.error(error);
		return response.status(400).json({
			status: 'error',
			message: error.message || 'Unexpected error occurred',
		});
	}
}
