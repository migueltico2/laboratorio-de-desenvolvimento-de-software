import { Entity, PrimaryGeneratedColumn, Column, OneToOne, OneToMany, JoinColumn } from 'typeorm';
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

	@OneToOne(() => User, (user) => user.enterprises)
	@JoinColumn({ name: 'user_id' })
	user: User;

	@OneToMany(() => Advantage, (advantage) => advantage.enterprise, { cascade: true })
	advantages: Advantage[];

	@OneToMany(() => Student, (student) => student.institution, { cascade: true })
	students: Student[];

	@OneToMany(() => Professor, (professor) => professor.institution, { cascade: true })
	professors: Professor[];

	public static isValidCNPJ(cnpj: string): boolean {
		return cnpj.length === 14 && /^\d+$/.test(cnpj);
	}
}
