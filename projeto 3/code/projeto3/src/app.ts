import express from 'express';
import userRoutes from './Router/UserRouter';

const app = express();

app.use(express.json());
app.use('/api', userRoutes);

app.listen(3000, () => {
    console.log('Server is running on port 3000');
});
