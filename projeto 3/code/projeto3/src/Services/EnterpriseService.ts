import {
	IEnterpriseInterface,
	CreateUserEnterpriseDTO,
	UpdateEnterpriseDTO,
} from '../Interfaces/IEnterpriseInterfaces';
import { EnterpriseRepository } from '../repositories/EnterpriseRepository';

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

	async create(data: CreateUserEnterpriseDTO) {
		// Validar CNPJ
		if (!this.isValidCNPJ(data.CNPJ)) {
			throw new Error('Invalid CNPJ');
		}

		// Verificar se já existe empresa com este CNPJ
		const existingEnterprise = await this.enterpriseRepository.findByCNPJ(data.CNPJ);
		if (existingEnterprise) {
			throw new Error('CNPJ already registered');
		}
		// Validar tipo
		if (!['partner', 'institution'].includes(data.type)) {
			throw new Error('Invalid enterprise type');
		}

		return await this.enterpriseRepository.create(data);
	}

	async update(id: number, data: UpdateEnterpriseDTO) {
		// Verificar se existe
		await this.findById(id);

		// Validar CNPJ se fornecido
		if (data.CNPJ && !this.isValidCNPJ(data.CNPJ)) {
			throw new Error('Invalid CNPJ');
		}

		// Validar tipo se fornecido
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

	private isValidCNPJ(cnpj: string): boolean {
		// Implementar validação de CNPJ
		return cnpj.length === 14 && /^\d+$/.test(cnpj);
	}
}
