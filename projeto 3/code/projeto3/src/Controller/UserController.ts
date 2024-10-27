import { Request, Response } from 'express';
import { UserService } from '../Services/UserService';
import { UserRepository } from '../repositories/UserRepository';

export class UserController {
	private userService: UserService;

	constructor() {
		const userRepository = new UserRepository();
		this.userService = new UserService(userRepository);
	}

	getAll = async (request: Request, response: Response) => {
		try {
			const users = await this.userService.findAll();
			return response.json(users);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	create = async (request: Request, response: Response) => {
		try {
			const user = await this.userService.create(request.body);
			return response.status(201).json(user);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	update = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const updateData = request.body;

			const user = await this.userService.update(id, updateData);
			if (!user) {
				return response.status(404).json({ message: 'User not found' });
			}

			return response.json(user);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	delete = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			await this.userService.delete(id);
			return response.status(204).send();
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	login = async (request: Request, response: Response) => {
		try {
			const user = await this.userService.login(request.body);
			return response.json(user);
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
