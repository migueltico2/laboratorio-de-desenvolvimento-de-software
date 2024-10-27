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
		return await this.repository
			.createQueryBuilder('enterprise')
			.leftJoinAndSelect('enterprise.user', 'user')
			.leftJoinAndSelect('enterprise.advantages', 'advantages')
			.getMany();
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

	findByType = async (type: string) => {
		try {
			const institutions = await this.repository.find({
				where: { type: type },
				relations: ['user'],
			});
			return institutions;
		} catch (error) {
			throw new Error('Error finding institutions');
		}
	};

	async create(data: CreateUserEnterpriseDTO): Promise<Enterprise> {
		console.log(data);
		const user = this.repositoryUser.create({ name: data.name, email: data.email, password: data.password });
		const savedUser = await this.repositoryUser.save(user);
		const { name, email, password, ...enterpriseData } = data;
		const enterprise = this.repository.create({
			CNPJ: enterpriseData.CNPJ,
			type: enterpriseData.type,
			user: savedUser,
		});

		return await this.repository.save({ ...enterprise, user: savedUser });
	}

	async update(id: number, data: UpdateEnterpriseDTO): Promise<Enterprise> {
		await this.repository.update(id, data);
		return (await this.findById(id)) as Enterprise;
	}

	async delete(id: number): Promise<void> {
		await this.repository.delete(id);
	}
}
