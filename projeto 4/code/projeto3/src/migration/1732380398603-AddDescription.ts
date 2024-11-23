import { MigrationInterface, QueryRunner } from "typeorm";

export class AddDescription1732380398603 implements MigrationInterface {
    name = 'AddDescription1732380398603'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "history" ADD "description" character varying(450)`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "description"`);
    }

}
