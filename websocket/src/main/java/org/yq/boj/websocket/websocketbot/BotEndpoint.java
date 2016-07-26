/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 * <p>
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package org.yq.boj.websocket.websocketbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yq.boj.websocket.dukeetf.ETFEndpoint;
import org.yq.boj.websocket.websocketbot.decoders.MessageDecoder;
import org.yq.boj.websocket.websocketbot.encoders.ChatMessageEncoder;
import org.yq.boj.websocket.websocketbot.encoders.InfoMessageEncoder;
import org.yq.boj.websocket.websocketbot.encoders.JoinMessageEncoder;
import org.yq.boj.websocket.websocketbot.encoders.UsersMessageEncoder;
import org.yq.boj.websocket.websocketbot.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/* Websocket endpoint */
@ServerEndpoint(
        value = "/websocketbot/websocketbot",
        decoders = {MessageDecoder.class},
        encoders = {JoinMessageEncoder.class, ChatMessageEncoder.class,
                InfoMessageEncoder.class, UsersMessageEncoder.class}
)
/* There is a BotEndpoint instance per connetion */
public class BotEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETFEndpoint.class);
    /* Bot functionality bean */
    @Inject
    private BotBean botBean;
    /* Executor service for asynchronous processing */
    @Resource(name = "concurrent/ThreadPool")
    private ManagedExecutorService mes;

    @OnOpen
    public void openConnection(Session session) {

        LOGGER.info("Connection opened.");
    }

    @OnMessage
    public void message(final Session session, Message msg) {
        LOGGER.info("Received: {}", msg.toString());

        if (msg instanceof JoinMessage) {
            /* Add the new user and notify everybody */
            JoinMessage jmsg = (JoinMessage) msg;
            session.getUserProperties().put("name", jmsg.getName());
            session.getUserProperties().put("active", true);
            LOGGER.info("Received: {}", jmsg.toString());
            sendAll(session, new InfoMessage(jmsg.getName() +
                    " has joined the chat"));
            sendAll(session, new ChatMessage("Duke", jmsg.getName(),
                    "Hi there!!"));
            sendAll(session, new UsersMessage(this.getUserList(session)));

        } else if (msg instanceof ChatMessage) {
            /* Forward the message to everybody */
            final ChatMessage cmsg = (ChatMessage) msg;
            LOGGER.info("Received: {}", cmsg.toString());
            sendAll(session, cmsg);
            if (cmsg.getTarget().compareTo("Duke") == 0) {
                /* The bot replies to the message */
                mes.submit(new Runnable() {
                    @Override
                    public void run() {
                        String resp = botBean.respond(cmsg.getMessage());
                        sendAll(session, new ChatMessage("Duke",
                                cmsg.getName(), resp));
                    }
                });
            }
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        /* Notify everybody */
        session.getUserProperties().put("active", false);
        if (session.getUserProperties().containsKey("name")) {
            String name = session.getUserProperties().get("name").toString();
            sendAll(session, new InfoMessage(name + " has left the chat"));
            sendAll(session, new UsersMessage(this.getUserList(session)));
        }
        LOGGER.info("Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {

        LOGGER.error("Connection error", t);
    }

    /* Forward a message to all connected clients
     * The endpoint figures what encoder to use based on the message type */
    public synchronized void sendAll(Session session, Object msg) {
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendObject(msg);
                    LOGGER.info("Sent: {}", msg.toString());
                }
            }
        } catch (IOException | EncodeException e) {
            LOGGER.error("", e);
        }
    }

    /* Returns the list of users from the properties of all open sessions */
    public List<String> getUserList(Session session) {
        List<String> users = new ArrayList<>();
        users.add("Duke");
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen() && (boolean) s.getUserProperties().get("active"))
                users.add(s.getUserProperties().get("name").toString());
        }
        return users;
    }
}
