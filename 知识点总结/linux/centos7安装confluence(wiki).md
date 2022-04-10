1.安装jdk

2.安装mysql

3.下载、解压、安装confluence

```
wget https://product-downloads.atlassian.com/software/confluence/downloads/atlassian-confluence-7.4.6-x64.bin
chmod +x atlassian-confluence-7.4.6-x64.bin
./atlassian-confluence-7.4.6-x64.bin
```

启动命令为:

```
/opt/atlassian/confluence/bin/startup.sh
```

启动后需要破解或注册才能使用

如果启动有报错，一般在

```
/opt/atlassian/confluence/logs/catalina.out
```



注意点:

1.官方建议不使用root权限启动项目，但是项目运行的用户必须要所需目录的权限

比如 `/opt/atlassian`，比如`安装目录/confluence`

```
具体哪些需要看自己的安装目录或者报错去修改
chown -R confluence:confluence /opt/atlassian
chown -R confluence:confluence /var/atlassian
chown -R confluence:confluence /home/confluence
```

2.默认Xmx是1g，服务器内存小于1g需要修改配置文件启动(很容易卡死)

```
/opt/atlassian/confluence/bin/setenv.sh
```

3.使用mysql数据库时，数据库编码必须是utf8_bin 或 utf8mb4_bin编码

4.数据库的隔离级别必须是read committed

5.配置mysql的数据库链接时，必须配编码，如

```
jdbc:mysql://ip/confluence?useUnicode=true&amp;characterEncoding=UTF-8
```

项目的具体配置一般在这个位置：

```
/var/atlassian/application-data/confluence/confluence.cfg.xml
```





参考网址:

https://blog.csdn.net/zz_aiytag/article/details/120020919