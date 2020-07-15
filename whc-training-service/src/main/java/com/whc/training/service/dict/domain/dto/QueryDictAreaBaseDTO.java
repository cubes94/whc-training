package com.whc.training.service.dict.domain.dto;

import com.whc.training.api.util.enums.DictAreaEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 区域查询接口
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 09:26
 */
@Data
public class QueryDictAreaBaseDTO implements Serializable {

    private static final long serialVersionUID = -1091143241662864161L;

    /**
     * 代码集合
     */
    private List<String> codeList;

    /**
     * 父级代码
     */
    private String parentCode;

    /**
     * 父级代码集合
     */
    private List<String> parentCodeList;

    /**
     * 级别（0-全国，1-省，2-市，3-区县）
     */
    private Integer level;

    /**
     * 状态（1-有效，0-无效，-1-已删除）
     */
    private Integer status;

    public QueryDictAreaBaseDTO() {
    }

    public QueryDictAreaBaseDTO(QueryDictAreaDTO queryDictAreaDTO) {
        this.codeList = queryDictAreaDTO.getCodeList();
        this.parentCode = queryDictAreaDTO.getParentCode();
        this.parentCodeList = queryDictAreaDTO.getParentCodeList();
        this.level = queryDictAreaDTO.getLevel();
        this.status = DictAreaEnum.Status.VALID.getCode();
    }
}
