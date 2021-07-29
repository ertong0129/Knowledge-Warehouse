# undolog概念

重做日志（提供回滚）

undolog在内存中存放在缓冲池中，在文件中存放在共享表空间中：ibdata

除了回滚，undo的另一个作用是MVCC。当用户读取一行记录时，若该记录已被其他事务占用，当前事务可以通过undolog读取之前的版本信息，以此实现非锁定读。

undolog也会产生redolog，undolog也需要持久性保护。