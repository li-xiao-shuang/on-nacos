# 安装包

## 安装

**步骤一**：可以通过 [https://github.com/alibaba/nacos/releases](https://github.com/alibaba/nacos/releases) 找到对应的版本，下载打包好的 Nacos。可使用以下命令下载对应的 Nacos版本。目前下载的是2.1.0 版本，可使用以下命令：

```java
wget https://github.com/alibaba/nacos/releases/download/2.1.0/nacos-server-2.1.0.tar.gz
```

**步骤二**：下载完压缩包以后需要通过命令进行解压：

```java
tar -xzf nacos-server-2.1.0.tar.gz
```

**步骤三**：cd 到 nacos 目录下，先来介绍下目录结构

```java
.
|____LICENSE
|____bin                                 // nacos 的启动、停止脚本
| |____startup.sh
| |____startup.cmd
| |____shutdown.sh
| |____shutdown.cmd
|____target                              // nacos 服务端 jar 包
| |____nacos-server.jar
|____NOTICE
|____conf                                // naocs 配置文件、sql脚本、集群配置等
| |____1.4.0-ipv6_support-update.sql
| |____schema.sql
| |____nacos-mysql.sql
| |____application.properties.example
| |____nacos-logback.xml
| |____cluster.conf.example
| |____application.properties
```

**步骤四**：单机模式启动 Nacos

**Linux/Unix/Mac**

启动命令( -m standalone 表示单机模式启动):

`sh startup.sh -m standalone`

如果您使用的是ubuntu系统，或者运行脚本报错提示[[符号找不到，可尝试如下运行：

`bash startup.sh -m standalone`

**Windows**

启动命令( -m standalone 表示单机模式启动):

`startup.cmd -m standalone`

出现以下日志说明已经启动 naocs 了，详细的日志要看 nacos/logs/start.out。

```java
nacos is starting with standalone
nacos is starting，you can check the /Users/lixiaoshuang/nacos-related/nacos/logs/start.out
```

**步骤五**：通过浏览器访问：[http://127.0.0.1:8848](http://127.0.0.1:8848/nacos/#/login)/nacos   登录nacos 控制台，默认的账户名和密码是：nacos、nacos。

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jghe1b57j215u0u0div.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jghe1b57j215u0u0div.jpg)

> 备注：单机模式启动默认是不需要 MySQL 的，如果想要使用 MySQL 可以修改配置中的数据源信息
>

## 配置管理

通过 curl 命令调用 Nacos 的 Open API 来快速体验一下 Nacos 的配置管理功能。

**发布配置**：

```java
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test&content=Hello Nacos"
```

通过 curl 命令发布完配置后，可以在通过控制台配置管理-配置列表中查看到配置信息。

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jh75hwdhj22300tgtcu.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jh75hwdhj22300tgtcu.jpg)

**获取配置**：

```java
curl -X GET "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test"
```

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jh8xya7ej21fa0eq761.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jh8xya7ej21fa0eq761.jpg)

## 服务注册&发现

通过 curl 命令调用 Nacos 的 Open API 来快速体验一下 Nacos 的服务发现功能。

**服务注册**：

```java
curl -X POST 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=nacos.naming.serviceName&ip=20.18.7.10&port=8080'
```

执行完 curl 命令后 ，可以在控制台服务管理-服务列表中查看到注册的服务信息。

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jhexgudnj223i0quwie.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jhexgudnj223i0quwie.jpg)

**服务发现**：

```java
curl -X GET 'http://127.0.0.1:8848/nacos/v1/ns/instance/list?serviceName=nacos.naming.serviceName'
```

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jhi77yujj21no0ki0y3.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jhi77yujj21no0ki0y3.jpg)

## 使用 MySQL

如果想要 Nacos 使用 MySQL 来做底层存储的话，首先需要找到 nacos/conf 目录下的 nacos-mysql.sql 。执行 sql 脚本创建 nacos 相关库表。

然后就只需要修改 [application.properties](http://application.properties) 文件，首先 cd 到 nacos/conf 目录下，用 vim 命令修改 application.properties 文件,找到下面这段注释：

```java
#*************** Config Module Related Configurations ***************#
### If use MySQL as datasource:
# spring.datasource.platform=mysql

### Count of DB:
# db.num=1

### Connect URL of DB:
# db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
# db.user.0=nacos
# db.password.0=nacos
```

将注释放开，修改 db.url.0 的数据库链接为实际要连的数据库地址，修改对应的 user、password。

```java
#*************** Config Module Related Configurations ***************#
### If use MySQL as datasource:
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=12345678
```

当修改完以上配置以后，在通过 nacos/bin 目录下的启动脚本启动 nacos 即可：

```java
sh startup.sh -m standalone
```

# 调试源码

**步骤一**：通过 git 命令将 nacos 仓库 clone 到本地

```java
git clone https://github.com/alibaba/nacos.git
```

**步骤二**：使用idea 打开 nacos 源码

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jl16bygzj21bg0u0got.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jl16bygzj21bg0u0got.jpg)

**步骤三**：使用 mvn clean compile -U -Dmaven.test.skip=true 命令编译下项目

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jlbydm93j20yi0u0dn0.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jlbydm93j20yi0u0dn0.jpg)

**步骤四**：找到 com.alibaba.nacos.Nacos 启动类，在 idea 启动时添加 VM 参数-Dnacos.standalone=true，然后再启动，就可以调试 nacos 源码啦。

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3jllyxo7sj219c0u0tbp.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3jllyxo7sj219c0u0tbp.jpg)