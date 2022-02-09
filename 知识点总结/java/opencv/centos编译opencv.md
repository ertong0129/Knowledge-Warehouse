1.安装前置软件、安装jdk

```
yum update
yum install ant
yum install cmake3
yum install gcc-c++

如需安装jdk
yum install java-1.8.0-openjdk.x86_64
```

2.下载openvc、openvc_contrib源码（需要是同版本的），放到同一目录下

```
wget https://github.com/opencv/opencv/tags
wget https://github.com/opencv/opencv_contrib/tags
```

3.在openvc、openvc_contrib源码同目录创建文件夹

```
mkdir openvc_build
```

4.在shell终端执行语句，声明java home

```
export JAVA_HOME=

ps:查看java所在目录：which java
```

5.执行cmake

```
cmake ../opencv-4.5.3 -DOPENCV_EXTRA_MODULES_PATH=/目录/opencv_contrib-4.5.3/modules
```

执行后需要出现以下图片，才会生成java使用的jar包，否则编译不报错但不生成jar包：

ant和JNI都需要有路径，环境错误的情况下值为NO

![](/Users/jingling/Desktop/工作文件/图片识别二维码/cmake注意事项.png)

7.编译

```
make -j2
```

8.在build对应文件夹下找到文件

```
so：./lib/libopencv_java453.so
jar：./bin/opencv-453.jar
```

9.启动时，java.library.path指定libopencv_java453.so目录

```
java ...其他参数 -Djava.library.path=/tmp/wch_tmp/opencv_build/lib -jar xxx.jar
```

```
java -Djava.library.path=/tmp/wch_tmp/opencv_build/lib -Xmx2g -Xms2g -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M -XX:+UseParNewGC -XX:+PrintHeapAtGC -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+UseConcMarkSweepGC -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection -XX:CMSInitiatingOccupancyFraction=80 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/data/logs/gc.out -Dspring.profiles.active=online -jar qrcode_demo.jar
```

