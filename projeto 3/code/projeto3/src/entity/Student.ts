import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany } from 'typeorm';
import { User } from './User';
import { Account } from './Account';
import { History } from './History';

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
	user: User;

	@ManyToOne(() => Account, (account) => account.students)
	account: Account;

	@Column()
	institution_id: number;

	@OneToMany(() => History, (history) => history.student)
	histories: History[];
}
