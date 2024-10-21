import { AppDataSource } from './data-source';
import express from 'express';
import userRoutes from './Router/UserRouter';


const app = express();


AppDataSource.initialize()
.then(async () => {
	app.use(express.json());
		app.use('/api', userRoutes);
		app.listen(3000, () => {
			console.log('Server is running on port 3000');
		});
		console.log('Connected to the database');
	})
	.catch((error) => console.log(error));
