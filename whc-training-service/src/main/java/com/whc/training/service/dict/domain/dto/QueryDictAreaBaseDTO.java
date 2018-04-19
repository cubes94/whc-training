package com.whc.training.service.dict.domain.dto;

import com.whc.training.api.util.enums.DictAreaEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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
@Api(value = "queryDictAreaBaseDTO", description = "区域查询接口")
public class QueryDictAreaBaseDTO implements Serializable {

    private static final long serialVersionUID = -1091143241662864161L;

    @ApiModelProperty(name = "codeList", value = "代码集合")
    private List<String> codeList;

    @ApiModelProperty(name = "parentCode", value = "父级代码")
    private String parentCode;

    @ApiModelProperty(name = "parentCodeList", value = "父级代码集合")
    private List<String> parentCodeList;

    @ApiModelProperty(name = "level", value = "级别（0-全国，1-省，2-市，3-区县）")
    private Integer level;

    @ApiModelProperty(name = "status", value = "状态（1-有效，0-无效，-1-已删除）")
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
