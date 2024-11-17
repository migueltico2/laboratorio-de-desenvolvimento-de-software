import { Router } from 'express';
import { AccountController } from '../Controller/AccountController';

const router = Router();
const accountController = new AccountController();

// router.post('/', accountController.create.bind(accountController));
// router.get('/:id', accountController.checkAccount.bind(accountController));
router.post('/buy-advantage/:id', accountController.buyAdvantage.bind(accountController));
export default router;
