package com.whc.util.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应对象
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 15:53
 */
@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -2113844745886695952L;

    @ApiModelProperty(value = "成功标志", required = true)
    private static final String SUCCESS_CODE = "1";

    @ApiModelProperty(value = "失败标志", required = true)
    private static final String FAIL_CODE = "0";

    @ApiModelProperty(value = "标志码1:成功,0:失败", required = true)
    private String code;

    @ApiModelProperty(value = "说明", required = true)
    private String message;

    @ApiModelProperty(value = "数据", required = true)
    private T data;

    /**
     * 无参构造方法
     */
    public Response() {
        this.code = SUCCESS_CODE;
        this.message = "success";
        this.data = null;
    }

    /**
     * 构造方法
     *
     * @param code    标志
     * @param message 说明
     * @param data    数据
     */
    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造方法
     *
     * @param data 数据
     */
    public Response(T data) {
        this.code = SUCCESS_CODE;
        this.message = "success";
        this.data = data;
    }

    /**
     * 构造器
     *
     * @param code    标志
     * @param message 消息说明
     * @param data    数据
     * @return 结果
     */
    public static <T> Response<T> build(String code, String message, T data) {
        return new Response<>(code, message, data);
    }

    /**
     * 成功响应构造
     *
     * @param data 响应数据
     * @return 结果
     */
    public static <T> Response<T> ok(T data) {
        return new Response<>(data);
    }

    /**
     * 空的成功响应方法
     *
     * @return 结果
     */
    public static <T> Response<T> ok() {
        return new Response<>(null);
    }

    /**
     * 空的成功响应方法,待消息
     *
     * @param message 消息
     * @param <T>     泛型
     * @return 结果
     */
    public static <T> Response<T> okMessage(String message) {
        return new Response<T>(SUCCESS_CODE, message, null);
    }

    /**
     * 构造器
     *
     * @param code    标志
     * @param message 说明
     * @return 结果
     */
    public static <T> Response<T> build(String code, String message) {
        return new Response<>(code, message, null);
    }

    /**
     * 失败结果构造器
     *
     * @param message 说明
     * @return 结果
     */
    public static <T> Response<T> fail(String message) {

        return build(FAIL_CODE, message);
    }

    /**
     * 失败结果构造器
     *
     * @param message 说明
     * @param data    数据
     * @return 结果
     */
    public static <T> Response<T> fail(String message, T data) {

        return build(FAIL_CODE, message, data);
    }

    /**
     * 验证结果是否成功
     *
     * @return 是否成功
     */
    public Boolean checkSuccess() {
        return this.code.equals(SUCCESS_CODE);
    }

    /**
     * 验证结果是否挫败
     *
     * @return 是否成功
     */
    public Boolean checkFail() {
        return this.code.equals(FAIL_CODE);
    }
}
