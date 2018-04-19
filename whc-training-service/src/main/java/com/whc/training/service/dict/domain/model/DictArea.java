package com.whc.training.service.dict.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * 区域实体类
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月16 18:04
 */
@Data
@Table(name = "dict_area")
public class DictArea implements Serializable {

    private static final long serialVersionUID = 2644193929764359619L;

    /**
     * 代码
     */
    private String code;

    /**
     * 父级代码
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    @Column(name = "short_name")
    private String shortName;

    /**
     * 级别（0-全国，1-省，2-市，3-区县）
     */
    private Integer level;

    /**
     * 状态（1-有效，0-无效，-1-已删除）
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建者ID
     */
    @Column(name = "create_user_id")
    private String createUserId;

    /**
     * 创建者名称
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新者ID
     */
    @Column(name = "update_user_id")
    private String updateUserId;

    /**
     * 更新者名称
     */
    @Column(name = "update_user_name")
    private String updateUserName;
}