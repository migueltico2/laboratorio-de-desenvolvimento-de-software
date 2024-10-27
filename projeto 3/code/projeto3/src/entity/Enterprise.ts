import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany, JoinColumn } from 'typeorm';
import { User } from './User';
import { Advantage } from './Advantage';
import { Student } from './Student';
import { Professor } from './Professor';

@Entity()
export class Enterprise {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 14 })
	CNPJ: string;

	@Column({ length: 11 })
	type: string;

	@ManyToOne(() => User, (user) => user.enterprises)
	@JoinColumn({ name: 'user_id' })
	user: User;

	@OneToMany(() => Advantage, (advantage) => advantage.enterprise)
	advantages: Advantage[];

	@OneToMany(() => Student, (student) => student.institution)
	students: Student[];

	@OneToMany(() => Professor, (professor) => professor.institution)
	professors: Professor[];

	public static isValidCNPJ(cnpj: string): boolean {
		return cnpj.length === 14 && /^\d+$/.test(cnpj);
	}
}
