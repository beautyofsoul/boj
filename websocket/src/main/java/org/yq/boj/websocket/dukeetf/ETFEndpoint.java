package org.yq.boj.websocket.dukeetf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created on 2016/7/24.
 */
@ServerEndpoint("/dukeetf/dukeetf")
public class ETFEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETFEndpoint.class);

    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    @OnOpen
    public void openConnection(Session session) {
        queue.add(session);
        LOGGER.info("Connection opened!");
    }

    @OnClose
    public void closedConnection(Session session) {
        queue.remove(session);
        LOGGER.info("Connection closed!");
    }

    @OnError
    public void error(Session session, Throwable t) {
        queue.remove(session);
        LOGGER.error("Connection error!", t);
    }


    public static void send(double price, int volume) {
        String msg = String.format("%.2f / %d", price, volume);
        try {
            /* Send updates to all open WebSocket sessions */
            for (Session session : queue) {
                session.getBasicRemote().sendText(msg);
                LOGGER.info("Sent: {}", msg);
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }


}
