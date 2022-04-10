1.按照官方文档命令安装

```
# 安装依赖
sudo yum install -y curl policycoreutils-python openssh-server perl
sudo systemctl enable sshd
sudo systemctl start sshd
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo systemctl reload firewalld
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo systemctl reload firewalld

# 安装gitlab
wget https://omnibus.gitlab.cn/el/7/gitlab-jh-14.9.2-jh.0.el7.x86_64.rpm
sudo rpm -Uvh gitlab-jh-14.9.2-jh.0.el7.x86_64.rpm
```

此时报错防火墙没开也无所谓

2.修改/etc/gitlab/gitlab.rb文件

搜索external_url，修改为本机ip，暴露对外访问端口

3.第一次登陆，root用户的密码是随机生成的

/etc/gitlab/initial_root_password



常用操作:

```
gitlab-ctl reconfigure
gitlab-ctl stop
gitlab-ctl restart
```



参考网址：

https://about.gitlab.cn/install/

https://docs.gitlab.cn/omnibus/installation/