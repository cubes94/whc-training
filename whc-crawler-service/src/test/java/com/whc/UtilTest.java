package com.whc;

import com.whc.util.http.HttpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工具类测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年09月26 17:35
 */
public class UtilTest {

    private final ExecutorService exe = Executors.newFixedThreadPool(4);

    private final String rootPath = "/data/webmagic/";

    @Test
    public void testEasyPageProcessor() throws Exception {

        String firstUrl = "https://www.douban.com/note/321027147/";

        PageProcessor pageProcessor = new PageProcessor() {

            @Override
            public void process(Page page) {
                page.addTargetRequests(page.getHtml().links().regex("(https://www\\.douban\\.com/[\\w\\-]+/[\\w\\-]+)").all());
                page.addTargetRequests(page.getHtml().links().regex("(https://www\\.douban\\.com/[\\w\\-])").all());
                List<Map<String, String>> imgs = new ArrayList<>();
                page.getHtml().xpath("//img").nodes().forEach(node -> {
                    String link = node.xpath("//img//@src").toString();
                    if (link.endsWith(".webp")) {
                        Map<String, String> img = new HashMap<>();
                        img.put("link", link);
                        img.put("name", link.split("/")[link.split("/").length - 1]);
                        imgs.add(img);
                    }
                });
                page.putField("imgs", imgs);
            }

            @Override
            public Site getSite() {
                return Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setCharset("UTF-8");
            }
        };

        Spider.create(pageProcessor).addUrl(firstUrl).thread(7).setPipelines(Collections.singletonList((resultItems, task) -> {
            List<Map<String, String>> imgs = resultItems.get("imgs");
            if (CollectionUtils.isNotEmpty(imgs)) {
                for (Map<String, String> img : imgs) {
                    String path = rootPath + task.getSite().getDomain() + File.separator;
                    exe.execute(() -> {
                        try {
                            byte[] bytes = HttpUtils.getFileBytesByUrl(img.get("link"));
                            FileUtils.writeByteArrayToFile(new File(path + img.get("name")), bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        })).run();
    }
}
