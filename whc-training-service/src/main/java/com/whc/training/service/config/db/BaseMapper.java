package com.whc.training.service.config.db;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 *
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 14:37
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}

