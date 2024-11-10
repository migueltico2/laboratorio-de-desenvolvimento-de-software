import { Student } from '../entity/Student';

export interface IStudentInterface {
	findAll(): Promise<Student[]>;
	findById(id: number): Promise<Student | null>;
	findByCPF(CPF: string): Promise<Student | null>;
	create(student: CreateUserStudentDTO): Promise<Student>;
	update(id: number, student: UpdateStudentDTO): Promise<Student>;
	delete(id: number): Promise<void>;
}

export interface CreateUserStudentDTO {
	name: string;
	email: string;
	password: string;
	CPF: string;
	RG: string;
	address: string;
	course: string;
	user_id: number;
	institutionId: number;
}

export interface UpdateStudentDTO extends Partial<CreateUserStudentDTO> {}
