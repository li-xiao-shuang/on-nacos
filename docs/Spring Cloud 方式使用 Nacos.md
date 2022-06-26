本文介绍下如何在 Spring Cloud 项目中使用 Nacos，Nacos 主要分为两个部分，配置中心和服务注册与发现。在使用 Spring Cloud 项目中使用 Nacos ，首先要保证启动一个 Nacos 服务，具体可以参考《[快速上手 Nacos](https://github.com/li-xiao-shuang/on-nacos/blob/master/docs/%E5%BF%AB%E9%80%9F%E4%B8%8A%E6%89%8B%20Nacos.md)》来搭建一个单机的 Nacos 服务。

Nacos 接入 Spring Cloud 的源代码可以参考 **[spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba)** 这个项目，感兴趣的朋友可以查看源代码。Spring Cloud Alibaba 的版本对应关系可以参考：[版本说明 Wiki](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

> 本篇文章的详细的代码示例点击[【nacos-spring-cloud】](https://github.com/li-xiao-shuang/on-nacos/tree/master/nacos-spring-cloud)查看

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

配置创建好就可以在控制台 配置管理-配置列表中查看到。接下来演示下怎么在 Spring Cloud 项目中获取到 Nacos 中的配置信息。

需要在项目中添加以下依赖:

```java
<!--配置中心-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>${latest.version}</version>
</dependency>
```

如果项目的 Spring Boot 版本 小于 2.4.0 需要在项目中创建 [bootstrap.properties](http://bootstrap.properties) 然后在项目中的 [bootstrap.properties](http://bootstrap.properties) 文件中添加 nacos 的一些配置：

```java
spring.cloud.nacos.config.server-addr=127.0.0.1:8848
spring.cloud.nacos.config.name=nacos-datasource
spring.cloud.nacos.config.file-extension=yaml
spring.cloud.nacos.config.username=nacos
spring.cloud.nacos.config.password=nacos
```

> Spring Boot 2.4.0 版本开始默认不启动 bootstrap 容器 本项目演示如何在 Spring boot < 2.4.0 版本使用 nacos。如果 Spring Boot 版本 ≥ 2.4.0 可以参考 ****[Nacos Config 2.4.x Example](https://github.com/alibaba/spring-cloud-alibaba/blob/2021.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-2.4.x-example/readme-zh.md)** 进行配置
>

在 SpringCloud Alibaba 中，Nacos dataId 的拼接格式为：

```java
${prefix} - ${spring.profiles.active} . ${file-extension}
```

- prefix`默认为`spring.application.name`的值，也可以通过配置项` @spring.cloud.nacos.config.prefix`来配置。
- `spring.profiles.active`即为当前环境对应的 profile，详情可以参考 [Spring Boot文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)

  > 注意，当 active profile 为空时，对应的连接符`-`也将不存在，dataId 的拼接格式变成`${prefix}`.`${file-extension}`
>
- `file-extension`为配置内容的数据格式，可以通过配置项`spring.cloud.nacos.config.file-extension`来配置。

## 获取配置

### @Value 注解获取配置

```java
/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springcloud/nacos/config")
public class AnnotationGetController {
    
    @Value(value = "${spring.datasource.name:}")
    private String name;
    
    @Value(value = "${spring.datasource.url:}")
    private String url;
    
    @Value(value = "${spring.datasource.username:}")
    private String username;
    
    @Value(value = "${spring.datasource.password:}")
    private String password;
    
    @Value(value = "${spring.datasource.driverClassName:}")
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

启动服务后，通过访问 [http://localhost:8333/springcloud/nacos/config/annotation/get](http://localhost:8333/springcloud/nacos/config/annotation/get) 获取配置信息

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3lzju1ml3j21zq0hoadk.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3lzju1ml3j21zq0hoadk.jpg)

### @ConfigurationProperties 注解绑定配置到类

```java
/**
 * @author lixiaoshuang
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
@Component
public class NacosDataSourceConfig {
    
    private String name;
    
    private String url;
    
    private String username;
    
    private String password;
    
    private String driverClassName;
}

/**
 * @author lixiaoshuang
 */
@RestController
@RequestMapping(path = "springcloud/nacos/config")
public class BindingClassController {
    
    @Resource
    private NacosDataSourceConfig nacosDataSourceConfig;
    
    
    @GetMapping(path = "binding/class/get")
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

启动服务后，通过访问 [http://localhost:8333/springcloud/nacos/config/binding/class/get](http://localhost:8333/springcloud/nacos/config/binding/class/get) 获取配置信息

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3lzqevse3j21za0ey77u.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3lzqevse3j21za0ey77u.jpg)

# 服务注册&发现

## 服务注册

在项目中添加以下依赖：

```java
<!--注册中心-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>${latest.version}</version>
</dependency>
```

然后在项目中的 [application.properties](http://application.properties) 文件中添加 nacos 的一些配置：

```java
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
spring.cloud.nacos.discovery.username=naocs
spring.cloud.nacos.discovery.password=nacos
```

当添加完配置启动服务以后看到下面这段日志，就说明服务注册成功了。

```java
nacos registry, DEFAULT_GROUP SPRING-CLOUD-CONFIG 192.168.1.8:8444 register finished
```

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3m1kf4qllj228v0u0naz.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3m1kf4qllj228v0u0naz.jpg)

## 服务发现

### 整合 @LoadBalanced RestTemplate

首先模拟一个订单服务的接口，可以通过服务发现的方式调用此接口

```java
/**
 * @author lixiaoshuang
 */
@RestController
public class OrderServiceController {
    
    @GetMapping("/order/{orderid}")
    public String echo(@PathVariable String orderid) {
        System.out.println("接到远程调用订单服务请求");
        return "[ORDER] : " + "订单id：[" + orderid + "] 的订单信息";
    }
}
```

先访问下 [http://localhost:8444/order/1234](http://localhost:8444/order/1234) 获取订单id 为 1234 的订单信息，看接口是否能调通：

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3m2ii2lp6j20yy07ydgc.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3m2ii2lp6j20yy07ydgc.jpg)

接下来在模拟一个业务接口，调用订单接口。这里通过使用 @LoadBalanced 注解使 RestTemplate 具备负载均衡和服务发现的能力，这样就可以通过指定服务名调用。

```java
/**
 * @author lixiaoshuang
 */
@RestController
public class RestTemplateController {
    
    @Autowired
    public RestTemplate restTemplate;
    
    @Bean
    @LoadBalanced
    public RestTemplate RestTemplate() {
        return new RestTemplate();
    }
    
    @GetMapping("/call/order/{orderid}")
    public String callEcho(@PathVariable String orderid) {
        // 访问应用 SPRING-CLOUD-CONFIG 的 REST "/order/{orderid}"
        return restTemplate.getForObject("http://SPRING-CLOUD-CONFIG/order/" + orderid, String.class);
    }
}
```

访问接口 [http://localhost:8444/call/order/1234](http://localhost:8444/call/order/1234) 获取订单 1234 的订单信息，内部将通过服务名 SPRING-CLOUD-CONFIG 发现服务，并调用 /order/{orderid} 接口获取订单信息。

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3m2wozka4j20xi0akt98.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3m2wozka4j20xi0akt98.jpg)

观察 order 服务的日志会发现有远程调用的日志

![https://tva1.sinaimg.cn/large/e6c9d24ely1h3m2xxo8wcj218c0hwmy7.jpg](https://tva1.sinaimg.cn/large/e6c9d24ely1h3m2xxo8wcj218c0hwmy7.jpg)