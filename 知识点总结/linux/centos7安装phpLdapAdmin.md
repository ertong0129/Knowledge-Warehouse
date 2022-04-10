安装依赖

```
yum -y install httpd php php-ldap php-gd php-mbstring php-pear php-bcmath php-xml
```

安装phpadmin

```
yum -y install epel-release
yum --enablerepo=epel -y install phpldapadmin
```

修改配置文件

```
vim /etc/phpldapadmin/config.php
#397行取消注释，398行添加注释
$servers->setValue('login','attr','dn');
// $servers->setValue('login','attr','uid');

vim /etc/httpd/conf.d/phpldapadmin.conf
<IfModule mod_authz_core.c>
# Apache 2.4
# 允许本地访问
Require local
# 允许某个ip访问
Require ip 172.31.101.110
# 允许所有ip访问
Require all granted
</IfModule>
```



参考网址:

https://blog.51cto.com/u_11555417/2065747