import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany } from 'typeorm';
import { Enterprise } from './Enterprise';
import { History } from './History';

@Entity()
export class Advantage {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 150 })
	name: string;

	@Column('double')
	coins: number;

	@Column({ length: 250 })
	description: string;

	@Column('longblob')
	image: Buffer;

	@ManyToOne(() => Enterprise, (enterprise) => enterprise.advantages)
	enterprise: Enterprise;

	@OneToMany(() => History, (history) => history.advantage)
	histories: History[];
}
