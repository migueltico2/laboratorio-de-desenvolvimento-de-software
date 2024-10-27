import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany, JoinColumn } from 'typeorm';
import { User } from './User';
import { Account } from './Account';
import { History } from './History';
import { Enterprise } from './Enterprise';

@Entity()
export class Student {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 11 })
	CPF: string;

	@Column({ length: 9 })
	RG: string;

	@Column({ length: 250 })
	address: string;

	@Column({ length: 150 })
	course: string;

	@ManyToOne(() => User, (user) => user.students)
	@JoinColumn({ name: 'user_id' })
	user: User;

	@ManyToOne(() => Account, (account) => account.students)
	@JoinColumn({ name: 'account_id' })
	account: Account;

	@ManyToOne(() => Enterprise, (enterprise) => enterprise.students)
	@JoinColumn({ name: 'institution_id' })
	institution: Enterprise;

	@OneToMany(() => History, (history) => history.student)
	histories: History[];
}
