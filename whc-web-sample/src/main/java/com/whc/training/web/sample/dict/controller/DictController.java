package com.whc.training.web.sample.dict.controller;

import com.whc.training.web.sample.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 字典接口
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年11月26 17:45
 */
@Controller
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @RequestMapping(value = "/toDictPage", method = RequestMethod.GET)
    public String toDictPage() {
        return "/";
    }
}
