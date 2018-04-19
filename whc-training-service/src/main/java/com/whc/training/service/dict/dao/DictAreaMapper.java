package com.whc.training.service.dict.dao;

import com.whc.training.service.dict.domain.dto.QueryDictAreaBaseDTO;
import com.whc.training.service.dict.domain.model.DictArea;
import com.whc.training.service.dict.domain.vo.DictAreaVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 区域dao层
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 18:04
 */
@Component
public interface DictAreaMapper extends Mapper<DictArea> {

    /**
     * 根据代码获取区域
     *
     * @param code 代码
     * @return 区域
     */
    DictAreaVO getDictAreaByCode(@Param("code") String code);

    /**
     * 区域集合查询
     *
     * @param queryDictAreaDTO 查询条件
     * @return 查询结果
     */
    List<DictAreaVO> listDictArea(QueryDictAreaBaseDTO queryDictAreaDTO);
}