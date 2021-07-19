# ACT_HI_VARINST

所有流程变量数据表，结束或未结束的流程实例所使用的流程变量都在该表记录。



## 表字段说明

| 字段               | 字段名称     | 字段默认值 | 是否允许为空 | 数据类型 | 字段长度 | 键               | 备注                                                         |
| ------------------ | ------------ | ---------- | ------------ | -------- | -------- | ---------------- | ------------------------------------------------------------ |
| ID_                | 主键         | NULL       | NO           | varchar  | 64       | PRIMARY          |                                                              |
| PROC_INST_ID_      | 流程实例ID   | NULL       | YES          | varchar  | 64       | MUL              |                                                              |
| EXECUTION_ID_      | 执行实例ID   | NULL       | YES          | varchar  | 64       |                  |                                                              |
| TASK_ID_           | 任务ID       | NULL       | YES          | varchar  | 64       | MUL              |                                                              |
| NAME_              | 名称         | NULL       | NO           | varchar  | 255      | MUL              |                                                              |
| VAR_TYPE_          | 变量类型     | NULL       | YES          | varchar  | 100      |                  |                                                              |
| REV_               | 版本号       | NULL       | YES          | int      | NULL     |                  | version                                                      |
| BYTEARRAY_ID_      | 字节流ID     | NULL       | YES          | varchar  | 64       | ACT_GE_BYTEARRAY |                                                              |
| DOUBLE_            | 浮点值       | NULL       | YES          | double   | NULL     |                  | 存储DoubleType类型的数据                                     |
| LONG_              | 长整型       | NULL       | YES          | bigint   | NULL     |                  | 存储LongType类型的数据                                       |
| TEXT_              | 文本值       | NULL       | YES          | varchar  | 4000     |                  | 存储变量值类型为String，如此处存储持久化对象时，值jpa对象的class |
| TEXT2_             | 文本值       | NULL       | YES          | varchar  | 4000     |                  |                                                              |
| CREATE_TIME_       | 创建时间     | NULL       | YES          | datetime | NULL     |                  |                                                              |
| LAST_UPDATED_TIME_ | 最后更新时间 | NULL       | YES          | datetime | NULL     |                  |                                                              |



## 建表语句

```
CREATE TABLE `ACT_HI_VARINST` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT '1',
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `SCOPE_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` datetime(3) DEFAULT NULL,
  `LAST_UPDATED_TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_HI_PROCVAR_NAME_TYPE` (`NAME_`,`VAR_TYPE_`) USING BTREE,
  KEY `ACT_IDX_HI_VAR_SCOPE_ID_TYPE` (`SCOPE_ID_`,`SCOPE_TYPE_`) USING BTREE,
  KEY `ACT_IDX_HI_VAR_SUB_ID_TYPE` (`SUB_SCOPE_ID_`,`SCOPE_TYPE_`) USING BTREE,
  KEY `ACT_IDX_HI_PROCVAR_PROC_INST` (`PROC_INST_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_PROCVAR_TASK_ID` (`TASK_ID_`) USING BTREE,
  KEY `ACT_IDX_HI_PROCVAR_EXE` (`EXECUTION_ID_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```

