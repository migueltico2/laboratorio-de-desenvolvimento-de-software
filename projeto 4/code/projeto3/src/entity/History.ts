import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { Advantage } from './Advantage';
import { Account } from './Account';

@Entity()
export class History {
	@PrimaryGeneratedColumn()
	id: number;

	@Column('decimal', { precision: 10, scale: 2 })
	coins: number;

	@Column({ length: 250 })
	type: string;

	@Column('timestamp')
	date: Date;

	@ManyToOne(() => Advantage, (advantage) => advantage.histories)
	@JoinColumn({ name: 'advantage_id' })
	advantage: Advantage;

	@ManyToOne(() => Account, (account) => account.histories)
	@JoinColumn({ name: 'account_id' })
	account: Account;
}
