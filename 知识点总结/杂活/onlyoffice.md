- [1.什么是onlyoffice](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-1.什么是onlyoffice)
- [2.部署资源](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-2.部署资源)
- [3.集成](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-3.集成)
  - [3.1集成方式及注意事项](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-3.1集成方式及注意事项)
    - [3.1.1前端](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-3.1.1前端)
    - [3.1.2后端](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-3.1.2后端)
    - [3.1.3注意事项](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-3.1.3注意事项)
  - [3.2操作时序图](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-3.2操作时序图)
  - [3.3权限控制的一种方式](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-3.3权限控制的一种方式)
- [4.onlyoffice重点配置](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-4.onlyoffice重点配置)
  - [4.1Config](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-4.1Config)
  - [4.2Config.Document](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-4.2Config.Document)
  - [4.3Config.Editor](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-4.3Config.Editor)
  - [4.4参数示例](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-4.4参数示例)
- [5.附录](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-5.附录)
  - [5.1也可以看看](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-5.1也可以看看)
  - [5.2相关网址](http://wiki.zswltech.cn:8888/display/SPC/onlyoffice#onlyoffice-5.2相关网址)



# 1.什么是onlyoffice

onlyoffice可以与我们的业务系统集成，提供文档协作的服务功能，支持Word，Excel和PowerPoint的预览和编辑。

onlyoffice有开源社区版，有免费的版本，包含大部分商业版的功能，但有20同时在线连接的限制（可破解）。

onlyoffice在浏览器上操作office文档功能肯定没有桌面版功能齐全，但是在web界面上做一些简单的操作还是可以的。

# 2.部署资源

建议使用docker部署，onlyoffice本身需要一些mq、数据库等相关配套组件（但这部分数据我个人认为并不需要保证不丢失，数据只用作缓存），如果单独安装相关套件，比较麻烦。

# 3.集成

## 3.1集成方式及注意事项

简单来说，onlyoffice的使用方式就是前端引入onlyoffice的js文件，并传递参数调用它的api，唤起onlyoffice的编辑器，后续在界面上的操作全由onlyoffice自己处理。

比较重要的配置就包括：文件的key、下载文件的url、编辑完后回调的url。

最简单的实现情况下：后端3个接口，前端引1个js文件并调用后端1个接口。

onlyoffice也提供了一些jsapi可供调用，前端可以在合适的时机（如封装一个按钮）进行调用以改变编辑器的属性。

### 3.1.1前端

1.针对onlyoffice，前端仅需要引入onlyoffice的一个js（{{host}}/web-apps/apps/api/documents/api.js），这个js文件会自动引用剩余所需的js文件并在合适的时机自动调用onlyoffice服务端（所以无法把这个js文件复制出来单独引用）。

2.针对业务系统，前端需要调用后端提供的接口，获取onlyoffice的配置。（原因：后端可以动态判断权限或其他的条件，动态的修改编辑器的展现形式）

3.在文件操作界面初始化时，调用后端配置接口获取onlyoffice配置，然后带着配置调用onlyoffice的jsapi，初始化编辑器。

### 3.1.2后端

1.针对onlyoffice，后端需要给它的服务端提供两个接口：文件下载接口和编辑后回调接口。

文件下载接口：前端调用jsapi唤起编辑器时会传文件下载url，然后onlyoffice js会将该参数传给它的服务端，它的服务端会访问url下载文件。租赁系统的做法是将下载接口统一，文件的标识就是onlyoffice所需的文件key，根据该key我们需要维护对应的文件信息或oss路径，然后进行下载。

编辑后回调接口：需提供一个POST（JSON）接口。用户在界面上编辑后，onlyoffice服务端回调回来的参数是一个json，里面包含了如本次操作类型、文件缓存下载地址（用户编辑完文件后，onlyoffice会将该文件暴露出一个下载地址，但有时效）等。我们的接口在接收到回调后，需下载缓存文件上传到我们自己的文件系统，并处理相关业务逻辑。

2.针对业务系统，后端需要提供接口将onlyoffice的配置传递给前端。前端需传参数：要预览的文件标识（我们所有模块文件在一张表里，传1个文件id即可。如果要预览的文件散落在不同表，就需要额外参数来定位到具体的文件）、本次操作类型（预览还是编辑）、窗体类型（web端还是移动端）

3.由于onlyoffice文件预览、编辑等都有个key，所以后端需要维护一套key → 业务自身文件数据的映射。租赁系统的做法是前端在调用接口获取onlyoffice配置的时候，针对该文件获取一个已存在的key，如果不存在则生成。用户在界面上编辑文件，onlyoffice回调我方后端时，根据key查到oss中存储的路径，保存文件后需更新此key（否则会导致编辑器报错：文件已发生变化，xxx）。

给出一个key映射表以供参考，key的生成规则就是一个随机字符串

```sql
CREATE TABLE `onlyoffice_key_store` ( 
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', 		  
  `file_oss_path` varchar(256) NOT NULL COMMENT '文件oss路径', 
  `file_oss_path_md5` varchar(32) NOT NULL COMMENT '文件oss路径 md5值', 
  `file_key` varchar(32) NOT NULL COMMENT '随机生成的key，文件未改动前不变', 
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP, 
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='onlyoffice文件key存档';
```



### 3.1.3注意事项

1.后端返回的配置字段名最好都有确定值，如果没有值，字段值为null或undefined，会导致非预期的效果（不会使用默认配置）

2.onlyoffice编辑后回调后端服务传的参数中，文件下载的url的host与前端引入js的host一致，后端服务需能访问该host才能正常下载文件。

这个问题在我们的内网环境中有点绕，为了不连vpn访问业务系统，需要给onlyoffice服务器配域名（xxx.zswl.cn:8888），但是我们的服务器本身无法访问公网，正常情况下后端也就无法下载到文件。需要在后端服务器上配置host和nginx转发规则，后端访问域名（xxx.zswl.cn:8888）时需要通过内网访问到onlyoffice服务。

3.注意对和onlyoffice服务端交互的接口放开登陆校验

## 3.2操作时序图



## 3.3权限控制的一种方式

既然配置全由后端返回，后端就可以根据权限动态的填充参数，如果没有编辑权限，就报错或者填充成预览态的参数就好了。

租赁系统的做法：先根据前端参数渲染出通用的配置，再根据当前想操作的文件相关的业务属性对配置做修改。

# 4.onlyoffice重点配置

上文中提过前端需要带着参数调用onlyoffice的jsapi唤起编辑器，这些参数就是一个json，这里简单描述下重点参数（都以官方字段命名进行描述，实际json里的字段名可能有出入）。

官网配置属性介绍：https://api.onlyoffice.com/editors/config/

## 4.1Config

Config.documentType：文件类型（word、cell、slide（ppt等文件）），onlyoffice针对不同类型文件有不同的渲染方式

Config.type：窗体类型（desktop、mobile、embedded），不同窗体有不同渲染方式，一般只用desktop（浏览器）、mobile（移动端）

## 4.2Config.Document

Config.Document.fileType：文件后缀名（不带.），如docx、xlsx。

Config.Document.key：文件标识，同一个key代表同一个文件，onlyoffice自己有数据，并且多人协作时也需要根据这个key指向onlyoffice数据里的同一个文件。

Config.Document.title：文件标题。

Config.Document.url：文件下载地址，这个地址要能下载到一个文件，才能正常预览。

Config.Document.Permissions.copy：界面上的内容是否可复制。

Config.Document.Permissions.download：下载权限，设成false下载tab会被隐藏。

Config.Document.Permissions.edit：编辑权限，设成false会展示预览态，但此处不是唯一的控制入口（Config.Editor.mode），两处配置的控制最好保持一致，否则会出现非预期的结果。

Config.Document.Permissions.print：打印权限，设成false打印tab会隐藏。

Config.Document.Permissions.review：查看修订变更的权限，最好设成true。

## 4.3Config.Editor

Config.Editor.callbackUrl：编辑后回调的url。这应该指向一个POST接口，传递回来的数据是json格式。

Config.Editor.lang：语言参数，中文需设置成"zh-CN"，控制整个编辑器界面的显示的语言。

Config.Editor.mode：编辑还是预览（edit、view），但此处不是唯一的控制入口（Config.Document.Permissions.edit），两处配置的控制最好保持一致，否则会出现非预期的结果。

Config.Editor.region：地区参数，最好和Config.Editor.lang设置成一致的。中文设成"zh-CN"。

Config.Editor.Customization.help：是否显示帮助tab。建议设成false关掉tab，因为显示的help会指向onlyoffice官网。

Config.Editor.Customization.plugins：是否显示插件tab。设成false把插件tab关掉，全是没用的东西。

Config.Editor.Customization.chat：是否显示聊天tab。如果没有需要建议设成false关掉。

Config.Editor.Customization.comments：是否显示评论tab。如果没有需要建议设成false关掉。

Config.Editor.Customization.forcesave：设置成true时，编辑态界面上点击保存按钮也会触发回调，可以保存文件。

## 4.4参数示例

预览态：

```
{
    "documentType": "word",
    "type": "desktop",
    "document": {
        "fileType": "docx",
        "key": "qqt1ifsh0617au681j4kjzwq8xjz8vsr",
        "title": "融资租赁合同-路乙乙科技",
        "url": "http://172.16.200.21:7003/onlyoffice/download?key=qqt1ifsh0617au681j4kjzwq8xjz8vsr",
        "permissions": {
            "chat": false,
            "comment": false,
            "copy": true,
            "download": true,
            "edit": false,
            "print": true,
            "review": true
        }
    },
    "editorConfig": {
        "callbackUrl": "http://172.16.200.21:7003/onlyoffice/callback",
        "lang": "zh-CN",
        "region": "zh-CN",
        "user": {
            "id": "3",
            "name": "administrator"
        },
        "customization": {
            "autosave": true,
            "comments": false,
            "help": false,
            "plugins": false
        },
        "mode": "view",
        "coEditing": {
            "mode": "fast",
            "change": true
        }
    }
}
```

编辑态：

```
{
    "documentType": "word",
    "type": "desktop",
    "document": {
        "fileType": "docx",
        "key": "qqt1ifsh0617au681j4kjzwq8xjz8vsr",
        "title": "融资租赁合同-路乙乙科技",
        "url": "http://172.16.200.21:7003/onlyoffice/download?key=qqt1ifsh0617au681j4kjzwq8xjz8vsr",
        "permissions": {
            "chat": false,
            "comment": false,
            "copy": true,
            "download": true,
            "edit": true,
            "print": true,
            "review": true
        }
    },
    "editorConfig": {
        "callbackUrl": "http://172.16.200.21:7003/onlyoffice/callback",
        "lang": "zh-CN",
        "region": "zh-CN",
        "user": {
            "id": "3",
            "name": "administrator"
        },
        "customization": {
            "autosave": true,
            "comments": false,
            "help": false,
            "plugins": false
        },
        "mode": "edit",
        "coEditing": {
            "mode": "fast",
            "change": true
        }
    }
}
```



# 5.附录

## 5.1相关网址

官网

https://www.onlyoffice.com/

官网配置属性介绍（部分属性仅在商业版可使用）

https://api.onlyoffice.com/editors/config/

github

https://github.com/ONLYOFFICE/DocumentServer

一些博客教程

https://www.cnblogs.com/cat520/p/9334063.html
https://juejin.cn/post/6979819010313420807