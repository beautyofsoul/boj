package org.yq.boj.websocket.dukeetf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.util.Random;

/**
 * Created on 2016/7/24.
 */
@Startup
@Singleton
public class PriceVolumeBean {

    @Resource
    TimerService tservice;

    private Random random;
    private volatile double price = 100.0;
    private volatile int volume = 300000;
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceVolumeBean.class);

    @PostConstruct
    public void init() {
        /* Initialize the EJB and create a timer */
        LOGGER.info("Initializing EJB.");
        random = new Random();
        tservice.createIntervalTimer(1000, 1000, new TimerConfig());
    }

    @Timeout
    public void timeout() {
        /* Adjust price and volume and send updates */
        price += 1.0 * (random.nextInt(100) - 50) / 100.0;
        volume += random.nextInt(5000) - 2500;
        ETFEndpoint.send(price, volume);
    }
}
