package com.lidachui.simpleRpc.service;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * service.User
 *
 * @author: lihuijie
 * @date: 2024/8/26 9:51
 * @version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
  // 客户端和服务端共有的
  private Integer id;
  private String userName;
  private Boolean sex;
}
