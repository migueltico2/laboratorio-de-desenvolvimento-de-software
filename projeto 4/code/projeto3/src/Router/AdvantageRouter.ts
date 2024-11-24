import { Router } from 'express';
import { AdvantageController } from '../Controller/AdvantageController';
import multer from 'multer';

const router = Router();
const advantageController = new AdvantageController();

// Configuração do multer para processar o upload de imagem
const upload = multer({
	storage: multer.memoryStorage(),
	limits: {
		fileSize: 5 * 1024 * 1024, // limite de 5MB
	},
});

router.get('/', advantageController.listAllAdvantages.bind(advantageController));
router.post('/', upload.single('image'), advantageController.create.bind(advantageController));
router.get('/list/:enterpriseId', advantageController.listAdvantagesByEnterprise.bind(advantageController));
router.get('/list/student/:institutionId', advantageController.listAllAdvantagesForStudent.bind(advantageController));

export default router;
