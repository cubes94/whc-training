package com.whc.training.service.dict.service;

import com.whc.training.api.dict.domain.vo.DictAreaChildVO;
import com.whc.training.api.dict.service.IDictService;
import com.whc.training.api.util.enums.DictAreaEnum;
import com.whc.training.service.dict.domain.dto.QueryDictAreaDTO;
import com.whc.training.service.dict.domain.vo.DictAreaVO;
import com.whc.training.service.util.utils.ModelConvertUtils;
import com.whc.util.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典接口
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 11:37
 */
@Slf4j
@Service
public class DictServiceImpl implements IDictService {

    @Autowired
    private DictCommonServiceImpl dictCommonService;

    /**
     * 通过父级代码获取子节点信息
     *
     * @param parentCode 父级代码
     * @return 子节点信息
     */
    @Override
    public Response<List<DictAreaChildVO>> getDictAreaChildrenByParentCode(String parentCode) {
        Response<DictAreaChildVO> response = this.getDictAreaChildrenWithParentByParentCode(parentCode);
        return ModelConvertUtils.convertToResponse(response,
                response.checkSuccess() ? response.getData().getDictAreaChildList() : null);
    }

    /**
     * 通过父级代码获取子节点信息（包括父节点）
     *
     * @param parentCode 父级代码
     * @return 子节点信息
     */
    @Override
    public Response<DictAreaChildVO> getDictAreaChildrenWithParentByParentCode(String parentCode) {
        // 获取当前节点信息
        DictAreaVO dictAreaVO = dictCommonService.getDictAreaByCode(parentCode);
        if (dictAreaVO == null) {
            return Response.fail("参数有误");
        }
        DictAreaChildVO dictAreaChildVO = ModelConvertUtils.convertToDictAreaChildVO(dictAreaVO);
        // 操作级别
        DictAreaEnum.Level level = DictAreaEnum.Level.getObjByCode(dictAreaChildVO.getLevel());
        if (level == null) {
            return Response.fail("参数有误");
        }
        // 当前处理信息
        Map<String, DictAreaChildVO> codeDictAreaChildMap = new HashMap<>();
        codeDictAreaChildMap.put(dictAreaChildVO.getCode(), dictAreaChildVO);
        // 补充子节点数据
        while (level != null && DictAreaEnum.Level.DISTRICT.lowerThan(level) && !codeDictAreaChildMap.isEmpty()) {
            QueryDictAreaDTO queryDictAreaDTO = new QueryDictAreaDTO();
            queryDictAreaDTO.setParentCodeList(new ArrayList<>(codeDictAreaChildMap.keySet()));
            List<DictAreaVO> dictAreaList = dictCommonService.listDictArea(queryDictAreaDTO);
            Map<String, DictAreaChildVO> codeDictAreaChildTempMap = new HashMap<>();
            for (DictAreaVO dictArea : dictAreaList) {
                DictAreaChildVO dictAreaChild = ModelConvertUtils.convertToDictAreaChildVO(dictArea);
                codeDictAreaChildMap.get(dictArea.getParentCode()).getDictAreaChildList().add(dictAreaChild);
                codeDictAreaChildTempMap.put(dictAreaChild.getCode(), dictAreaChild);
            }
            codeDictAreaChildMap = codeDictAreaChildTempMap;
            level = level.getNextLevel();
        }

        return Response.ok(dictAreaChildVO);
    }
}
