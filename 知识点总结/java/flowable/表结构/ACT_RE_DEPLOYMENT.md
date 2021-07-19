# ACT_RE_DEPLOYMENT

流程模型发布记录表，核心表



## 表字段说明

| **字段**     | **字段名称** | **字段默认值** | **是否允许为空** | **数据类型** | **字段长度** | **键**  | **备注** |
| ------------ | ------------ | -------------- | ---------------- | ------------ | ------------ | ------- | -------- |
| ID_          | 主键         |                | NO               | varchar      | 64           | PRIMARY |          |
| NAME_        | 名称         | NULL           | YES              | varchar      | 255          |         |          |
| CATEGORY_    | 分类         | NULL           | YES              | varchar      | 255          |         |          |
| TENANT_ID_   | 租户ID       |                | YES              | varchar      | 255          |         |          |
| DEPLOY_TIME_ | 部署时间     | NULL           | YES              | timestamp    | NULL         |         |          |



## 建表语句

```
CREATE TABLE `ACT_RE_DEPLOYMENT` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `DEPLOY_TIME_` timestamp(3) NULL DEFAULT NULL,
  `DERIVED_FROM_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DERIVED_FROM_ROOT_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_DEPLOYMENT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ENGINE_VERSION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```