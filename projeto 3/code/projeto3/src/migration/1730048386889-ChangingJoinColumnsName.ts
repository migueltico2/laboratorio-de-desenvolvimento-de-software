import { MigrationInterface, QueryRunner } from "typeorm";

export class ChangingJoinColumnsName1730048386889 implements MigrationInterface {
    name = 'ChangingJoinColumnsName1730048386889'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "professor" DROP CONSTRAINT "FK_5c30960d3b0cd311554ae724f72"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP CONSTRAINT "FK_6c3d450eb19696a31841fa4bde9"`);
        await queryRunner.query(`ALTER TABLE "student" DROP CONSTRAINT "FK_b35463776b4a11a3df3c30d920a"`);
        await queryRunner.query(`ALTER TABLE "student" DROP CONSTRAINT "FK_627399e684b01d45317af431237"`);
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_bfed13df1f6841af3d47f96078c"`);
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_835e6f255ba22679b2c476bf31d"`);
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_2768868122f2b0b260ea4576e26"`);
        await queryRunner.query(`ALTER TABLE "advantage" DROP CONSTRAINT "FK_16582479285811d69485f12d740"`);
        await queryRunner.query(`ALTER TABLE "enterprise" DROP CONSTRAINT "FK_7b204d630af12dda2cbf1253c49"`);
        await queryRunner.query(`ALTER TABLE "advantage" RENAME COLUMN "enterpriseId" TO "enterprise_id"`);
        await queryRunner.query(`ALTER TABLE "enterprise" RENAME COLUMN "userId" TO "user_id"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP COLUMN "userId"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP COLUMN "accountId"`);
        await queryRunner.query(`ALTER TABLE "student" DROP COLUMN "userId"`);
        await queryRunner.query(`ALTER TABLE "student" DROP COLUMN "accountId"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "advantageId"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "studentId"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "professorId"`);
        await queryRunner.query(`ALTER TABLE "professor" ADD "user_id" integer`);
        await queryRunner.query(`ALTER TABLE "professor" ADD "account_id" integer`);
        await queryRunner.query(`ALTER TABLE "student" ADD "user_id" integer`);
        await queryRunner.query(`ALTER TABLE "student" ADD "account_id" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD "advantage_id" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD "student_id" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD "professor_id" integer`);
        await queryRunner.query(`ALTER TABLE "professor" ADD CONSTRAINT "FK_cfed83451062b93f81929b406ba" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "professor" ADD CONSTRAINT "FK_78e56aa16a1ff4d60230524574d" FOREIGN KEY ("account_id") REFERENCES "account"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "student" ADD CONSTRAINT "FK_0cc43638ebcf41dfab27e62dc09" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "student" ADD CONSTRAINT "FK_5a09cb10bb7fedd4ccae2261b6c" FOREIGN KEY ("account_id") REFERENCES "account"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_2ef93c4cbad16505cb9f56bfce1" FOREIGN KEY ("advantage_id") REFERENCES "advantage"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_47e1f83a0c652a252efa79ce98f" FOREIGN KEY ("student_id") REFERENCES "student"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_a672102bff6d2d1b4e35d334f58" FOREIGN KEY ("professor_id") REFERENCES "professor"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "advantage" ADD CONSTRAINT "FK_0f9ce60863d63f89d5be516eeb4" FOREIGN KEY ("enterprise_id") REFERENCES "enterprise"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "enterprise" ADD CONSTRAINT "FK_3bd39b4d525d5a48491e726082f" FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "enterprise" DROP CONSTRAINT "FK_3bd39b4d525d5a48491e726082f"`);
        await queryRunner.query(`ALTER TABLE "advantage" DROP CONSTRAINT "FK_0f9ce60863d63f89d5be516eeb4"`);
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_a672102bff6d2d1b4e35d334f58"`);
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_47e1f83a0c652a252efa79ce98f"`);
        await queryRunner.query(`ALTER TABLE "history" DROP CONSTRAINT "FK_2ef93c4cbad16505cb9f56bfce1"`);
        await queryRunner.query(`ALTER TABLE "student" DROP CONSTRAINT "FK_5a09cb10bb7fedd4ccae2261b6c"`);
        await queryRunner.query(`ALTER TABLE "student" DROP CONSTRAINT "FK_0cc43638ebcf41dfab27e62dc09"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP CONSTRAINT "FK_78e56aa16a1ff4d60230524574d"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP CONSTRAINT "FK_cfed83451062b93f81929b406ba"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "professor_id"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "student_id"`);
        await queryRunner.query(`ALTER TABLE "history" DROP COLUMN "advantage_id"`);
        await queryRunner.query(`ALTER TABLE "student" DROP COLUMN "account_id"`);
        await queryRunner.query(`ALTER TABLE "student" DROP COLUMN "user_id"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP COLUMN "account_id"`);
        await queryRunner.query(`ALTER TABLE "professor" DROP COLUMN "user_id"`);
        await queryRunner.query(`ALTER TABLE "history" ADD "professorId" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD "studentId" integer`);
        await queryRunner.query(`ALTER TABLE "history" ADD "advantageId" integer`);
        await queryRunner.query(`ALTER TABLE "student" ADD "accountId" integer`);
        await queryRunner.query(`ALTER TABLE "student" ADD "userId" integer`);
        await queryRunner.query(`ALTER TABLE "professor" ADD "accountId" integer`);
        await queryRunner.query(`ALTER TABLE "professor" ADD "userId" integer`);
        await queryRunner.query(`ALTER TABLE "enterprise" RENAME COLUMN "user_id" TO "userId"`);
        await queryRunner.query(`ALTER TABLE "advantage" RENAME COLUMN "enterprise_id" TO "enterpriseId"`);
        await queryRunner.query(`ALTER TABLE "enterprise" ADD CONSTRAINT "FK_7b204d630af12dda2cbf1253c49" FOREIGN KEY ("userId") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "advantage" ADD CONSTRAINT "FK_16582479285811d69485f12d740" FOREIGN KEY ("enterpriseId") REFERENCES "enterprise"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_2768868122f2b0b260ea4576e26" FOREIGN KEY ("professorId") REFERENCES "professor"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_835e6f255ba22679b2c476bf31d" FOREIGN KEY ("studentId") REFERENCES "student"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "history" ADD CONSTRAINT "FK_bfed13df1f6841af3d47f96078c" FOREIGN KEY ("advantageId") REFERENCES "advantage"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "student" ADD CONSTRAINT "FK_627399e684b01d45317af431237" FOREIGN KEY ("accountId") REFERENCES "account"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "student" ADD CONSTRAINT "FK_b35463776b4a11a3df3c30d920a" FOREIGN KEY ("userId") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "professor" ADD CONSTRAINT "FK_6c3d450eb19696a31841fa4bde9" FOREIGN KEY ("accountId") REFERENCES "account"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
        await queryRunner.query(`ALTER TABLE "professor" ADD CONSTRAINT "FK_5c30960d3b0cd311554ae724f72" FOREIGN KEY ("userId") REFERENCES "user"("id") ON DELETE NO ACTION ON UPDATE NO ACTION`);
    }

}
