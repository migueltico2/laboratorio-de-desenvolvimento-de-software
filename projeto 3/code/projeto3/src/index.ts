import { AppDataSource } from './data-source';
import { User } from './entity/User';

AppDataSource.initialize()
	.then(async () => {
		console.log('Connected to the database');
	})
	.catch((error) => console.log(error));
