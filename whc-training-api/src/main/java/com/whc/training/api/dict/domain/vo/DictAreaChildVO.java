package com.whc.training.api.dict.domain.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 区域子节点
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 18:04
 */
@Data
@Api(value = "dictAreaChildVO", description = "区域子节点")
public class DictAreaChildVO implements Serializable {

    private static final long serialVersionUID = -5618364156433167710L;

    @ApiModelProperty(name = "code", value = "代码")
    private String code;

    @ApiModelProperty(name = "parentCode", value = "父级代码")
    private String parentCode;

    @ApiModelProperty(name = "name", value = "名称")
    private String name;

    @ApiModelProperty(name = "shortName", value = "简称")
    private String shortName;

    @ApiModelProperty(name = "level", value = "级别（0-全国，1-省，2-市，3-区县）")
    private Integer level;

    @ApiModelProperty(name = "dictAreaChildList", value = "区域子节点集合")
    private List<DictAreaChildVO> dictAreaChildList;

    /**
     * 根据子代码查询子级别
     *
     * @param childCode 子代码
     * @return 子级别
     */
    public DictAreaChildVO getDictAreaChildByChildCode(String childCode) {
        if (!CollectionUtils.isEmpty(dictAreaChildList)) {
            for (DictAreaChildVO dictAreaChildVO : dictAreaChildList) {
                if (Objects.equals(dictAreaChildVO.getCode(), childCode)) {
                    return dictAreaChildVO;
                }
            }
        }
        return null;
    }
}
