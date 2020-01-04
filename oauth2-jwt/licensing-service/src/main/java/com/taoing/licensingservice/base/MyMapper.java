package com.taoing.licensingservice.base;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 自定义通用mapper
 *
 * @param <T>
 */
public interface MyMapper<T> extends Mapper<T>, InsertListMapper {
}
