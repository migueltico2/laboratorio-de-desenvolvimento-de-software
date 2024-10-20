import { Entity, PrimaryGeneratedColumn, Column, OneToMany } from 'typeorm';
import { Student } from './Student';
import { Professor } from './Professor';

@Entity()
export class Account {
	@PrimaryGeneratedColumn()
	id: number;

	@Column('double')
	coins: number;

	@OneToMany(() => Student, (student) => student.account)
	students: Student[];

	@OneToMany(() => Professor, (professor) => professor.account)
	professors: Professor[];
}
