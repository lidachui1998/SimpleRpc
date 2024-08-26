package com.lidachui.simpleRpc.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc 请求 RpcRequest
 *
 * @author: lihuijie
 * @date: 2024/8/26 16:31
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest {

  /** 服务类名，客户端只知道接口名，在服务端中用接口名指向实现类 */
  private String interfaceName;

  /** 方法名称 */
  private String methodName;

  /** 参数列表 */
  private Object[] params;

  /** 参数类型 */
  private Class<?>[] paramsTypes;
}
