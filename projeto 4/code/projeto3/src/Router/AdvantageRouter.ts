import { Router } from 'express';
import { AdvantageController } from '../Controller/AdvantageController';

const router = Router();
const advantageController = new AdvantageController();

router.get('/', advantageController.listAllAdvantages.bind(advantageController));
router.post('/', advantageController.create.bind(advantageController));
router.get('/enterprise/:enterpriseId', advantageController.listAdvantagesByEnterprise.bind(advantageController));

export default router;
