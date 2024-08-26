package com.lidachui.simpleRpc.service;

/**
 * service.UserService
 *
 * @author: lihuijie
 * @date: 2024/8/26 9:53
 * @version: 1.0
 */
public interface UserService {
  // 客户端通过这个接口调用服务端的实现类
  User getUserByUserId(Integer id);

  // 给这个服务增加一个功能
  Integer insertUserId(User user);
}
