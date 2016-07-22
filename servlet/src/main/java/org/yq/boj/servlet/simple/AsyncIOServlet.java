package org.yq.boj.servlet.simple;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 使用非阻塞I/O读取大HTTP POST 请求
 * Created on 2016/7/17.
 *
 */
@WebServlet(urlPatterns = {"/asyncioServlet"},asyncSupported = true)
public class AsyncIOServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final AsyncContext acontext = req.startAsync();
        final ServletInputStream input = req.getInputStream();

        input.setReadListener(new ReadListener() {
            byte buffer[] = new byte[4*1024];
            StringBuilder sb = new StringBuilder();
            @Override
            public void onDataAvailable() throws IOException {
                do {
                    int length = input.read(buffer);
                    sb.append(new String(buffer,0,length));
                }while(input.isReady());

            }

            @Override
            public void onAllDataRead() throws IOException {
                System.out.println("data:"+sb);
                acontext.getResponse().getWriter().write("...the response...");
                acontext.complete();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
