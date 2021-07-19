# ACT_RU_VARIABLE

未结束的流程实例，流程变量数据表，相当于一个redis。核心表。

流程变量可以用来做分支判断或用于关联前后节点等操作。



## 表字段说明

| **字段**      | **字段名称** | **字段默认值** | **是否允许为空** | **数据类型** | **字段长度** | **键**                  | **备注**                                                     |
| ------------- | ------------ | -------------- | ---------------- | ------------ | ------------ | ----------------------- | ------------------------------------------------------------ |
| ID_           | 主键         | NULL           | NO               | varchar      | 64           | PRI                     |                                                              |
| REV_          | 版本号       | NULL           | YES              | int          | NULL         |                         | version                                                      |
| TYPE_         | 类型         | NULL           | NO               | varchar      | 255          |                         | 见备注                                                       |
| NAME_         | 名称         | NULL           | NO               | varchar      | 255          |                         |                                                              |
| EXECUTION_ID_ | 执行实例ID   | NULL           | YES              | varchar      | 64           | MUL（ACT_RU_EXECUTION） |                                                              |
| PROC_INST_ID_ | 流程实例ID   | NULL           | YES              | varchar      | 64           | MUL（ACT_RU_EXECUTION） |                                                              |
| TASK_ID_      | 任务ID       | NULL           | YES              | varchar      | 64           | MUL（ACT_RU_TASK）      |                                                              |
| BYTEARRAY_ID_ | 资源ID       | NULL           | YES              | varchar      | 64           | MUL（ACT_GE_BYTEARRAY） |                                                              |
| DOUBLE_       | 浮点值       | NULL           | YES              | double       | NULL         |                         | 存储变量类型为Double                                         |
| LONG_         | 长整型       | NULL           | YES              | bigint       | NULL         |                         | 存储变量类型为long                                           |
| TEXT_         | 文本值       | NULL           | YES              | varchar      | 4000         |                         | 存储变量值类型为String  如此处存储持久化对象时，值jpa对象的class |
| TEXT2_        | 文本值       | NULL           | YES              | varchar      | 4000         |                         | 此处存储的是JPA持久化对象时，才会有值。此值为对象ID          |

注：
1.运行时流程变量数据表。
2.类型：jpa-entity、boolean、bytes、serializable(可序列化)、自定义type(根据你自身配置)、

 CustomVariableType、date、double、integer、long、null、short、string



## 建表语句

```
CREATE TABLE `ACT_RU_VARIABLE` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `SCOPE_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUB_SCOPE_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SCOPE_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` longtext COLLATE utf8_bin,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_IDX_RU_VAR_SCOPE_ID_TYPE` (`SCOPE_ID_`,`SCOPE_TYPE_`) USING BTREE,
  KEY `ACT_IDX_RU_VAR_SUB_ID_TYPE` (`SUB_SCOPE_ID_`,`SCOPE_TYPE_`) USING BTREE,
  KEY `ACT_FK_VAR_BYTEARRAY` (`BYTEARRAY_ID_`) USING BTREE,
  KEY `ACT_IDX_VARIABLE_TASK_ID` (`TASK_ID_`) USING BTREE,
  KEY `ACT_FK_VAR_EXE` (`EXECUTION_ID_`) USING BTREE,
  KEY `ACT_FK_VAR_PROCINST` (`PROC_INST_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_VAR_BYTEARRAY` FOREIGN KEY (`BYTEARRAY_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```





