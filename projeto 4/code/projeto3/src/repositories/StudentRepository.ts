import { Repository } from 'typeorm';
import { Student } from '../entity/Student';
import { AppDataSource } from '../data-source';
import { CreateUserStudentDTO, UpdateStudentDTO } from '../Interfaces/IStudentInterfaces';
import { User } from '../entity/User';
import { Enterprise } from '../entity/Enterprise';
import { Account } from '../entity/Account';
import { createHistoryPayloadForStudent } from '../util';
export class StudentRepository {
	private repository: Repository<Student>;
	private repositoryUser: Repository<User>;
	private repositoryEnterprise: Repository<Enterprise>;
	private repositoryAccount: Repository<Account>;

	constructor() {
		this.repository = AppDataSource.getRepository(Student);
		this.repositoryUser = AppDataSource.getRepository(User);
		this.repositoryEnterprise = AppDataSource.getRepository(Enterprise);
		this.repositoryAccount = AppDataSource.getRepository(Account);
	}

	async findAll(): Promise<Student[]> {
		return await this.repository
			.createQueryBuilder('student')
			.leftJoinAndSelect('student.user', 'user')
			.leftJoinAndSelect('student.institution', 'institution')
			.getMany();
	}

	async findById(id: number): Promise<Student | null> {
		return await this.repository.findOne({
			where: { id },
			relations: ['user', 'institution', 'account'],
		});
	}

	async findByCPF(CPF: string): Promise<Student | null> {
		return await this.repository.findOne({ where: { CPF } });
	}

	async removeCoins(id: number, coins: number) {
		const student = await this.findById(id);
		if (!student) {
			throw new Error('Student not found');
		}
		const account = await this.repositoryAccount.findOneBy({ id: student.account.id });
		if (!account) {
			throw new Error('Account not found');
		}
		account.coins -= coins;

		return await this.repositoryAccount.save(account);
	}

	async addCoins(id: number, coins: number) {
		const student = await this.findById(id);
		if (!student) {
			throw new Error('Student not found');
		}
		const account = await this.repositoryAccount.findOneBy({ id: student.account.id });
		if (!account) {
			throw new Error('Account not found');
		}
		account.coins += coins;

		return await this.repositoryAccount.save(account);
	}

	async create(data: CreateUserStudentDTO): Promise<Student> {
		const account = await this.repositoryAccount.findOne({
			where: { id: data.account_id },
		});

		if (!account) {
			throw new Error('Account not found');
		}

		const user = this.repositoryUser.create({
			name: data.name,
			email: data.email,
			password: data.password,
		});
		const savedUser = await this.repositoryUser.save(user);

		const institution = await this.repositoryEnterprise.findOne({
			where: { id: data.institutionId, type: 'institution' },
		});

		if (!institution) {
			throw new Error('Institution not found');
		}

		const student = this.repository.create({
			CPF: data.CPF,
			RG: data.RG,
			address: data.address,
			course: data.course,
			user: savedUser,
			institution,
			account,
		});

		await this.repository.save(student);

		return await this.repository.findOne({
			where: { CPF: data.CPF },
			relations: ['user', 'institution', 'account'],
		});
	}

	async update(id: number, data: UpdateStudentDTO): Promise<Student> {
		const { name, email, password, ...studentData } = data;
		await this.repository.update(id, { ...studentData });
		return await this.findById(id);
	}

	async delete(id: number): Promise<void> {
		await this.repository.delete(id);
	}
}
