import { Entity, PrimaryGeneratedColumn, Column, OneToMany } from 'typeorm';
import { Enterprise } from './Enterprise';
import { Professor } from './Professor';
import { Student } from './Student';
import { compare, hash } from 'bcrypt';

@Entity()
export class User {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 200 })
	name: string;

	@Column({ unique: true })
	email: string;

	@Column()
	password: string;

	@OneToMany(() => Enterprise, (enterprise) => enterprise.user)
	enterprises: Enterprise[];

	@OneToMany(() => Professor, (professor) => professor.user)
	professors: Professor[];

	@OneToMany(() => Student, (student) => student.user)
	students: Student[];

	public async validatePassword(password: string): Promise<boolean> {
		return await compare(password, this.password);
	}

	public static async hashPassword(password: string): Promise<string> {
		return await hash(password, 10);
	}
}
