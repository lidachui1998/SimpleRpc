package com.lidachui.simpleRpc.core;

/**
 * ServiceProvider
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:26
 * @version: 1.0
 */

import com.lidachui.simpleRpc.annotation.RpcService;

import java.util.HashMap;
import java.util.Map;

/**
 * 之前这里使用Map简单实现的
 * 存放服务接口名与服务端对应的实现类
 * 服务启动时要暴露其相关的实现类0
 * 根据request中的interface调用服务端中相关实现类
 */
public class ServiceProvider {
  /**
   * 存储接口名与实现类对象的映射
   */
  private final Map<String, Object> interfaceProvider;

  public ServiceProvider() {
    this.interfaceProvider = new HashMap<>();
  }

  /**
   * 注册服务实现类，支持一个实现类实现多个接口
   * @param service 服务实现类对象
   */
  public void provideServiceInterface(Object service) {
    Class<?>[] interfaces = service.getClass().getInterfaces();

    for (Class<?> iface : interfaces) {
      if (iface.isAnnotationPresent(RpcService.class)) {
        // 将接口名作为 key，服务实现类对象作为 value 存储
        interfaceProvider.put(iface.getName(), service);
      }
    }
  }

  /**
   * 根据接口名获取对应的服务实现类
   * @param interfaceName 接口名
   * @return 服务实现类对象
   */
  public Object getService(String interfaceName) {
    return interfaceProvider.get(interfaceName);
  }
}
