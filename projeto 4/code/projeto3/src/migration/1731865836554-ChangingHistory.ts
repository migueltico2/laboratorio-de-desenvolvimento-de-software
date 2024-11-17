import { MigrationInterface, QueryRunner } from "typeorm";

export class ChangingHistory1731865836554 implements MigrationInterface {
    name = 'ChangingHistory1731865836554'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_47e1f83a0c652a252efa79ce98f"`);
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_a672102bff6d2d1b4e35d334f58"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "student_id"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "professor_id"`);
        await queryRunner.query(`ALTER TABLE "history" ADD "account_id" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_454599792618a83e4a42e8e210e" FOREIGN KEY ("account_id") REFERENCES "account"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_454599792618a83e4a42e8e210e"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "account_id"`);
        await queryRunner.query(`ALTER TABLE "history" ADD "professor_id" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD "student_id" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_a672102bff6d2d1b4e35d334f58" FOREIGN KEY ("professor_id") REFERENCES "professor"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_47e1f83a0c652a252efa79ce98f" FOREIGN KEY ("student_id") REFERENCES "student"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

}
