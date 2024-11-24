import { Router } from 'express';
import { ProfessorController } from '../Controller/ProfessorController';

const router = Router();
const professorController = new ProfessorController();

router.post('/', professorController.create.bind(professorController));

export default router;
