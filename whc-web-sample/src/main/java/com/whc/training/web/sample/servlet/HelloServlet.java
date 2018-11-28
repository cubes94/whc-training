package com.whc.training.web.sample.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet是一个运行在Web服务器中的Java小程序，会接收和响应来自Web客户端的请求，使用HTTP(超文本传输协议)进行通信
 *
 * @author whc
 * @version 1.0.0
 * @since 2018年11月26 15:03
 */
@Slf4j
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1189096371237969368L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Hello World!");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        log.info("Servlet: {} has started!", this.getServletName());
    }

    @Override
    public void destroy() {
        super.destroy();
        log.info("Servlet: {} has stopped!", this.getServletName());
    }
}
