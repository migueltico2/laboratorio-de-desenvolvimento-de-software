import { Router } from 'express';
// import { UserController } from '../Controller/UserController';
import { UserController } from '../Controller/UserController';

const router = Router();
const userController = new UserController();

router.get('/', userController.getAll.bind(userController));
router.post('/', userController.create.bind(userController));
router.delete('/:id', userController.delete.bind(userController));
router.put('/:id', userController.update.bind(userController));
router.post('/login', userController.login.bind(userController));

export default router;
