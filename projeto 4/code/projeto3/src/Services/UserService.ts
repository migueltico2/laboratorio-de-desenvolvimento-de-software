import { hash, compare } from 'bcrypt';
import { IUserInterface, CreateUserDTO, UpdateUserDTO, LoginUserDTO } from '../Interfaces/IUserInterface';
import { UserRepository } from '../repositories/UserRepository';
import { User } from '../entity/User';

export class UserService implements IUserInterface {
	constructor(private userRepository: UserRepository) {}

	async findAll(): Promise<User[]> {
		return await this.userRepository.findAll();
	}

	async findById(id: number): Promise<User | null> {
		const user = await this.userRepository.findById(id);
		if (!user) {
			throw new Error('User not found');
		}
		return user;
	}

	async findByEmail(email: string): Promise<User | null> {
		const user = await this.userRepository.findByEmail(email);
		if (!user) {
			throw new Error('User not found');
		}
		return user;
	}

	async create(data: CreateUserDTO): Promise<User> {
		const existingUser = await this.userRepository.findByEmail(data.email);
		if (existingUser) {
			throw new Error('Email already in use');
		}

		const hashedPassword = await User.hashPassword(data.password);
		return await this.userRepository.create({
			...data,
			password: hashedPassword,
		});
	}

	async update(id: number, updateData: UpdateUserDTO): Promise<User> {
		const existingUser = await this.findById(id);

		if (!existingUser) {
			throw new Error('User not found');
		}

		if (updateData.password) {
			updateData.password = await hash(updateData.password, 10);
		}

		return await this.userRepository.update(id, updateData);
	}

	async delete(id: number): Promise<void> {
		await this.findById(id);
		await this.userRepository.delete(id);
	}

	async loginEnterprise(credentials: LoginUserDTO): Promise<User> {
		try {
			const user = await this.userRepository.findByEmailWithEnterprise(credentials.email);
			if (!user) {
				throw new Error('Invalid credentials for user');
			}
			if (credentials.password !== user.password) {
				throw new Error('Invalid credentials password');
			}

			return user;
		} catch (error: any) {
			console.error('Error in loginEnterprise', error);
			throw error;
		}
	}

	async loginStudent(credentials: LoginUserDTO): Promise<User> {
		const user = await this.userRepository.findByEmailWithStudent(credentials.email);
		if (!user) {
			throw new Error('Invalid credentials for user');
		}

		if (credentials.password !== user.password) {
			throw new Error('Invalid credentials password');
		}

		return user;
	}
}
