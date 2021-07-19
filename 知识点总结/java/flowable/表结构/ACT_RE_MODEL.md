# ACT_RE_MODEL

流程模型数据表，核心表



## 表字段说明

| 字段                          | 字段名称     | 字段默认值 | 是否允许为空 | 数据类型  | 字段长度 | 键                      | 备注                                          |
| :---------------------------- | ------------ | ---------- | ------------ | --------- | -------- | ----------------------- | --------------------------------------------- |
| ID_                           | 主键         | NULL       | NO           | varchar   | 64       | PRIMARY                 |                                               |
| REV_                          | 版本号       | NULL       | YES          | int       | NULL     |                         | flowable自己生成的版本号                      |
| NAME_                         | 名称         | NULL       | YES          | varchar   | 255      |                         | 流程名称                                      |
| KEY_                          | 标识         | NULL       | YES          | varchar   | 255      |                         | 流程key，flowable底层API中很多操作以该key为准 |
| CATEGORY_                     | 分类         | NULL       | YES          | varchar   | 255      |                         |                                               |
| CREATE_TIME_                  | 创建时间     | NULL       | YES          | timestamp | NULL     |                         |                                               |
| LAST_UPDATE_TIME_             | 最后更新时间 | NULL       | YES          | timestamp | NULL     |                         |                                               |
| VERSION_                      | 版本         | NULL       | YES          | int       | NULL     |                         | 用户自填版本号                                |
| META_INFO_                    | 元数据       | NULL       | YES          | varchar   | 4000     |                         | 以json格式保存流程定义的信息                  |
| DEPLOYMENT_ID_                | 部署ID       | NULL       | YES          | varchar   | 64       | MUL(ACT_RE_DEPLOYMENT） | 部署记录的id                                  |
| EDITOR_SOURCE_VALUE_ID_       | 二进制文件ID | NULL       | YES          | varchar   | 64       | MUL（ACT_GE_BYTEARRAY） | 设计器原始信息                                |
| EDITOR_SOURCE_EXTRA_VALUE_ID_ | 二进制文件ID | NULL       | YES          | varchar   | 64       | MUL（ACT_GE_BYTEARRAY） | 设计器扩展信息                                |
| TENANT_ID_                    | 租户ID       |            | YES          | varchar   | 255      |                         | 多租户功能，租户id                            |



## 建表语句

```
CREATE TABLE `ACT_RE_MODEL` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LAST_UPDATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `VERSION_` int(11) DEFAULT NULL,
  `META_INFO_` longtext COLLATE utf8_bin,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_MODEL_SOURCE` (`EDITOR_SOURCE_VALUE_ID_`) USING BTREE,
  KEY `ACT_FK_MODEL_SOURCE_EXTRA` (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) USING BTREE,
  KEY `ACT_FK_MODEL_DEPLOYMENT` (`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```



