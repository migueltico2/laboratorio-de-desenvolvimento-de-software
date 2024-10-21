import { Request, Response } from 'express';
import { AppDataSource } from '../data-source';
import { User } from '../entity/User';

export class UserController {
    private userRepository = AppDataSource.getRepository(User);

    // GET - Fetch all users
    async getAll(request: Request, response: Response) {
        const users = await this.userRepository.find();
        return response.json(users);
    }

    // POST - Create a new user
    async create(request: Request, response: Response) {
        const newUser = this.userRepository.create(request.body);
        const results = await this.userRepository.save(newUser);
        return response.json(results);
    }

    // DELETE - Remove a user
    async delete(request: Request, response: Response) {
        const id = request.params.id;
        const result = await this.userRepository.delete(id);
        return response.json(result);
    }

    // PUT - Update a user
    async update(request: Request, response: Response) {
        const id = parseInt(request.params.id);
        const user = await this.userRepository.findOne({ where: { id } });
        if (user) {
            this.userRepository.merge(user, request.body);
            const results = await this.userRepository.save(user);
            return response.json(results);
        }
        return response.status(404).json({ message: 'User not found' });
    }
}
