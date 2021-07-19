# ACT_GE_BYTEARRAY

文件管理表，管理描述流程的bpmn文件、流程的Image缩略图、业务规则任务的drl文件等。

业务规则任务的drl规则，改动该表数据后清除系统缓存，即可生效新的drl规则。

所以猜测流程的实际资源文件由该表维护，改动该表数据会影响流程执行。



## 表字段说明

| 字段           | 字段名称           | 字段默认值 | 是否允许为空 | 数据类型 | 字段长度   | 键                | 备注                                                         |
| -------------- | ------------------ | ---------- | ------------ | -------- | ---------- | ----------------- | ------------------------------------------------------------ |
| ID_            | 主键               |            | NO           | varchar  | 64         | PRIMARY           |                                                              |
| REV_           | 版本号             | NULL       | YES          | int      | NULL       |                   | version                                                      |
| NAME_          | 名称               | NULL       | YES          | varchar  | 255        |                   | 部署的文件名称，如：mail.bpmn、mail.png 、mail.bpmn20.xml    |
| DEPLOYMENT_ID_ | 部署ID             | NULL       | YES          | varchar  | 64         | ACT_RE_DEPLOYMENT |                                                              |
| BYTES_         | 字节（二进制数据） | NULL       | YES          | longblob | 4294967295 |                   |                                                              |
| GENERATED_     | 是否系统生成       | NULL       | YES          | tinyint  | NULL       |                   | 0为用户上传， 1为系统自动生 成， 比如系统会 自动根据xml生 成png |



## 建表语句

```
CREATE TABLE `ACT_GE_BYTEARRAY` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BYTES_` longblob,
  `GENERATED_` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID_`) USING BTREE,
  KEY `ACT_FK_BYTEARR_DEPL` (`DEPLOYMENT_ID_`) USING BTREE,
  CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```

