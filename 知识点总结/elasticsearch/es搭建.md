公司使用版本：6.8.2，需要运维操作安全组，开放服务器端口号9200、9300、5601

具体步骤可看参考网址操作

## es搭建：

wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.8.2.tar.gz

1.按需修改配置文件

2.修改系统配置文件

2.1 创建系统用户elastic: adduser elastic

2.2 其他配置看启动报错按需调整

3.操作安全认证、增加用户、密码

3.1 elasticsearch-certutil ca （后续确认全回车）

3.2 elasticsearch-certutil cert --ca elastic-stack-ca.p12 （后续确认全回车）

3.3 将安全认证文件拷到config/cert文件夹下

3.4 elasticsearch.yml加配置：

```json
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate
xpack.security.transport.ssl.keystore.path: /usr/elasticsearch/elasticsearch-6.8.2/certs/elastic-certificates.p12
xpack.security.transport.ssl.truststore.path: /usr/elasticsearch/elasticsearch-6.8.2/certs/elastic-certificates.p12
```

3.5 初始化密码：elasticsearch-setup-passwords auto

3.6 保存初始化的密码，保存至config/init_user.config文件



es启动：/usr/elasticsearch/elasticsearch-6.8.2/bin/elasticsearch -d



初始账号密码文件地址：/usr/elasticsearch/elasticsearch-6.8.2/config/init_user.config

安全认证文件地址：/usr/elasticsearch/elasticsearch-6.8.2/config/cert/elastic-certificates.p12

## kibana搭建：

wget https://artifacts.elastic.co/downloads/kibana/kibana-6.8.2-linux-x86_64.tar.gz

1.按需修改配置文件（如果es有密码，配置文件要配账号密码）



kibana启动：/usr/elasticsearch/kibana-6.8.2-linux-x86_64/bin/kibana &

## 参考网址：

https://www.cnblogs.com/pxblog/p/12632756.html

https://www.codenong.com/cs109535035/

https://www.cnblogs.com/lovelinux199075/p/9101631.html

https://blog.csdn.net/star1210644725/article/details/116027973

https://blog.csdn.net/casuallc/article/details/116006687