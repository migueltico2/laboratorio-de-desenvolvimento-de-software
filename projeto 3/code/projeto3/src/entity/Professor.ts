import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany, JoinColumn } from 'typeorm';
import { User } from './User';
import { Account } from './Account';
import { History } from './History';
import { Enterprise } from './Enterprise';

@Entity()
export class Professor {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 11 })
	CPF: string;

	@Column({ length: 150 })
	department: string;

	@ManyToOne(() => User, (user) => user.professors)
	user: User;

	@ManyToOne(() => Account, (account) => account.professors)
	account: Account;

	@ManyToOne(() => Enterprise, (enterprise) => enterprise.professors)
	@JoinColumn({ name: 'institution_id' })
	institution: Enterprise;

	@OneToMany(() => History, (history) => history.professor)
	histories: History[];
}
