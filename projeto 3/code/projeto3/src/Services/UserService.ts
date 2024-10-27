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

	async login(credentials: LoginUserDTO): Promise<User> {
		const user = await this.userRepository.findByEmail(credentials.email);
		if (!user) {
			throw new Error('Invalid credentials');
		}

		const validPassword = await compare(credentials.password, user.password);
		if (!validPassword) {
			throw new Error('Invalid credentials');
		}

		return user;
	}
}
