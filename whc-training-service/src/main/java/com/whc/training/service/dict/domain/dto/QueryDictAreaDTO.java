package com.whc.training.service.dict.domain.dto;

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
public class QueryDictAreaDTO implements Serializable {

    private static final long serialVersionUID = -7366297932842228015L;

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

}
