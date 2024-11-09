import { AppDataSource } from './data-source';
import express from 'express';
import userRoutes from './Router/UserRouter';
import accountRoutes from './Router/AccountRouter';
import enterpriseRoutes from './Router/EnterpriseRouter';
import studentRoutes from './Router/StudentRouter';
import cors from 'cors';

const app = express();

AppDataSource.initialize()
	.then(async () => {
		app.use(express.json());
		app.use(cors('*'));
		app.use('/users', userRoutes);
		app.use('/accounts', accountRoutes);
		app.use('/enterprise', enterpriseRoutes);
		app.use('/student', studentRoutes);
		app.listen(3000, () => {
			console.log('Server is running on port 3000');
		});
		console.log('Connected to the database');
	})
	.catch((error) => console.log(error));
