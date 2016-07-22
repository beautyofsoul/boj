package org.yq.boj.servlet.simple;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created on 2016/7/17.
 */
@WebServlet(urlPatterns = {"/asyncServlet"}, asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        final AsyncContext acontext = req.startAsync();
        acontext.start(new Runnable() {
            @Override
            public void run() {
                String param = acontext.getRequest().getParameter("param");
                String result = "OK";
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    acontext.getResponse().getWriter().print(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                acontext.complete();
            }
        });
    }
}
