import { Request, Response } from 'express';
import { StudentService } from '../Services/StudentService';
import { StudentRepository } from '../repositories/StudentRepository';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { HistoryRepository } from '../repositories/HistoryRepository';
import { AccountRepository } from '../repositories/AccountRepository';
export class StudentController {
	private studentService: StudentService;

	constructor() {
		const studentRepository = new StudentRepository();
		const advantageRepository = new AdvantageRepository();
		const historyRepository = new HistoryRepository();
		const accountRepository = new AccountRepository();
		this.studentService = new StudentService(
			studentRepository,
			advantageRepository,
			historyRepository,
			accountRepository
		);
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

	buyAdvantage = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const { advantageId, coins } = request.body;
			const result = await this.studentService.buyAdvantage(advantageId, coins, id);
			return response.json(result);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	verifyHistory = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const history = await this.studentService.verifyHistory(id);
			return response.json(history);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	addCoins = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const { coins } = request.body;
			const student = await this.studentService.addCoins(id, coins);
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
