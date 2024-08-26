package com.lidachui.simpleRpc.service;

/**
 * BlogServiceImpl
 *
 * @author: lihuijie
 * @date: 2024/8/26 10:11
 * @version: 1.0
 */
public class BlogServiceImpl implements BlogService {
  @Override
  public Blog getBlogById(Integer id) {
    Blog blog = Blog.builder().id(id).title("我的博客").useId(22).build();
    System.out.println("客户端查询了"+id+"博客");
    return blog;
  }
}
