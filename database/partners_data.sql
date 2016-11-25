/*
 Navicat Premium Data Transfer

 Source Server         : localPostgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 90304
 Source Host           : localhost
 Source Database       : micro
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 90304
 File Encoding         : utf-8

 Date: 04/28/2014 18:23:19 PM
*/

-- ----------------------------
--  Table structure for partners
-- ----------------------------
DROP TABLE IF EXISTS "public"."partners";
CREATE TABLE "public"."partners" (
	"id" int4 NOT NULL DEFAULT nextval('partners_id_seq'::regclass),
	"name" varchar(250) COLLATE "default",
	"urltest" varchar(250) COLLATE "default",
	"urlwork" varchar(250) COLLATE "default",
	"loginwork" varchar(50) COLLATE "default",
	"passwordwork" varchar(50) COLLATE "default",
	"logintest" varchar(50) COLLATE "default",
	"passwordtest" varchar(50) COLLATE "default",
	"codework" varchar(50) COLLATE "default",
	"codetest" varchar(50) COLLATE "default",
	"applicationid" varchar(50) COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."partners" OWNER TO "postgres";

-- ----------------------------
--  Records of partners
-- ----------------------------
BEGIN;
INSERT INTO "public"."partners" VALUES ('3', 'ОКБ', null, null, null, null, null, null, null, null, null);
INSERT INTO "public"."partners" VALUES ('4', 'Equifax', null, null, null, null, null, null, null, null, null);
INSERT INTO "public"."partners" VALUES ('5', 'НБКИ', null, null, null, null, null, null, null, null, null);
INSERT INTO "public"."partners" VALUES ('6', 'Клиент', null, null, null, null, null, null, null, null, null);
INSERT INTO "public"."partners" VALUES ('2', 'Cronos', null, null, null, null, null, null, null, null, null);
INSERT INTO "public"."partners" VALUES ('1', 'Система', null, null, null, null, null, null, null, null, null);
INSERT INTO "public"."partners" VALUES ('11', 'Twitter', null, null, null, 'fMQuP0164fR43Psq1qkhTwU2LahaWEC9z5xrVQ8N0dba3ErvK1', null, null, null, null, '1462700770632787');
INSERT INTO "public"."partners" VALUES ('7', 'Вконтакте', null, null, null, null, null, null, '6Xh7btLWtEJUC7fBlA5n', null, '4331470');
INSERT INTO "public"."partners" VALUES ('10', 'Facebook', null, null, null, '71027b0e137c3fa0ced5e1c19517f981', null, null, '', null, '1462700770632787');
INSERT INTO "public"."partners" VALUES ('9', 'Мой мир', null, null, null, '329e9a67efa062cc9fbeb5571c29160a', null, null, '5707670775245760d98e2f4881034471', null, '720245');
INSERT INTO "public"."partners" VALUES ('8', 'Одноклассники', null, null, null, '60AE46A5D2B87241603FB6DD', null, null, 'CBAQGCOBEBABABABA', null, '1088515840');
COMMIT;

-- ----------------------------
--  Primary key structure for table partners
-- ----------------------------
ALTER TABLE "public"."partners" ADD PRIMARY KEY ("id") NOT DEFERRABLE INITIALLY IMMEDIATE;

-- ----------------------------
--  Indexes structure for table partners
-- ----------------------------
CREATE UNIQUE INDEX  "partners_pk" ON "public"."partners" USING btree("id" ASC NULLS LAST);

-- ----------------------------
--  Triggers structure for table partners
-- ----------------------------
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_27990" AFTER UPDATE ON "public"."partners" FROM "public"."address" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_27990" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_27989" AFTER DELETE ON "public"."partners" FROM "public"."address" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_cascade_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_27989" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28210" AFTER UPDATE ON "public"."partners" FROM "public"."mismatches" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28210" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28209" AFTER DELETE ON "public"."partners" FROM "public"."mismatches" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28209" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28240" AFTER UPDATE ON "public"."partners" FROM "public"."peoplecontact" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28240" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28239" AFTER DELETE ON "public"."partners" FROM "public"."peoplecontact" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28239" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28265" AFTER UPDATE ON "public"."partners" FROM "public"."peoplepersonal" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28265" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28264" AFTER DELETE ON "public"."partners" FROM "public"."peoplepersonal" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28264" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_27965" AFTER UPDATE ON "public"."partners" FROM "public"."document" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_27965" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_27964" AFTER DELETE ON "public"."partners" FROM "public"."document" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_27964" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28250" AFTER UPDATE ON "public"."partners" FROM "public"."peoplemisc" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28250" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28249" AFTER DELETE ON "public"."partners" FROM "public"."peoplemisc" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28249" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28300" AFTER UPDATE ON "public"."partners" FROM "public"."ref_header" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28300" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28299" AFTER DELETE ON "public"."partners" FROM "public"."ref_header" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28299" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28075" AFTER UPDATE ON "public"."partners" FROM "public"."credit" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28075" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28074" AFTER DELETE ON "public"."partners" FROM "public"."credit" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28074" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28325" AFTER UPDATE ON "public"."partners" FROM "public"."requests" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28325" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28324" AFTER DELETE ON "public"."partners" FROM "public"."requests" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_cascade_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28324" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28350" AFTER UPDATE ON "public"."partners" FROM "public"."score_model" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28350" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28349" AFTER DELETE ON "public"."partners" FROM "public"."score_model" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28349" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28370" AFTER UPDATE ON "public"."partners" FROM "public"."scoring" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28370" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28369" AFTER DELETE ON "public"."partners" FROM "public"."scoring" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28369" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28165" AFTER UPDATE ON "public"."partners" FROM "public"."employment" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_upd"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28165" ON "public"."partners" IS NULL;
CREATE CONSTRAINT TRIGGER "RI_ConstraintTrigger_a_28164" AFTER DELETE ON "public"."partners" FROM "public"."employment" NOT DEFERRABLE INITIALLY IMMEDIATE FOR EACH ROW EXECUTE PROCEDURE "RI_FKey_restrict_del"();
COMMENT ON TRIGGER "RI_ConstraintTrigger_a_28164" ON "public"."partners" IS NULL;

