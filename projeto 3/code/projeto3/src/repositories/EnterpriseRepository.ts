import { Repository } from 'typeorm';
import { Enterprise } from '../entity/Enterprise';
import { AppDataSource } from '../data-source';
import { CreateUserEnterpriseDTO, UpdateEnterpriseDTO } from '../Interfaces/IEnterpriseInterfaces';
import { User } from '../entity/User';
export class EnterpriseRepository {
	private repository: Repository<Enterprise>;
	private repositoryUser: Repository<User>;

	constructor() {
		this.repository = AppDataSource.getRepository(Enterprise);
		this.repositoryUser = AppDataSource.getRepository(User);
	}

	async findAll(): Promise<Enterprise[]> {
		return await this.repository.find({
			relations: ['user', 'advantages'],
		});
	}

	async findById(id: number): Promise<Enterprise | null> {
		return await this.repository.findOne({
			where: { id },
			relations: ['user', 'advantages'],
		});
	}

	async findByCNPJ(CNPJ: string): Promise<Enterprise | null> {
		return await this.repository.findOne({
			where: { CNPJ },
		});
	}

	async create(data: CreateUserEnterpriseDTO): Promise<Enterprise> {
		const user = this.repositoryUser.create({ name: data.name, email: data.email, password: data.password });
		const savedUser = await this.repositoryUser.save(user);

		const { name, email, password, ...enterpriseData } = data;
		const dataEnterprise = {
			...enterpriseData,
			user_id: savedUser.id,
		};

		const enterprise = this.repository.create(dataEnterprise);
		return await this.repository.save(enterprise);
	}

	async update(id: number, data: UpdateEnterpriseDTO): Promise<Enterprise> {
		await this.repository.update(id, data);
		return (await this.findById(id)) as Enterprise;
	}

	async delete(id: number): Promise<void> {
		await this.repository.delete(id);
	}
}
