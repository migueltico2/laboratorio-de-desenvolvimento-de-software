import { Router } from 'express';
import { UserController } from '../Controller/UserController';

const router = Router();
const userController = new UserController();

router.get('/users', userController.getAll.bind(userController));
router.post('/users', userController.create.bind(userController));
router.delete('/users/:id', userController.delete.bind(userController));
router.put('/users/:id', userController.update.bind(userController));

export default router;
