package com.lidachui.simpleRpc.service;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Blog
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:11
 * @version: 1.0
 */
// pojo类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {
  private Integer id;
  private Integer useId;
  private String title;
}
