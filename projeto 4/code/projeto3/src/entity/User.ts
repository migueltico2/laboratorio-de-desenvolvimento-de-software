import { Entity, PrimaryGeneratedColumn, Column, OneToOne } from 'typeorm';
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

	@OneToOne(() => Enterprise, (enterprise) => enterprise.user, { cascade: true })
	enterprises: Enterprise[];

	@OneToOne(() => Professor, (professor) => professor.user, { cascade: true })
	professors: Professor[];

	@OneToOne(() => Student, (student) => student.user, { cascade: true })
	students: Student[];

	public async validatePassword(password: string): Promise<boolean> {
		return await compare(password, this.password);
	}

	public static async hashPassword(password: string): Promise<string> {
		return await hash(password, 10);
	}
}
