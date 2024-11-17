import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany, JoinColumn, OneToOne } from 'typeorm';
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

	@OneToOne(() => User, (user) => user.students)
	@JoinColumn({ name: 'user_id' })
	user: User;

	@ManyToOne(() => Account, (account) => account.students, { cascade: true })
	@JoinColumn({ name: 'account_id' })
	account: Account;

	@OneToMany(() => History, (history) => history.student)
	histories: History[];

	@ManyToOne(() => Enterprise, (enterprise) => enterprise.students)
	@JoinColumn({ name: 'institution_id' })
	institution: Enterprise;
}
