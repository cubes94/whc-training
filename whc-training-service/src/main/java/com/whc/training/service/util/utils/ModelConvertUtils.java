package com.whc.training.service.util.utils;

import com.whc.training.api.dict.domain.vo.DictAreaChildVO;
import com.whc.training.service.dict.domain.model.DictArea;
import com.whc.util.response.Response;

import java.util.ArrayList;

/**
 * 对象转换工具类
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月19 09:53
 */
public class ModelConvertUtils {

    /**
     * 转换相应对象类型
     *
     * @param response 相应对象
     * @param data     新数据
     * @param <T>      原数据类型
     * @param <F>      新数据类型
     * @return 新对象
     */
    public static <T, F> Response<F> convertToResponse(Response<T> response, F data) {
        Response<F> newResponse = new Response<>();
        newResponse.setCode(response.getCode());
        newResponse.setMessage(response.getMessage());
        newResponse.setData(data);
        return newResponse;
    }

    /**
     * 生成区域子节点实体
     *
     * @param dictArea 区域实体
     * @return 区域子节点实体
     */
    public static DictAreaChildVO convertToDictAreaChildVO(DictArea dictArea) {
        DictAreaChildVO dictAreaChildVO = new DictAreaChildVO();
        dictAreaChildVO.setCode(dictArea.getCode());
        dictAreaChildVO.setParentCode(dictArea.getParentCode());
        dictAreaChildVO.setName(dictArea.getName());
        dictAreaChildVO.setShortName(dictArea.getShortName());
        dictAreaChildVO.setLevel(dictArea.getLevel());
        dictAreaChildVO.setDictAreaChildList(new ArrayList<>());
        return dictAreaChildVO;
    }
}
