# 使用NMT和pmap分析java内存

内存分为堆（新生代（eden、survivor）、老年代、）、非堆（元空间、直接内存、code cache、压缩类空间）、虚拟机栈空间、本地方法栈空间

堆内存是比较好分析的，可以通过`jstat -gc pid`分析。堆外内存大小可以用arthas的dashboard查看。但其他内存空间就无法使用之前的工具进行分析。

此时需要打开java内置的内存链路追踪选项+使用linux pmap命令分析java进程内存占用。



## NMT

在Java启动项中，加入启动项： `-XX:NativeMemoryTracking=detail`

ps：该参数需要放在-jar前，否则会报错：`Java HotSpot(TM) 64-Bit Server VM warning: Native Memory Tracking did not setup properly, using wrong launcher?`

https://www.hellojava.com/a/47098.html

执行如下命令： `jcmd pid VM.native_memory detail`



## pmap

pmap - report memory map of a process(查看进程的内存映像信息)pmap命令用于报告进程的内存映射关系，是Linux调试及运维一个很好的工具。

用法
       pmap [ -x | -d ] [ -q ] pids...
       pmap -V
选项含义
       -x   extended       Show the extended format. 显示扩展格式
       -d   device         Show the device format.   显示设备格式
       -q   quiet          Do not display some header/footer lines. 不显示头尾行
       -V   show version   Displays version of program. 显示版本
扩展格式和设备格式域：
        Address:  start address of map  映像起始地址
        Kbytes:  size of map in kilobytes  映像大小
        RSS:  resident set size in kilobytes  驻留集大小
        Dirty:  dirty pages (both shared and private) in kilobytes  脏页大小
        Mode:  permissions on map 映像权限: r=read, w=write, x=execute, s=shared, p=private (copy on write)  
        Mapping:  file backing the map , or '[ anon ]' for allocated memory, or '[ stack ]' for the program stack.  映像支持文件,[anon]为已分配内存 [stack]为程序堆栈
        Offset:  offset into the file  文件偏移
        Device:  device name (major:minor)  设备名



## 参考网址

https://blog.csdn.net/jicahoo/article/details/50933469

https://blog.csdn.net/bbwangj/article/details/80698291