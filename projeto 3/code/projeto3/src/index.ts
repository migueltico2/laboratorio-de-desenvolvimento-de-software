import { AppDataSource } from './data-source';
import express from 'express';
import userRoutes from './Router/UserRouter';
import accountRoutes from './Router/AccountRouter';
import enterpriseRoutes from './Router/EnterpriseRouter';
const app = express();

AppDataSource.initialize()
	.then(async () => {
		app.use(express.json());
		app.use('/users', userRoutes);
		app.use('/accounts', accountRoutes);
		app.use('/enterprises', enterpriseRoutes);
		app.listen(3000, () => {
			console.log('Server is running on port 3000');
		});
		console.log('Connected to the database');
	})
	.catch((error) => console.log(error));
