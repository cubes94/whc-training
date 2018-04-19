package com.whc.training.service.dict.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whc.training.api.util.enums.DictAreaEnum;
import com.whc.training.service.dict.domain.model.DictArea;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 区域vo
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 10:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictAreaVO extends DictArea {

    private static final long serialVersionUID = -1657337089089475452L;

    /**
     * 级别名称
     */
    private String levelName;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 级别名称
     *
     * @return 级别名称
     */
    public String getLevelName() {
        return DictAreaEnum.Level.getNameByCode(super.getLevel());
    }

    /**
     * 状态名称
     *
     * @return 状态名称
     */
    public String getStatusName() {
        return DictAreaEnum.Status.getNameByCode(super.getStatus());
    }

    /**
     * 创建时间
     */
    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    /**
     * 更新时间
     */
    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdateTime() {
        return super.getUpdateTime();
    }
}
