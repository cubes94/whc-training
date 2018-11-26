package com.whc.util.http;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP公共接口
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年09月26 16:55
 */
public class HttpUtils {

    /**
     * 根据url下载文件
     *
     * @param policyUrl 电子保单地址
     * @return 流
     * @throws IOException IO异常
     */
    public static InputStream getFileInputStreamByUrl(String policyUrl) throws IOException {
        URL url = new URL(policyUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        return conn.getInputStream();
    }

    /**
     * 根据url下载文件
     *
     * @param policyUrl 电子保单地址
     * @return 流
     * @throws IOException IO异常
     */
    public static byte[] getFileBytesByUrl(String policyUrl) throws IOException {
        InputStream inputStream = getFileInputStreamByUrl(policyUrl);
        return StreamUtils.copyToByteArray(inputStream);
    }
}
