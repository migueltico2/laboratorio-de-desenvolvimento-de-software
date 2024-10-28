import { StudentRepository } from '../repositories/StudentRepository';
import { IStudentInterface, CreateUserStudentDTO, UpdateStudentDTO } from '../Interfaces/IStudentInterfaces';
import { Student } from '../entity/Student';

export class StudentService implements IStudentInterface {
	constructor(private studentRepository: StudentRepository) {}

	async findAll(): Promise<Student[]> {
		return await this.studentRepository.findAll();
	}

	async findById(id: number): Promise<Student | null> {
		return await this.studentRepository.findById(id);
	}

	async findByCPF(CPF: string): Promise<Student | null> {
		return await this.studentRepository.findByCPF(CPF);
	}

	async create(data: CreateUserStudentDTO): Promise<Student> {
		const existingStudent = await this.studentRepository.findByCPF(data.CPF);
		if (existingStudent) {
			throw new Error('CPF already registered');
		}

		return await this.studentRepository.create(data);
	}

	async update(id: number, data: UpdateStudentDTO): Promise<Student> {
		return await this.studentRepository.update(id, data);
	}

	async delete(id: number): Promise<void> {
		await this.studentRepository.delete(id);
	}
}
