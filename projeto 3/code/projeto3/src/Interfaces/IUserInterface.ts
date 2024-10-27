import { User } from '../entity/User';

export interface IUserInterface {
	findAll(): Promise<User[]>;
	findById(id: number): Promise<User | null>;
	create(user: User): Promise<User>;
	update(id: number, user: User): Promise<User>;
	delete(id: number): Promise<void>;
}

export interface CreateUserDTO {
	name: string;
	email: string;
	password: string;
}

export interface UpdateUserDTO extends Partial<CreateUserDTO> {}

export interface LoginUserDTO {
	email: string;
	password: string;
}
