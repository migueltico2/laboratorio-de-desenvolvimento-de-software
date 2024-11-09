import { Router } from 'express';
import { ProfessorController } from '../Controller/ProfessorController';

const router = Router();
const professorController = new ProfessorController();

router.get('/', professorController.getAll.bind(professorController));
router.post('/', professorController.create.bind(professorController));
router.delete('/:id', professorController.delete.bind(professorController));
router.put('/:id', professorController.update.bind(professorController));

export default router;
