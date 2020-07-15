package com.whc;

import com.whc.training.service.TrainingServiceApp;
import com.whc.training.service.dict.dao.DictAreaMapper;
import com.whc.training.service.dict.domain.vo.DictAreaVO;
import com.whc.training.service.dict.service.DictCommonServiceImpl;
import com.whc.training.service.dict.service.DictServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

/**
 * 业务测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年04月17 10:40
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TrainingServiceApp.class)
public class TrainingServiceAppTest {

    @Autowired
    private DataSource primaryDataSource;

    @Autowired
    private DictServiceImpl dictService;

    @Autowired
    private DictCommonServiceImpl dictCommonService;

    static final String BEI_JING_CODE = "110000";

    @Autowired
    private DictAreaMapper dictAreaMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void test() {
        final DictAreaVO a1 = dictAreaMapper.getDictAreaByCode(BEI_JING_CODE);
        final DictAreaVO a2 = dictAreaMapper.getDictAreaByCode(BEI_JING_CODE);

        final DictAreaVO area1 = sqlSessionFactory.openSession()
                .getMapper(DictAreaMapper.class)
                .getDictAreaByCode(BEI_JING_CODE);
        final DictAreaVO area2 = sqlSessionFactory.openSession()
                .getMapper(DictAreaMapper.class)
                .getDictAreaByCode(BEI_JING_CODE);
    }

}
