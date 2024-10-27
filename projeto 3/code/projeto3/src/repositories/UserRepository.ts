import { Repository } from 'typeorm';
import { User } from '../entity/User';
import { AppDataSource } from '../data-source';
import { CreateUserDTO } from '../Interfaces/IUserInterface';
export class UserRepository {
	private repository: Repository<User>;

	constructor() {
		this.repository = AppDataSource.getRepository(User);
	}

	async findAll(): Promise<User[]> {
		return await this.repository.find();
	}

	async findById(id: number): Promise<User | null> {
		return await this.repository.findOneBy({ id });
	}

	async findByEmail(email: string): Promise<User | null> {
		return await this.repository.findOneBy({ email });
	}

	async findByEmailWithEnterprise(email: string): Promise<User | null> {
		const user = await this.repository
			.createQueryBuilder('user')
			.leftJoinAndSelect('user.enterprises', 'enterprises')
			.where('user.email = :email', { email })
			.getOne();

		console.log('repository', user);
		// Verifica se o user existe e se enterprises existe
		if (!user?.enterprises) {
			return null;
		}

		return user;
	}

	async findByEmailWithStudent(email: string): Promise<User | null> {
		const user = await this.repository
			.createQueryBuilder('user')
			.leftJoinAndSelect('user.students', 'student')
			.where('user.email = :email', { email })
			.getOne();

		if (!user?.students) {
			return null;
		}

		return user;
	}

	async create(data: CreateUserDTO): Promise<User> {
		const user = this.repository.create(data);
		return await this.repository.save(user);
	}

	async update(id: number, data: Partial<User>): Promise<User> {
		await this.repository.update(id, data);
		return (await this.findById(id)) as User;
	}

	async delete(id: number): Promise<void> {
		await this.repository.delete(id);
	}
}
