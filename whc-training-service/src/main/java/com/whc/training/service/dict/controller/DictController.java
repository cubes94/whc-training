package com.whc.training.service.dict.controller;

import com.whc.training.api.dict.domain.vo.DictAreaChildVO;
import com.whc.training.api.dict.service.IDictService;
import com.whc.util.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典接口
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 11:38
 */
@Slf4j
@RestController
@Api(value = "字典接口", description = "字典接口")
public class DictController {

    @Autowired
    private IDictService dictService;

    /**
     * 通过父级代码获取子节点信息
     *
     * @param parentCode 父级代码
     * @return 子节点信息
     */
    @ApiOperation(value = "通过父级代码获取子节点信息", notes = "通过父级代码获取子节点信息",
            httpMethod = "GET", consumes = "application/json; charset=utf-8", position = 2)
    @ApiResponse(response = Response.class, code = 200, message = "成功")
    @RequestMapping(value = IDictService.BASE_URL + "/getDictAreaChildrenByParentCode", method = RequestMethod.GET)
    public Response<List<DictAreaChildVO>> getDictAreaChildrenByParentCode(
            @RequestParam("parentCode") String parentCode) {
        try {
            return dictService.getDictAreaChildrenByParentCode(parentCode);
        } catch (Exception e) {
            log.error("通过父级代码获取子节点信息时发生异常", e);
            return Response.fail("通过父级代码获取子节点信息时发生异常");
        }
    }


    /**
     * 通过父级代码获取子节点信息（包括父节点）
     *
     * @param parentCode 父级代码
     * @return 子节点信息
     */
    @ApiOperation(value = "通过父级代码获取子节点信息（包括父节点）", notes = "通过父级代码获取子节点信息（包括父节点）",
            httpMethod = "GET", consumes = "application/json; charset=utf-8", position = 2)
    @ApiResponse(response = Response.class, code = 200, message = "成功")
    @RequestMapping(value = IDictService.BASE_URL + "/getDictAreaChildrenWithParentByParentCode", method = RequestMethod.GET)
    public Response<DictAreaChildVO> getDictAreaChildrenWithParentByParentCode(@RequestParam("parentCode") String parentCode) {
        try {
            return dictService.getDictAreaChildrenWithParentByParentCode(parentCode);
        } catch (Exception e) {
            log.error("通过父级代码获取子节点信息时发生异常", e);
            return Response.fail("通过父级代码获取子节点信息时发生异常");
        }
    }
}
