package com.whc;

import com.whc.training.api.dict.domain.vo.DictAreaChildVO;
import com.whc.training.api.dict.service.IDictService;
import com.whc.training.api.util.constants.Constants;
import com.whc.training.app.TrainingAppApp;
import com.whc.util.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 业务测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月18 19:21
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrainingAppApp.class)
public class TrainingAppAppTest {

    @Autowired
    private IDictService dictService;

    @Test
    public void test() {
        Response<List<DictAreaChildVO>> response = dictService.getDictAreaChildrenByParentCode(Constants.AREA_COUNTRY_CODE);
        System.out.println(response);
    }
}
