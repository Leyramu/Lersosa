DROP DATABASE IF EXISTS `lersosa-config`;

CREATE DATABASE  `lersosa-config` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `lersosa-config`;

/******************************************/
/*   表名称 = config_info   */
/******************************************/
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) DEFAULT NULL COMMENT 'group_id',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text COMMENT '配置的模式',
  `encrypted_data_key` text NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

insert into config_info(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) values
(1, 'application-common.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:18:55', '2022-01-09 15:18:55', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '通用配置基础配置', NULL, NULL, 'yaml', NULL, ''),
(2, 'datasource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:19:07', '2022-01-09 15:19:07', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '数据源配置', NULL, NULL, 'yaml', NULL, ''),
(3, 'lersosa-gateway-api.yml', 'GATEWAY_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:19:43', '2022-01-09 15:22:42', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '网关模块', NULL, NULL, 'yaml', NULL, ''),
(4, 'lersosa-service-auth.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:19:43', '2022-01-09 15:22:29', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '认证中心', NULL, NULL, 'yaml', NULL, ''),
(5, 'lersosa-visual-monitor.yml', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:22:15', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '监控中心', NULL, NULL, 'yaml', NULL, ''),
(6, 'lersosa-service-system.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:22:03', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '系统模块', NULL, NULL, 'yaml', NULL, ''),
(7, 'lersosa-service-gen.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:21:51', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '代码生成', NULL, NULL, 'yaml', NULL, ''),
(8, 'lersosa-service-job.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:18', '2022-01-09 15:21:36', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '定时任务', NULL, NULL, 'yaml', NULL, ''),
(9, 'lersosa-service-resource.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:35', '2022-01-09 15:21:21', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '文件服务', NULL, NULL, 'yaml', NULL, ''),
(10, 'lersosa-service-workflow.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:20:35', '2022-01-09 15:21:21', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '工作流服务', NULL, NULL, 'yaml', NULL, ''),
(11, 'sentinel-lersosa-gateway-api.json', 'GATEWAY_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', '限流策略', NULL, NULL, 'json', NULL, ''),
(12, 'lersosa-visual-seata.properties', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', 'seata配置文件', NULL, NULL, 'properties', NULL, ''),
(13, 'lersosa-visual-sentinel.yml', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', 'sentinel控制台配置文件', NULL, NULL, 'yaml', NULL, ''),
(14, 'lersosa-visual-snailjob.yml', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', '106fdced-d95e-46db-b9f4-0abdce58a18b', 'SJ定时任务控制台', NULL, NULL, 'yaml', NULL, ''),

(101, 'application-common.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '通用配置基础配置', NULL, NULL, 'yaml', NULL, ''),
(102, 'datasource.yml', 'DEFAULT_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '数据源配置', NULL, NULL, 'yaml', NULL, ''),
(103, 'lersosa-gateway-api.yml', 'GATEWAY_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '网关模块', NULL, NULL, 'yaml', NULL, ''),
(104, 'lersosa-service-auth.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '认证中心', NULL, NULL, 'yaml', NULL, ''),
(105, 'lersosa-visual-monitor.yml', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '监控中心', NULL, NULL, 'yaml', NULL, ''),
(106, 'lersosa-service-system.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '系统模块', NULL, NULL, 'yaml', NULL, ''),
(107, 'lersosa-service-gen.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '代码生成', NULL, NULL, 'yaml', NULL, ''),
(108, 'lersosa-service-job.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '定时任务', NULL, NULL, 'yaml', NULL, ''),
(109, 'lersosa-service-resource.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '文件服务', NULL, NULL, 'yaml', NULL, ''),
(110, 'lersosa-service-workflow.yml', 'SERVICE_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '工作流服务', NULL, NULL, 'yaml', NULL, ''),
(111, 'sentinel-lersosa-gateway-api.json', 'GATEWAY_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:23:00', '2022-01-09 15:23:00', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', '限流策略', NULL, NULL, 'json', NULL, ''),
(112, 'lersosa-visual-seata.properties', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', 'seata配置文件', NULL, NULL, 'properties', NULL, ''),
(113, 'lersosa-visual-sentinel.yml', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', 'sentinel控制台配置文件', NULL, NULL, 'yaml', NULL, ''),
(114, 'lersosa-visual-snailjob.yml', 'VISUAL_GROUP', '# 将项目路径：config/下对应文件中内容复制到此处', '2944a25cb97926efcaa43b3ad7a64cf0', '2022-01-09 15:21:02', '2022-01-09 15:21:02', NULL, '0:0:0:0:0:0:0:1', '', '356d484c-399c-4a23-9419-e200e8edbff9', 'SJ定时任务控制台', NULL, NULL, 'yaml', NULL, '');

/******************************************/
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `op_type` char(10) DEFAULT NULL COMMENT 'operation type',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '密钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

insert into tenant_info(id, kp, tenant_id, tenant_name, tenant_desc, create_source, gmt_create, gmt_modified) values
(1, '1', '106fdced-d95e-46db-b9f4-0abdce58a18b', 'lersosa dev', '开发环境', NULL, 1641741261189, 1641741261189),
(2, '1', '356d484c-399c-4a23-9419-e200e8edbff9', 'lersosa prod', '生产环境', NULL, 1641741270448, 1641741287236);

CREATE TABLE `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY COMMENT 'username',
	`password` varchar(500) NOT NULL COMMENT 'password',
	`enabled` boolean NOT NULL COMMENT 'enabled'
);

CREATE TABLE `roles` (
	`username` varchar(50) NOT NULL COMMENT 'username',
	`role` varchar(50) NOT NULL COMMENT 'role',
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL COMMENT 'role',
    `resource` varchar(128) NOT NULL COMMENT 'resource',
    `action` varchar(8) NOT NULL COMMENT 'action',
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$Pq.ht.Lr8U82FeVoEXkb7.koOGPoTEVlfDLpjnXr.NEquuCKnG4me', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
