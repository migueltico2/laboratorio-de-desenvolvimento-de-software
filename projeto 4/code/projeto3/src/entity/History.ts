import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { Advantage } from './Advantage';
import { Student } from './Student';
import { Professor } from './Professor';

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

	@ManyToOne(() => Student, (student) => student.histories)
	@JoinColumn({ name: 'student_id' })
	student: Student;

	@ManyToOne(() => Professor, (professor) => professor.histories)
	@JoinColumn({ name: 'professor_id' })
	professor: Professor;
}
