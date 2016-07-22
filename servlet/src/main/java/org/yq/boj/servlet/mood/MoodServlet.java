package org.yq.boj.servlet.mood;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created on 2016/7/17.
 */
@WebServlet("/mood/report")
public class MoodServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("<title>Servlet MoodServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MoodServlet at "
                    + request.getContextPath() + "</h1>");
            String mood = (String) request.getAttribute("mood");
            out.println("<p>Duke's mood is: " + mood + "</p>");
            switch (mood) {
                case "sleepy":
                    out.println("<img src=\"" + request.getContextPath() + "/resources/images/mood/duke.snooze.gif\" alt=\"Duke sleeping\"/><br/>");
                    break;
                case "alert":
                    out.println("<img src=\"" + request.getContextPath() + "/resources/images/mood/duke.waving.gif\" alt=\"Duke waving\"/><br/>");
                    break;
                case "hungry":
                    out.println("<img src=\"" + request.getContextPath() + "/resources/images/mood/duke.cookies.gif\" alt=\"Duke with cookies\"/><br/>");
                    break;
                case "lethargic":
                    out.println("<img src=\"" + request.getContextPath() + "/resources/images/mood/duke.handsOnHips.gif\" alt=\"Duke with hands on hips\"/><br/>");
                    break;
                case "thoughtful":
                    out.println("<img src=\"" + request.getContextPath() + "/resources/images/mood/duke.pensive.gif\" alt=\"Duke thinking\"/><br/>");
                    break;
                default:
                    out.println("<img src=\"" + request.getContextPath() + "/resources/images/mood/duke.thumbsup.gif\" alt=\"Duke with thumbs-up gesture\"/><br/>");
                    break;
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        processRequest(req, resp);
    }
}
