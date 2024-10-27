import 'reflect-metadata';
import { DataSource } from 'typeorm';
import { User } from './entity/User';
import { Account } from './entity/Account';
import { Professor } from './entity/Professor';
import { Student } from './entity/Student';
import { Enterprise } from './entity/Enterprise';
import { Advantage } from './entity/Advantage';
import { History } from './entity/History';

export const AppDataSource = new DataSource({
	type: 'postgres',
	host: 'localhost',
	port: 5432,
	username: 'postgres',
	password: 'PASSWORD',
	database: 'lab3',
	synchronize: true,
	logging: false,
	entities: [User, Account, Professor, Student, Enterprise, Advantage, History],
	migrations: ['./src/migration/*{.ts,.js}'],
	subscribers: [],
});
