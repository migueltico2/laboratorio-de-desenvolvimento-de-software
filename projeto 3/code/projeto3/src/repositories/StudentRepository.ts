import { Repository } from 'typeorm';
import { Student } from '../entity/Student';
import { AppDataSource } from '../data-source';
import { CreateUserStudentDTO, UpdateStudentDTO } from '../Interfaces/IStudentInterfaces';
import { User } from '../entity/User';
import { Enterprise } from '../entity/Enterprise';
export class StudentRepository {
	private repository: Repository<Student>;
	private repositoryUser: Repository<User>;
	private repositoryEnterprise: Repository<Enterprise>;

	constructor() {
		this.repository = AppDataSource.getRepository(Student);
		this.repositoryUser = AppDataSource.getRepository(User);
		this.repositoryEnterprise = AppDataSource.getRepository(Enterprise);
	}

	async findAll(): Promise<Student[]> {
		return await this.repository
			.createQueryBuilder('student')
			.leftJoinAndSelect('student.user', 'user')
			.leftJoinAndSelect('student.institution', 'institution')
			.getMany();
	}

	async findById(id: number): Promise<Student | null> {
		return await this.repository.findOne({ where: { id }, relations: ['user'] });
	}

	async findByCPF(CPF: string): Promise<Student | null> {
		return await this.repository.findOne({ where: { CPF } });
	}

	async create(data: CreateUserStudentDTO): Promise<Student> {
		const user = this.repositoryUser.create({ name: data.name, email: data.email, password: data.password });
		const institution = await this.repositoryEnterprise.findOne({
			where: { id: data.institutionId, type: 'institution' },
		});
		const savedUser = await this.repositoryUser.save(user);
		const { name, email, password, ...studentData } = data;
		const student = this.repository.create({
			CPF: studentData.CPF,
			RG: studentData.RG,
			address: studentData.address,
			course: studentData.course,
			user: savedUser,
			institution,
		});

		return await this.repository.save({ ...student, user: savedUser, institution });
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
