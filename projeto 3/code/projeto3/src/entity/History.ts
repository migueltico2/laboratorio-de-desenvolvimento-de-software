import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from 'typeorm';
import { Advantage } from './Advantage';
import { Student } from './Student';
import { Professor } from './Professor';

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

	@ManyToOne(() => Professor, (professor) => professor.histories)
	professor: Professor;
}
