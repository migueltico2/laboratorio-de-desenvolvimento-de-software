import { Request, Response } from 'express';
import { EnterpriseService } from '../Services/EnterpriseService';
import { EnterpriseRepository } from '../repositories/EnterpriseRepository';

export class EnterpriseController {
	private enterpriseService: EnterpriseService;

	constructor() {
		const enterpriseRepository = new EnterpriseRepository();
		this.enterpriseService = new EnterpriseService(enterpriseRepository);
	}

	getAll = async (request: Request, response: Response) => {
		try {
			const enterprises = await this.enterpriseService.findAll();
			return response.json(enterprises);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	getById = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const enterprise = await this.enterpriseService.findById(id);
			return response.json(enterprise);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	getInstitutions = async (request: Request, response: Response) => {
		try {
			const institutions = await this.enterpriseService.findInstitutions();

			if (!institutions || institutions.length === 0) {
				return response.status(404).json({
					message: 'No institutions found',
				});
			}

			return response.status(200).json({
				success: true,
				data: institutions,
			});
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	create = async (request: Request, response: Response) => {
		try {
			console.log('EnterpriseController.create', request.body);
			const enterprise = await this.enterpriseService.create(request.body);
			return response.status(201).json(enterprise);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	update = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			const enterprise = await this.enterpriseService.update(id, request.body);
			return response.json(enterprise);
		} catch (error) {
			return this.handleError(error, response);
		}
	};

	delete = async (request: Request, response: Response) => {
		try {
			const id = parseInt(request.params.id);
			await this.enterpriseService.delete(id);
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
