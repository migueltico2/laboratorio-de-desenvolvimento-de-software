import { StudentRepository } from '../repositories/StudentRepository';
import { IStudentInterface, CreateUserStudentDTO, UpdateStudentDTO } from '../Interfaces/IStudentInterfaces';
import { Student } from '../entity/Student';
import { AppDataSource } from '../data-source';
import { Advantage } from '../entity/Advantage';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { HistoryRepository } from '../repositories/HistoryRepository';
import { createHistoryPayloadForStudent, createGenericHistoryPayloadForStudent } from '../util';
import { AccountRepository } from '../repositories/AccountRepository';
export class StudentService implements IStudentInterface {
	constructor(
		private studentRepository: StudentRepository,
		private advantageRepository: AdvantageRepository,
		private historyRepository: HistoryRepository,
		private accountRepository: AccountRepository
	) {}

	async findAll(): Promise<Student[]> {
		return await this.studentRepository.findAll();
	}

	async findById(id: number): Promise<Student | null> {
		return await this.studentRepository.findById(id);
	}

	async findByCPF(CPF: string): Promise<Student | null> {
		return await this.studentRepository.findByCPF(CPF);
	}

	async buyAdvantage(advantageId: number, coins: number, studentId: number) {
		console.log(advantageId, coins, studentId);
		const advantage = await this.advantageRepository.findById(advantageId);
		if (!advantage) {
			throw new Error('Advantage not found');
		}
		if (coins < advantage.coins) {
			throw new Error('Not enough coins');
		}
		await this.studentRepository.removeCoins(studentId, coins);
		const historyEntry = createHistoryPayloadForStudent(studentId, advantageId, coins, 'compra');
		await this.historyRepository.create(historyEntry);
	}

	async addCoins(id: number, coins: number) {
		await this.studentRepository.addCoins(id, coins);
		const historyEntry = createGenericHistoryPayloadForStudent(id, coins, 'adicionar');
		await this.historyRepository.create(historyEntry);
	}

	async verifyHistory(studentId: number) {
		const student = await this.studentRepository.findById(studentId);
		if (!student) {
			throw new Error('Student not found');
		}
		return await this.historyRepository.listAllHistoriesByStudent(studentId);
	}

	async create(data: CreateUserStudentDTO): Promise<Student> {
		const existingStudent = await this.studentRepository.findByCPF(data.CPF);
		if (existingStudent) {
			throw new Error('CPF already registered');
		}

		const account = await this.accountRepository.create({ coins: 0 });

		const studentData = {
			...data,
			account_id: account.id,
		};

		return await this.studentRepository.create(studentData);
	}

	async update(id: number, data: UpdateStudentDTO): Promise<Student> {
		return await this.studentRepository.update(id, data);
	}

	async delete(id: number): Promise<void> {
		await this.studentRepository.delete(id);
	}
}
