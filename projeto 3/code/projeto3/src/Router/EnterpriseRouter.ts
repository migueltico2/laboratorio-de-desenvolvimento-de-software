import { Router } from 'express';
import { EnterpriseController } from '../Controller/EnterpriseController';

const router = Router();
const enterpriseController = new EnterpriseController();

router.get('/', enterpriseController.getAll.bind(enterpriseController));
router.get('/:id', enterpriseController.getById.bind(enterpriseController));
router.post('/', enterpriseController.create.bind(enterpriseController));
router.put('/:id', enterpriseController.update.bind(enterpriseController));
router.delete('/:id', enterpriseController.delete.bind(enterpriseController));

export default router;
