import { Advantage } from '../entity/Advantage';
import { AppDataSource } from '../data-source';
import { Request, Response } from 'express';
import { AdvantageService } from '../Services/AdvantageServices';
import { AdvantageRepository } from '../repositories/AdvantageRepository';

export class AdvantageController {
	private advantageService: AdvantageService;

	constructor() {
		this.advantageService = new AdvantageService();
	}

	async create(request: Request, response: Response) {
		try {
			const advantageData = request.body;

			if ('files' in request && request.files && Array.isArray(request.files)) {
				advantageData.image = request.files[0].buffer;
			} else if ('files' in request && request.files) {
				advantageData.image = (request.files as any)[0].buffer;
			}

			const advantage = await this.advantageService.create(advantageData);
			return response.status(201).json(advantage);
		} catch (error) {
			return response.status(400).json({ error: error.message });
		}
	}

	async listAllAdvantages(request: Request, response: Response) {
		try {
			const advantages = await this.advantageService.listAllAdvantages();
			return response.json(advantages);
		} catch (error) {
			return response.status(400).json({ error: error.message });
		}
	}
}
