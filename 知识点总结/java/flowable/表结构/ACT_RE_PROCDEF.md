# ACT_RE_PROCDEF

流程定义表，和流程模型发布记录表（`ACT_RE_DEPLOYMENT`）一一对应。流程模型发布后，生成一个流程定义。核心表。



## 表字段说明

| **字段**                | **字段名称**     | **字段默认值** | **是否允许为空** | **数据类型** | **字段长度** | **键**  | **备注**                                                     |
| ----------------------- | ---------------- | -------------- | ---------------- | ------------ | ------------ | ------- | ------------------------------------------------------------ |
| ID_                     | 主键             | NULL           | NO               | varchar      | 64           | PRIMARY |                                                              |
| REV_                    | 版本号           | NULL           | YES              | int          | NULL         |         | flowable自己生成的版本号，根据key启动流程实例时，使用该字段版本号最高的数据记录 |
| CATEGORY_               | 分类             | NULL           | YES              | varchar      | 255          |         | 流程定义的Namespace就是类别                                  |
| NAME_                   | 名称             | NULL           | YES              | varchar      | 255          |         |                                                              |
| KEY_                    | 标识             | NULL           | NO               | varchar      | 255          | MUL     | 流程key，flowable底层API中很多操作以该key为准                |
| VERSION_                | 版本             | NULL           | NO               | int          | NULL         |         |                                                              |
| DEPLOYMENT_ID_          | 部署ID           | NULL           | YES              | varchar      | 64           |         |                                                              |
| RESOURCE_NAME_          | 资源名称         | NULL           | YES              | varchar      | 4000         |         | 流程bpmn文件名称                                             |
| DGRM_RESOURCE_NAME_     | 图片资源名称     | NULL           | YES              | varchar      | 4000         |         |                                                              |
| DESCRIPTION_            | 描述             | NULL           | YES              | varchar      | 4000         |         |                                                              |
| HAS_START_FORM_KEY_     | 拥有开始表单标识 | NULL           | YES              | tinyint      | NULL         |         | start节点是否存在formKey 0否  1是                            |
| HAS_GRAPHICAL_NOTATION_ | 拥有图形信息     | NULL           | YES              | tinyint      | NULL         |         |                                                              |
| SUSPENSION_STATE_       | 挂起状态         | NULL           | YES              | int          | NULL         |         | 暂停状态 1激活 2暂停                                         |
| TENANT_ID_              | 租户ID           |                | YES              | varchar      | 255          |         |                                                              |



## 建表语句

```
CREATE TABLE `ACT_RE_PROCDEF` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `HAS_START_FORM_KEY_` tinyint(4) DEFAULT NULL,
  `HAS_GRAPHICAL_NOTATION_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `ENGINE_VERSION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DERIVED_FROM_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DERIVED_FROM_ROOT_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DERIVED_VERSION_` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_`) USING BTREE,
  UNIQUE KEY `ACT_UNIQ_PROCDEF` (`KEY_`,`VERSION_`,`DERIVED_VERSION_`,`TENANT_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```

