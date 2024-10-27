import { MigrationInterface, QueryRunner } from "typeorm";

export class InsertDefaultCoins1730049254116 implements MigrationInterface {
    name = 'InsertDefaultCoins1730049254116'

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "account" ALTER COLUMN "coins" SET DEFAULT '0'`);
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(`ALTER TABLE "account" ALTER COLUMN "coins" DROP DEFAULT`);
    }

}
