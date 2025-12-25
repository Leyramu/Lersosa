-- Create "stellar_status" table
CREATE TABLE "public"."stellar_status" (
  "status_id" uuid NOT NULL,
  "tenant_id" uuid NOT NULL,
  "create_by" bigint NULL,
  "create_time" timestamptz NULL,
  "update_by" bigint NULL,
  "update_time" timestamptz NULL,
  "remark" character varying NULL,
  "version" integer NOT NULL DEFAULT 0,
  "info_id" uuid NOT NULL,
  "score" double precision NOT NULL DEFAULT 0,
  "flag" integer NOT NULL DEFAULT 0,
  "check" integer NOT NULL DEFAULT 0,
  PRIMARY KEY ("status_id")
);
-- Create index "status_flag_check_score" to table: "stellar_status"
CREATE INDEX "status_flag_check_score" ON "public"."stellar_status" ("flag", "check", "score");
-- Create index "stellar_status_info_id_key" to table: "stellar_status"
CREATE UNIQUE INDEX "stellar_status_info_id_key" ON "public"."stellar_status" ("info_id");
-- Set comment to table: "stellar_status"
COMMENT ON TABLE "public"."stellar_status" IS '星体状态表';
-- Set comment to column: "status_id" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."status_id" IS '天体状态 ID';
-- Set comment to column: "tenant_id" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."tenant_id" IS '租户编号';
-- Set comment to column: "create_by" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."create_by" IS '创建者';
-- Set comment to column: "create_time" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."create_time" IS '创建时间';
-- Set comment to column: "update_by" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."update_by" IS '更新者';
-- Set comment to column: "update_time" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."update_time" IS '更新时间';
-- Set comment to column: "remark" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."remark" IS '备注';
-- Set comment to column: "version" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."version" IS '版本';
-- Set comment to column: "info_id" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."info_id" IS '天体信息 ID';
-- Set comment to column: "score" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."score" IS '得分';
-- Set comment to column: "flag" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."flag" IS '筛选标记';
-- Set comment to column: "check" on table: "stellar_status"
COMMENT ON COLUMN "public"."stellar_status"."check" IS '审核标记';
