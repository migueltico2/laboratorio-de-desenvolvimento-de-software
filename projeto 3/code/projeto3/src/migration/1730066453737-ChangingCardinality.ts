import { MigrationInterface, QueryRunner } from "typeorm";

export class ChangingCardinality1730066453737 implements MigrationInterface {
    name = 'ChangingCardinality1730066453737'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "professor" DROP CONSTRAINT "FK_cfed83451062b93f81929b406ba"`);
        await queryRunner.query(`ALTER TABLE "professor" ADD CONSTRAINT "UQ_cfed83451062b93f81929b406ba" UNIQUE ("user_id")`);
        await queryRunner.query(`ALTER TABLE "student" DROP CONSTRAINT "FK_0cc43638ebcf41dfab27e62dc09"`);
        await queryRunner.query(`ALTER TABLE "student" ADD CONSTRAINT "UQ_0cc43638ebcf41dfab27e62dc09" UNIQUE ("user_id")`);
        await queryRunner.query(`ALTER TABLE "enterprise" DROP CONSTRAINT "FK_3bd39b4d525d5a48491e726082f"`);
        await queryRunner.query(`ALTER TABLE "enterprise" ADD CONSTRAINT "UQ_3bd39b4d525d5a48491e726082f" UNIQUE ("user_id")`);
        await queryRunner.query(`ALTER TABLE "professor" ADD CONSTRAINT "FK_cfed83451062b93f81929b406ba" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "student" ADD CONSTRAINT "FK_0cc43638ebcf41dfab27e62dc09" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "enterprise" ADD CONSTRAINT "FK_3bd39b4d525d5a48491e726082f" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "enterprise" DROP CONSTRAINT "FK_3bd39b4d525d5a48491e726082f"`);
        await queryRunner.query(`ALTER TABLE "student" DROP CONSTRAINT "FK_0cc43638ebcf41dfab27e62dc09"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP CONSTRAINT "FK_cfed83451062b93f81929b406ba"`);
        await queryRunner.query(`ALTER TABLE "enterprise" DROP CONSTRAINT "UQ_3bd39b4d525d5a48491e726082f"`);
        await queryRunner.query(`ALTER TABLE "enterprise" ADD CONSTRAINT "FK_3bd39b4d525d5a48491e726082f" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "student" DROP CONSTRAINT "UQ_0cc43638ebcf41dfab27e62dc09"`);
        await queryRunner.query(`ALTER TABLE "student" ADD CONSTRAINT "FK_0cc43638ebcf41dfab27e62dc09" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "professor" DROP CONSTRAINT "UQ_cfed83451062b93f81929b406ba"`);
        await queryRunner.query(`ALTER TABLE "professor" ADD CONSTRAINT "FK_cfed83451062b93f81929b406ba" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

}
