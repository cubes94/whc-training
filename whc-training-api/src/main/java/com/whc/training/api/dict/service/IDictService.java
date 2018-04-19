package com.whc.training.api.dict.service;

import com.whc.training.api.config.TrainingFeignConfiguration;
import com.whc.training.api.dict.domain.vo.DictAreaChildVO;
import com.whc.util.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典接口
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 11:28
 */
@FeignClient(name = "whc-training-service", configuration = TrainingFeignConfiguration.class)
public interface IDictService {

    String BASE_URL = "/api/dict";

    /**
     * 通过父级代码获取子节点信息（不包括父节点）
     *
     * @param parentCode 父级代码
     * @return 子节点信息
     */
    @RequestMapping(value = BASE_URL + "/getDictAreaChildrenByParentCode", method = RequestMethod.GET)
    Response<List<DictAreaChildVO>> getDictAreaChildrenByParentCode(@RequestParam("parentCode") String parentCode);

    /**
     * 通过父级代码获取子节点信息（包括父节点）
     *
     * @param parentCode 父级代码
     * @return 子节点信息
     */
    @RequestMapping(value = BASE_URL + "/getDictAreaChildrenWithParentByParentCode", method = RequestMethod.GET)
    Response<DictAreaChildVO> getDictAreaChildrenWithParentByParentCode(@RequestParam("parentCode") String parentCode);
}
