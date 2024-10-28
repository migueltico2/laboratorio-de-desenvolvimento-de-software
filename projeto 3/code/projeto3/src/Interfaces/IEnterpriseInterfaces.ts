import { Enterprise } from '../entity/Enterprise';

export interface IEnterpriseInterface {
	findAll(): Promise<Enterprise[]>;
	findById(id: number): Promise<Enterprise | null>;
	findByCNPJ(CNPJ: string): Promise<Enterprise | null>;
	create(enterprise: CreateUserEnterpriseDTO): Promise<Enterprise>;
	update(id: number, enterprise: UpdateEnterpriseDTO): Promise<Enterprise>;
	delete(id: number): Promise<void>;
}

export interface CreateEnterpriseDTO {
	CNPJ: string;
	type: string;
	user_id: number;
}

export interface CreateUserEnterpriseDTO {
	name: string;
	email: string;
	password: string;
	CNPJ: string;
	type: string;
	user_id: number;
}

export interface UpdateEnterpriseDTO extends Partial<CreateEnterpriseDTO> {}
