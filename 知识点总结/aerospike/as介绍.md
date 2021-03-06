# 1、概述

## 1.1	什么是Aerospike（AS）

 Aerospike是一个分布式，高可用的 K-V类型的Nosql数据库。提供类似传统数据库的ACID操作。

## 1.2	AS的特点和优势

- 高性能、高可用、高可扩展、高复杂性的NoSQL数据库（注意不是缓存，它就是数据库），支持多种部署模式（纯内存、内存+SSD、纯SSD）
- 同时支持二级索引与聚合，支持简单的sql操作

- 硬件上必须使用SSD或CSS高性能NVMe存储卡，为了达到最高性能建议使用NVMe
- 绕过了OS的文件系统，直接操纵底层硬件

- 采用Pure C语言编写，性能较高，不存在GC类问题
- 同时提供社区版和商业版，前者免费，后者提供更多的功能和技术支持

# 2、基本概念

## 2.1	Namespaces 

AS数据存储的最高层级，类比于传统的数据库的库层级，一个namespace包含记录（records），索引（indexes ）及策略（policies）。其中策略决定namespace的行为，包括：

​      1.数据的存储位置是内存还是SSD。

​      2.一条记录存储的副本个数。

​      3.过期时间（TTL）：不同redis的针对key设置TTL，AS可以在库的层级进行全局设置，并且支持对于已存在的数据进行TTL的设置，方便了使用。

## 2.2	Set  

​    存储于namespace，是一个逻辑分区，类比于传统数据库的表。set的存储策略继承自namespace，也可以为set设置单独的存储策略。

## 2.3	Records

类比于传统数据库的行，包含key，Bins（value）和Metadata（元数据）。

## 2.4	Key

key全局唯一，作为K-V数据库一般也是通过key去查询单条记录。

## 2.5	Bins

- Bins相当于列，存储具体的数据。
- 在一条记录里，数据被存储在一个或多个bins里，bins由名称和值组成。

- bins不需要指定数据类型，数据类型有bins中的值决定。
- bins的名称最多包含32k

# 3、AS架构

## 3.1	分布式架构

![img](https://cdn.nlark.com/yuque/0/2022/png/22616374/1641707282220-0e4ebcc3-94d3-4790-bc92-59c65367e81d.png)

## 3.2	分布式特征

- CAP：AS实现了AP
- 节点自动发现：基于Multicast或Mesh

- 数据分区

- - 每个Namespace固定4096个数据分区
  - 任意节点加入或下线会立即触发数据迁移(Migration)以保证可用性

- - 迁移过程不需要人工干预且对正常响应无影响

## 3.4 AS存储结构

![img](https://cdn.nlark.com/yuque/0/2022/png/22616374/1641708011572-05c2c5ff-7235-4ff3-92d8-13c68f2ae469.png)

- 主索引
- 每个Key对一个固定64字节长度的索引

- AS默认不保存原始Key
- 索引仅存于内存而不做持久化

- 节点重启时需要全盘扫描并重建内存索引
- 每个副本独立拥有一套索引（一般2个副本）

![img](https://cdn.nlark.com/yuque/0/2022/png/22616374/1641708177471-581c24ab-edde-4c5a-b9ae-5a4f45d529da.png)

# 4、AS功能介绍

## 4.1	AS客户端智能路由

- 客户端自动感知节点拓扑结构变化并路由请求
- 客户端自动感知数据分区情况

- 内置高性能TCP连接池
- 内置智能故障重试/重传机制

## 4.2	数据结构

- 简单KV

- - int,string,byte,double,blob

- 复杂KV

- - list/map
  - sorted map

- - GeoJOSN

- - - 支持地理信息检索功能

- - Record（对象）

- - - AS独有，类比传统数据库的一行记录
    - 可原子性的单独操作对象中的一个成员值

- 服务端批处理

- - 多个操作一次性提交到服务端，服务端保证在同一个事务中处理

- 支持数据逐条策略，默认为LRU
- 支持LUA编写UDF函数

- 支持Scan（一般禁止使用）

## 4.3 限制和推荐

- Value长度原则上小于128KB（理论最大值为1M）

- - Value长度4~8KB性能最佳

- 尽量从业务上避免热点数据频繁写入
- 原则上所有数据必须具有TTL，特殊场景除外

- 不同业务的AS资源勿相互“借用”
- 业务开发需持续关注监控

# 5、性能压测

- 机器配置

![img](https://cdn.nlark.com/yuque/0/2022/png/22616374/1641709932227-39859bbf-177c-416e-9a92-c3c4437b421b.png)

- 读写5:5

![img](https://cdn.nlark.com/yuque/0/2022/png/22616374/1641709981860-773439e3-6cf4-4b32-ae74-bf2fbb7d002d.png)

- 读写9:1

![img](https://cdn.nlark.com/yuque/0/2022/png/22616374/1641710084618-38a98c6d-03c0-4ed5-b072-737180e20118.png)