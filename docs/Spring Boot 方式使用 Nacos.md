本文介绍下如何在 Spring Boot 项目中使用 Nacos，Nacos 主要分为两个部分，配置中心和服务注册与发现。在使用 Spring Boot 项目中使用 Nacos ，首先要保证启动一个 Nacos 服务，具体可以参考《[快速上手 Nacos](https://github.com/li-xiao-shuang/on-nacos/blob/master/docs/%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B%20Nacos.md)》来搭建一个单机的 Nacos 服务。

Nacos 封装 starter 的源代码可以参考 **[nacos-spring-boot-project](https://github.com/nacos-group/nacos-spring-boot-project)** 这个项目，感兴趣的朋友可以查看源代码。

> 本篇文章的详细的代码示例点击[【nacos-spring-boot】](https://github.com/li-xiao-shuang/on-nacos/tree/master/nacos-spring-boot)查看

# 配置中心

## 创建配置

打开控制台 [http://127.0.0.1:8848/nacos](http://127.0.0.1:8848/nacos) ，进入 配置管理-配置列表 点击+号新建配置，这里创建个数据源配置例子: nacos-datasource.yaml

```java
spring:
    datasource:
     name: datasource
     url: jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=UTF-8&characterSetResults=UTF-8&zeroDateTimeBehavior=convertToNull&useDynamicCharsetInfo=false&useSSL=false
     username: root
     password: root
     driverClassName: com.mysql.jdbc.Driver
```

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3kow3r2tkj21680u0wgu.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3kow3r2tkj21680u0wgu.jpg)

## 添加依赖

配置创建好就可以在控制台 配置管理-配置列表中查看到。接下来演示下怎么在 Spring Boot 项目中获取到 Nacos 中的配置信息。

需要在项目中添加以下依赖:

```java
<!--配置中心 starter-->
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-config-spring-boot-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```

然后在项目中的 [application.properties](http://application.properties) 文件中添加 nacos 的一些配置：

```java
nacos.config.server-addr=127.0.0.1:8848
nacos.config.group=DEFAULT_GROUP
nacos.config.namespace=
nacos.config.username=nacos
nacos.config.password=nacos
```

## 获取配置

### 绑定到类获取

可以通过 @NacosConfigurationProperties 注解将 nacos-datasource.yaml 中的配置绑定到 NacosDataSourceConfig 类上。这样就可以通过 `@Resource` 或 `@Autowired` 将 NacosDataSourceConfig 注入到要使用的地方。

```java
@NacosConfigurationProperties(prefix = "spring.datasource", dataId = "nacos-datasource.yaml", autoRefreshed = true)
@Component
@Data
public class NacosDataSourceConfig {
    
    private String name;
    
    private String url;
    
    private String username;
    
    private String password;
    
    private String driverClassName;
}
```

创建一个 Controller ，写一个获取配置信息的接口：

```java
/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springboot/nacos/config")
public class NacosConfigController {
    
    @Resource
    private NacosDataSourceConfig nacosDataSourceConfig;
    
    @GetMapping(path = "get")
    private Map<String, String> getNacosDataSource() {
        Map<String, String> result = new HashMap<>();
        result.put("name", nacosDataSourceConfig.getName());
        result.put("url", nacosDataSourceConfig.getUrl());
        result.put("username", nacosDataSourceConfig.getUsername());
        result.put("password", nacosDataSourceConfig.getPassword());
        result.put("driverClassName", nacosDataSourceConfig.getDriverClassName());
        return result;
    }
}
```

然后启动服务，访问 [http://localhost:8080/springboot/nacos/config/binding/class/get](http://localhost:8080/springboot/nacos/config/binding/class/get) 就可以获取到对应的配置信息:

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3koknj05gj21zm0hujv1.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3koknj05gj21zm0hujv1.jpg)

### @NacosValue+@NacosPropertySource 注解获取

在创建一个 properties 格式的配置，演示下使用 `@NacosValue` + `@NacosPropertySource` 注解获取配置信息。还是打开  配置管理-配置列表 点击+号新建配置：nacos-datasource.properties

```java
name=datasource
url=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=UTF-8&characterSetResults=UTF-8&zeroDateTimeBehavior=convertToNull&useDynamicCharsetInfo=false&useSSL=false
username=root
password=root
driverClassName=com.mysql.jdbc.Driver
```

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3kounqdmsj216m0u0wgp.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3kounqdmsj216m0u0wgp.jpg)

通过  `@NacosValue` + `@NacosPropertySource` 注解获取指定 dataId 的配置

```java
/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springboot/nacos/config")
@NacosPropertySource(dataId = "nacos-datasource.properties", autoRefreshed = true, before = SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, after = SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)
public class AnnotationGetController {
    
    @NacosValue(value = "${name:}", autoRefreshed = true)
    private String name;
    
    @NacosValue(value = "${url:}", autoRefreshed = true)
    private String url;
    
    @NacosValue(value = "${username:}", autoRefreshed = true)
    private String username;
    
    @NacosValue(value = "${password:}", autoRefreshed = true)
    private String password;
    
    @NacosValue(value = "${driverClassName:}", autoRefreshed = true)
    private String driverClassName;
    
    @GetMapping(path = "annotation/get")
    private Map<String, String> getNacosDataSource2() {
        Map<String, String> result = new HashMap<>();
        result.put("name", name);
        result.put("url", url);
        result.put("username", username);
        result.put("password", password);
        result.put("driverClassName", driverClassName);
        return result;
    }
}
```

访问 [http://localhost:8080/springboot/nacos/config/annotation/get](http://localhost:8080/springboot/nacos/config/annotation/get) 获取 [nacos-datasource.properties](http://nacos-datasource.properties) 的配置信息：

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3koyy2gw9j21z00gu77r.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3koyy2gw9j21z00gu77r.jpg)

## 配置监听

Spring Boot 的使用方式也可以通过 `@NacosConfigListener` 注解进行配置变更的监听，在创建一个 hello-nacos.text 配置：

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3kpuvham9j219k0u0dhh.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3kpuvham9j219k0u0dhh.jpg)

```java
/**
 * @author lixiaoshuang
 */
@Component
public class ConfigListener {
    
    /**
     * 基于注解监听配置
     *
     * @param newContent
     * @throws Exception
     */
    @NacosConfigListener(dataId = "hello-nacos.text", timeout = 500)
    public void onChange(String newContent) {
        System.out.println("配置变更为 : \n" + newContent);
    }
    
}
```

然后在将 hello-nacos.text 的配置内容修改为 ” hello nacos config “，代码中就会回调 onChange() 方法

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3kpz4de68j210q0gomy4.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3kpz4de68j210q0gomy4.jpg)

# 服务注册&发现

## 服务注册

在项目中添加以下依赖：

```java
<!--注册中心 starter-->
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-discovery-spring-boot-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```

然后在项目中的 [application.properties](http://application.properties) 文件中添加 nacos 的一些配置：

```java
nacos.discovery.server-addr=127.0.0.1:8848
nacos.discovery.auto-register=true
nacos.discovery.register.clusterName=SPRINGBOOT
nacos.discovery.username=nacos
nacos.discovery.password=nacos

```

当添加完配置以后，并且开启了自动注册，启动服务以后看到下面这段日志，就说明服务注册成功了。

```java
Finished auto register service : SPRING_BOOT_SERVICE, ip : 192.168.1.8, port : 8222
```

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3kx59muggj22l30u04dq.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3kx59muggj22l30u04dq.jpg)

## 服务发现

可以使用 Nacos 提供的 **`NacosNamingService`** 来获取到服务的实例，可以通过 `@NacosInjected` 注解将 **`NacosNamingService`** 注入到需要使用的地方。

```java
/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springboot/nacos/discovery")
public class NacosDiscoveryController {
    
    @NacosInjected
    private NacosNamingService nacosNamingService;
    
    @RequestMapping(path = "get")
    public List<Instance> getInfo(@RequestParam("serviceName") String serviceName) throws NacosException {
        return nacosNamingService.getAllInstances(serviceName);
    }
}
```

通过调用 [http://localhost:8222/springboot/nacos/discovery/get?serviceName=SPRING_BOOT_SERVICE](http://localhost:8222/springboot/nacos/discovery/get?serviceName=SPRING_BOOT_SERVICE) 获取 `SPRING_BOOT_SERVICE` 服务的实例信息。

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3kxi7wg4nj21rq0r2aed.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3kxi7wg4nj21rq0r2aed.jpg)