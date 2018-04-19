package com.whc;

import com.whc.training.service.TrainingServiceApp;
import com.whc.training.service.config.db.properties.PrimaryDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
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
    private PrimaryDataSourceProperties primaryDataSourceProperties;

    @Autowired
    private DataSource primaryDataSource;

    @Test
    public void test() {
        log.info("adwa");
    }
}
