import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from 'typeorm';
import { Advantage } from './Advantage';
import { Student } from './Student';

@Entity()
export class History {
	@PrimaryGeneratedColumn()
	id: number;

	@Column('double')
	coins: number;

	@Column({ length: 8 })
	type: string;

	@Column('datetime')
	date: Date;

	@ManyToOne(() => Advantage, (advantage) => advantage.histories)
	advantage: Advantage;

	@ManyToOne(() => Student, (student) => student.histories)
	student: Student;

	@Column()
	professor_id: number;
}
