package com.whc.training.util.test.properties;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置信息（保密）
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年05月12 18:15
 */
public class LocalProperties extends Properties {

    private static final long serialVersionUID = -3998931695315409733L;

    private static LocalProperties localProperties = new LocalProperties();

    static {
        String localPath = "/data/whc/whc-training/local.properties";
        try {
            PropertiesLoaderUtils.fillProperties(localProperties, new FileUrlResource(localPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LocalProperties() {
    }

    public static LocalProperties getLocalProperties() {
        return localProperties;
    }
}
