import { Router } from 'express';
import { ProfessorController } from '../Controller/ProfessorController';

const router = Router();
const professorController = new ProfessorController();

router.post('/send-coins/:id', professorController.sendCoinsToStudent.bind(professorController));
router.get('/history/:id', professorController.getHistory.bind(professorController));
router.post('/', professorController.create.bind(professorController));

export default router;
