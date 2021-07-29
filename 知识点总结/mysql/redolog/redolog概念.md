# redolog概念

重做日志（提供前滚功能）。

mysql修改数据后，并不会直接将修改后的数据同步到磁盘的表空间去，而是仅修改缓冲池中的页（脏页）。后台线程定时将脏页刷新到磁盘去，或脏页数量超过一定占比后主动刷新到磁盘。当mysql宕机时，如果内存中的脏页没有全部刷到磁盘，就会出现磁盘与内存数据不一致的情况。在mysql恢复时，就会根据redolog的LSN进行数据恢复，保证数据的可靠性。



## redolog与binlog的区别：

binlog不用来恢复数据，binlog是mysql服务器级的，binlog只在事务提交时增加记录，binlog记录所有执行修改数据的语句。

redolog用来恢复数据，redolog是innodb引擎级的，redolog在事务开启后就增加记录，redolog记录物理页数据变化。



## redolog使用的文件：

ib_logfile0、ib_logfile1



## redolog和binlog的XA

事务提交时，mysql往磁盘写数据时，先写binlog，再写redolog。

如果写binlog成功，宕机，没写上redolog，会导致从库同步到binlog提交了事务，但主库没提交事物的情况。

为了解决这个问题，innodb先做一个prepare操作，将事物的xid写入，接着二进制日志写入。如果innodb存储引擎提交前mysql宕机了，mysql重启后会先检查准备的uxid事务是否已经提交，如果未提交则再提交一次。



## redolog从日志缓冲刷新到磁盘的时机

后台线程定时同步

事务提交时同步



## LSN

LSN表示事务写入重做日志的字节的总量，代表日志序列号。