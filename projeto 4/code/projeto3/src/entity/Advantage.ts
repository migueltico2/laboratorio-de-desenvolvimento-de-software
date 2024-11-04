import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany, JoinColumn } from 'typeorm';
import { Enterprise } from './Enterprise';
import { History } from './History';

@Entity()
export class Advantage {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 150 })
	name: string;

	@Column('decimal', { precision: 10, scale: 2 })
	coins: number;

	@Column({ length: 250 })
	description: string;

	@Column('bytea')
	image: Buffer;

	@ManyToOne(() => Enterprise, (enterprise) => enterprise.advantages)
	@JoinColumn({ name: 'enterprise_id' })
	enterprise: Enterprise;

	@OneToMany(() => History, (history) => history.advantage)
	histories: History[];
}
