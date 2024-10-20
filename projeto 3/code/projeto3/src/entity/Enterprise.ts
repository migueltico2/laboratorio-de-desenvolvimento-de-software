import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, OneToMany } from 'typeorm';
import { User } from './User';
import { Advantage } from './Advantage';

@Entity()
export class Enterprise {
	@PrimaryGeneratedColumn()
	id: number;

	@Column({ length: 14 })
	CNPJ: string;

	@Column({ length: 11 })
	type: string;

	@ManyToOne(() => User, (user) => user.enterprises)
	user: User;

	@OneToMany(() => Advantage, (advantage) => advantage.enterprise)
	advantages: Advantage[];
}
