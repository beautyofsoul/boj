package org.yq.boj.servlet.mood;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created on 2016/7/20.
 */
@WebFilter(filterName = "TimeOfDayFilter",
        urlPatterns = {"/mood/*"},
        initParams = {
                @WebInitParam(name = "mood", value = "awake")})
public class TimeOfDayFilter implements Filter {

    String mood = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        mood = filterConfig.getInitParameter("mood");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Calendar cal = GregorianCalendar.getInstance();
        switch (cal.get(Calendar.HOUR_OF_DAY)) {
            case 23:
            case 24:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                mood = "sleepy";
                break;
            case 7:
            case 13:
            case 18:
                mood = "hungry";
                break;
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 16:
            case 17:
                mood = "alert";
                break;
            case 11:
            case 15:
                mood = "in need of coffee";
                break;
            case 19:
            case 20:
            case 21:
                mood = "thoughtful";
                break;
            case 22:
                mood = "lethargic";
                break;
        }
        req.setAttribute("mood", mood);
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
