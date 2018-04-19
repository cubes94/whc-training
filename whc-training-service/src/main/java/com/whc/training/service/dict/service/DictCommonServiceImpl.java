package com.whc.training.service.dict.service;

import com.whc.training.service.dict.dao.DictAreaMapper;
import com.whc.training.service.dict.domain.dto.QueryDictAreaBaseDTO;
import com.whc.training.service.dict.domain.dto.QueryDictAreaDTO;
import com.whc.training.service.dict.domain.vo.DictAreaVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典接口
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 09:38
 */
@Slf4j
@Service
public class DictCommonServiceImpl {

    @Autowired
    private DictAreaMapper dictAreaMapper;


    /**
     * 根据代码获取区域
     *
     * @param code 代码
     * @return 区域
     */
    public DictAreaVO getDictAreaByCode(String code) {
        return dictAreaMapper.getDictAreaByCode(code);
    }

    /**
     * 区域集合查询
     *
     * @param queryDictAreaDTO 查询条件
     * @return 查询结果
     */
    public List<DictAreaVO> listDictArea(QueryDictAreaDTO queryDictAreaDTO) {
        return dictAreaMapper.listDictArea(new QueryDictAreaBaseDTO(queryDictAreaDTO));
    }

    /**
     * 根据父级代码查询区域集合
     *
     * @param parentCode 父级代码
     * @return 区域集合
     */
    public List<DictAreaVO> listDictAreaByParentCode(String parentCode) {
        QueryDictAreaDTO queryDictAreaDTO = new QueryDictAreaDTO();
        queryDictAreaDTO.setParentCode(parentCode);
        return this.listDictArea(queryDictAreaDTO);
    }
}
