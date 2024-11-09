import { Router } from 'express';
import { StudentController } from '../Controller/StudentController';

const router = Router();
const studentController = new StudentController();

router.get('/', studentController.getAll.bind(studentController));
router.get('/:id', studentController.getById.bind(studentController));
router.post('/', studentController.create.bind(studentController));
router.put('/:id', studentController.update.bind(studentController));
router.delete('/:id', studentController.delete.bind(studentController));

export default router;
