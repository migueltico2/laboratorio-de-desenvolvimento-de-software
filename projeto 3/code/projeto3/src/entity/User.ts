import { Entity, PrimaryGeneratedColumn, Column, OneToMany } from 'typeorm';
import { Enterprise } from './Enterprise';
import { Professor } from './Professor';
import { Student } from './Student';

@Entity()
export class User {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 200 })
	name: string;

	@Column({ length: 200 })
	email: string;

	@Column({ length: 200 })
	password: string;

	@OneToMany(() => Enterprise, (enterprise) => enterprise.user)
	enterprises: Enterprise[];

	@OneToMany(() => Professor, (professor) => professor.user)
	professors: Professor[];

	@OneToMany(() => Student, (student) => student.user)
	students: Student[];
}
