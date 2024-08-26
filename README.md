
---

# SimpleRPC 框架

`SimpleRPC` 是一个轻量级的 RPC 框架，旨在简化 Java 应用程序中的远程过程调用。它基于 Netty 实现高效的网络通信，支持客户端和服务端的自动配置和注入，简化了分布式系统中的服务调用和管理。

## 主要特性

- **基于 Netty 的高性能 RPC 通信**
- **自动化客户端和服务端配置**
- **支持通过注解声明服务和引用**
- **灵活的服务注册和发现机制**

## 快速开始

### 1. 添加依赖

在你的 Maven 项目的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>com.lidachui</groupId>
    <artifactId>simple-rpc</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置服务端

在你的 Spring Boot 应用的主类上使用 `@RpcServer` 注解，并指定服务端端口：

```java
import com.lidachui.simpleRpc.annotation.RpcServer;

@RpcServer(port = 9090)
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### 3. 配置客户端

在你的 Spring Boot 应用的主类上使用 `@RpcClient` 注解，配置 RPC 客户端连接的主机和端口：

```java
import com.lidachui.simpleRpc.annotation.RpcClient;

@RpcClient(host = "localhost", port = 9090)
public class MyClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyClientApplication.class, args);
    }
}
```

### 4. 声明 RPC 服务

在你的服务实现类上使用 `@RpcService` 注解，标记为 RPC 服务接口：

```java
import com.lidachui.simpleRpc.annotation.RpcService;

@RpcService
public class MyServiceImpl implements MyService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
```

### 5. 引用 RPC 服务

在需要使用 RPC 服务的类中，使用 `@RpcReference` 注解注入服务：

```java
import com.lidachui.simpleRpc.annotation.RpcReference;

public class MyController {
    @RpcReference
    private MyService myService;

    public void someMethod() {
        String greeting = myService.sayHello("World");
        System.out.println(greeting);
    }
}
```

## 组件说明

### `@RpcServer`

用于标记 RPC 服务端的配置，指定服务端的端口号。例如：

```java
@RpcServer(port = 9090)
public class MyApplication {
    //...
}
```

### `@RpcClient`

用于标记 RPC 客户端的配置，指定服务端的主机和端口。例如：

```java
@RpcClient(host = "localhost", port = 9090)
public class MyClientApplication {
    //...
}
```

### `@RpcService`

用于标记一个类为 RPC 服务实现。RPC 框架会将这些服务提供给客户端调用。例如：

```java
@RpcService
public class MyServiceImpl implements MyService {
    //...
}
```

### `@RpcReference`

用于在客户端注入远程服务引用，允许客户端像调用本地方法一样调用远程方法。例如：

```java
@RpcReference
private MyService myService;
```

## 配置项

### `RpcServerProperties`

- `rpc.server.host`：指定服务端的主机（默认值为 `localhost`）。
- `rpc.server.port`：指定服务端的端口（默认值为 `9090`）。

### 示例配置

```properties
rpc.server.host=localhost
rpc.server.port=9090
```

## 贡献

欢迎贡献代码和提出改进建议。请遵循 GitHub 上的贡献指南和代码规范。

