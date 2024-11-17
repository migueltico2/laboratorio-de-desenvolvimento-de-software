import { Entity, PrimaryGeneratedColumn, Column, OneToOne, OneToMany, JoinColumn, ManyToOne } from 'typeorm';
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

	@OneToOne(() => User, (user) => user.professors)
	@JoinColumn({ name: 'user_id' })
	user: User;

	@ManyToOne(() => Account, (account) => account.professors, { cascade: true })
	@JoinColumn({ name: 'account_id' })
	account: Account;

	@ManyToOne(() => Enterprise, (enterprise) => enterprise.professors)
	@JoinColumn({ name: 'institution_id' })
	institution: Enterprise;
}
