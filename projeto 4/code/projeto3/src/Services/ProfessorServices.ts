import { AppDataSource } from '../data-source';
import { Advantage } from '../entity/Advantage';
import { Enterprise } from '../entity/Enterprise';
import { Professor } from '../entity/Professor';
import { Student } from '../entity/Student';
import { StudentService } from './StudentService';
import { StudentRepository } from '../repositories/StudentRepository';
import { AdvantageRepository } from '../repositories/AdvantageRepository';
import { HistoryRepository } from '../repositories/HistoryRepository';
import { AccountRepository } from '../repositories/AccountRepository';
import { History } from '../entity/History';
import { createGenericHistoryPayloadForTeacher } from '../util/index';
import { Account } from '../entity/Account';
import { CreateProfessorDTO } from '../Interfaces/IProfessorInterfaces';
import { User } from '../entity/User';

export class ProfessorService {
	private professorRepository = AppDataSource.getRepository(Professor);
	private studentRepository = AppDataSource.getRepository(Student);
	private historyRepository = AppDataSource.getRepository(History);
	private accountRepository = AppDataSource.getRepository(Account);
	private studentService = new StudentService(
		new StudentRepository(),
		new AdvantageRepository(),
		new HistoryRepository(),
		new AccountRepository()
	);
	private userRepository = AppDataSource.getRepository(User);
	private enterpriseRepository = AppDataSource.getRepository(Enterprise);

	async sendCoinsToStudent(studentId: number, coins: number, professorId: number) {
		// Buscar professor com todas as relações necessárias
		const professor = await this.professorRepository.findOne({
			where: { id: professorId },
			relations: ['account', 'user'], // Incluir a relação com a conta
		});

		if (!professor) {
			throw new Error('Professor not found');
		}

		// Buscar estudante com suas relações
		const student = await this.studentRepository.findOne({
			where: { id: studentId },
			relations: ['account', 'user'],
		});

		if (!student) {
			throw new Error('Student not found');
		}

		// Verificar se as contas existem
		if (!professor.account) {
			throw new Error('Professor account not found');
		}

		if (!student.account) {
			throw new Error('Student account not found');
		}

		// Verificar se há saldo suficiente
		if (professor.account.coins < coins) {
			throw new Error('Professor does not have enough coins');
		}

		// Atualizar saldo do professor
		professor.account.coins -= coins;
		await this.accountRepository.save(professor.account);

		// Adicionar moedas ao estudante
		await this.studentService.addCoins(studentId, coins);

		// Criar histórico
		const historyEntry = createGenericHistoryPayloadForTeacher(professorId, studentId, coins, 'transferencia');
		await this.historyRepository.create(historyEntry);

		return {
			message: 'Coins transferred successfully',
			professor: {
				id: professor.id,
				name: professor.user.name,
				remainingCoins: professor.account.coins,
			},
			student: {
				id: student.id,
				name: student.user.name,
				receivedCoins: coins,
			},
		};
	}

	async getHistory(professorId: number) {
		const history = await this.historyRepository.find({ where: { professor: { id: professorId } } });
		return history;
	}

	async create(data: CreateProfessorDTO): Promise<Professor> {
		// Verificar se o CPF já está cadastrado
		const existingProfessor = await this.professorRepository.findOne({
			where: { CPF: data.CPF },
		});

		if (existingProfessor) {
			throw new Error('CPF already registered');
		}

		// Criar conta com saldo inicial
		const account = this.accountRepository.create({ coins: 1000 });

		// Criar usuário
		const user = this.userRepository.create({
			name: data.name,
			email: data.email,
			password: data.password,
		});
		const savedUser = await this.userRepository.save(user);

		// Buscar instituição
		const institution = await this.enterpriseRepository.findOne({
			where: {
				id: data.institutionId,
				type: 'institution',
			},
		});

		if (!institution) {
			throw new Error('Institution not found');
		}

		// Criar professor com todas as relações
		const professor = this.professorRepository.create({
			CPF: data.CPF,
			department: data.department,
			user: savedUser,
			account: account,
			institution: institution,
		});

		await this.professorRepository.save(professor);

		// Retornar professor com todas as relações carregadas
		return await this.professorRepository.findOne({
			where: { CPF: data.CPF },
			relations: ['user', 'institution', 'account'],
		});
	}
}
