写在前面：

我使用源码编译安装，网上大多数教程都是针对旧版本的，2.6.1版本不一定能正常使用，安装的过程中会遇到各种bug，具体需要看日志，google去解决，百度基本查不到，最好优先看官方文档。

https://www.openldap.org/doc/admin26/





1.下载openldap源码包、解压

```
wget https://www.openldap.org/software/download/OpenLDAP/openldap-release/openldap-2.6.1.tgz
tar -zxvf openldap-2.6.1.tgz
```

2.编译

```
cd openldap-2.6.1
./configure --prefix=/home/tdops/openldap --enable-memberof=yes --enable-crypt=yes --with-cyrus-sasl --enable-spasswd

# 这两个参数可以选择要不要，如果使用这两个参数可能还需要安装sasl相关的依赖包(yum install cyrus-sasl*),这两个参数的作用我理解是使用服务器root权限修改openldap的配置(对应网上的教程,一般是ldapadd -Y EXTERNAL xxx.ldif(此处的-Y EXTERNAL是指使用sasl协议))，但实际操作时我没有成功，实际操作时我修改配置是通过配置cn=config数据库的rootdn去操作的
--with-cyrus-sasl --enable-spasswd
```

```
make depend
make
make install
```

3.修改slapd配置文件，此处仅对一些重要的，可能踩坑的地方进行描述，其他的可以用默认的或者自己查

```
具体配置文件的位置和编译时的参数--prefix=/home/tdops/openldap有关
vim /home/tdops/openldap/etc/openldap/slapd.conf
```

配置文件

```
# 一般建议include下面4个schema，引入inetorgperson这个objectclass
include         /home/tdops/openldap/etc/openldap/schema/core.schema
include         /home/tdops/openldap/etc/openldap/schema/cosine.schema
include         /home/tdops/openldap/etc/openldap/schema/inetorgperson.schema
include         /home/tdops/openldap/etc/openldap/schema/nis.schema

# 权限控制，具体语法看下文末的参考文档
access to *
        by users read
        # 这条 by anonymous auth 非常重要，如果没有，则普通用户无法登陆，但是又很难查到为什么，需要看日志去分析
				by anonymous auth
        by dn.exact="cn=Manager,dc=test,dc=cn" manage
        by self write

# openldap自己需要的配置数据库
database config
# config数据库的rootdn 必须是 "cn=config"
rootdn          "cn=config"
rootpw          {SSHA}Wtx8j58juO0bh9D5jOyfvlRH6nQOnwn8

# 实际存储数据的数据库
database        mdb
maxsize         1073741824
suffix          "dc=test,dc=cn"
rootdn          "cn=Manager,dc=test,dc=cn"
rootpw          {SSHA}Wtx8j58juO0bh9D5jOyfvlRH6nQOnwn8
directory       /home/tdops/openldap/var/openldap-data
index   objectClass     eq
# 这个overlay memberof配置非常重要，用于开启memberof的支持，网上大多数的让单独写ldif文件添加memberof支持的文章我都看了并且试验了，不适用于这个版本，会有各种各样的报错
overlay memberof
```

4.启动

前台启动用于debug看日志

```
/home/tdops/openldap/libexec/slapd -d 1
```

后台启动：

```
nohup /home/tdops/openldap/libexec/slapd -d 255 > /dev/null 2>&1 &
```

5.常用命令及解释

```
具体可执行文件的位置和编译时的参数--prefix=/home/tdops/openldap有关
bin/ldapadd -D "cn=Manager,dc=xxx,dc=xxx" -w 123456 -f xxx.ldif
bin/ldapsearch
bin/ldapmodify
sbin/slap
```

\-D: dn，此处一般填slapd.conf里的rootdn(管理员账号)，如果是操作一般数据，则是使用mdb数据库的管理员账号，如果是操作配置（cn=config后面的东西），则需要使用config数据库的管理员账号

-w: 密码

-f: 文件

文件格式具体网上搜下,比如:

```
dn:dc=test,dc=cn
objectClass:dcObject
objectClass:organization
o:base
dc:test
```

注意⚠️：网上搜的大部分命令里都使用了`-Y EXTERNAL`参数，但实际使用中我从未成功过



参考网址:

官方文档：

https://www.openldap.org/doc/admin26/

slap.conf文件说明：

https://linux.die.net/man/5/slapd.conf

openldap命令详解：

https://www.cnblogs.com/xzkzzz/p/9269714.html

其他的一般是遇到问题再去google，百度基本解决不了什么问题