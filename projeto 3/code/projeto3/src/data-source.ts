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
	host: 'ep-odd-art-a4izqlq9-pooler.us-east-1.aws.neon.tech',
	port: 5432,
	username: 'default',
	password: 'G9epwOMShLR7',
	database: 'verceldb',
	synchronize: true,
	logging: false,
	ssl: true,
	entities: [User, Account, Professor, Student, Enterprise, Advantage, History],
	migrations: ['./src/migration/*{.ts,.js}'],
	subscribers: [],
});
