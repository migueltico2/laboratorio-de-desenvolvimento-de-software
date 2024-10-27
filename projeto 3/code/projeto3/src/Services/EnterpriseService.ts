import {
	IEnterpriseInterface,
	CreateUserEnterpriseDTO,
	UpdateEnterpriseDTO,
} from '../Interfaces/IEnterpriseInterfaces';
import { EnterpriseRepository } from '../repositories/EnterpriseRepository';
import { Enterprise } from '../entity/Enterprise';
export class EnterpriseService implements IEnterpriseInterface {
	constructor(private enterpriseRepository: EnterpriseRepository) {}

	async findAll() {
		return await this.enterpriseRepository.findAll();
	}

	async findById(id: number) {
		const enterprise = await this.enterpriseRepository.findById(id);
		if (!enterprise) {
			throw new Error('Enterprise not found');
		}
		return enterprise;
	}

	async findByCNPJ(CNPJ: string) {
		const enterprise = await this.enterpriseRepository.findByCNPJ(CNPJ);
		if (!enterprise) {
			throw new Error('Enterprise not found');
		}
		return enterprise;
	}

	async findInstitutions() {
		try {
			const institutions = await this.enterpriseRepository.findByType('institution');
			return institutions;
		} catch (error) {
			throw new Error('Failed to fetch institutions');
		}
	}

	async create(data: CreateUserEnterpriseDTO) {
		console.log('EnterpriseService.create', data);
		if (!Enterprise.isValidCNPJ(data.CNPJ)) {
			throw new Error('Invalid CNPJ');
		}

		const existingEnterprise = await this.enterpriseRepository.findByCNPJ(data.CNPJ);
		if (existingEnterprise) {
			throw new Error('CNPJ already registered');
		}

		if (!['partner', 'institution'].includes(data.type)) {
			throw new Error('Invalid enterprise type');
		}

		return await this.enterpriseRepository.create(data);
	}

	async update(id: number, data: UpdateEnterpriseDTO) {
		await this.findById(id);

		if (data.CNPJ && !Enterprise.isValidCNPJ(data.CNPJ)) {
			throw new Error('Invalid CNPJ');
		}

		if (data.type && !['partner', 'institution'].includes(data.type)) {
			throw new Error('Invalid enterprise type');
		}

		const updated = await this.enterpriseRepository.update(id, data);
		if (!updated) {
			throw new Error('Error updating enterprise');
		}
		return updated;
	}

	async delete(id: number) {
		await this.findById(id);
		await this.enterpriseRepository.delete(id);
	}
}
