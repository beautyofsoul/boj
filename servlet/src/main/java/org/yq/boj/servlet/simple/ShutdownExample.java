package org.yq.boj.servlet.simple;


import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class ShutdownExample  extends HttpServlet{

    private long serviceCounter = 0;

    private boolean shuttingDown;

    protected synchronized  void setShuttingDown(boolean flag)
    {
       shuttingDown = flag;
    }

    protected  synchronized  boolean isShuttingDown()
    {
        return shuttingDown;
    }

    protected synchronized void enteringServiceMethod()
    {
        serviceCounter ++;
    }

    protected synchronized void leavingServiceMethod()
    {
        serviceCounter --;
    }

    protected  synchronized  long numService()
    {
        return serviceCounter;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        enteringServiceMethod();
        try {
            super.service(req, res);
        }
        finally {
            leavingServiceMethod();
        }

    }


    @Override
    public void destroy() {
        if(numService()>0)
        {
            setShuttingDown(true);
        }
        while (numService()>0)
        {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {

            }
        }
    }
}
