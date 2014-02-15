package com.a1systems.smpp.simulator;

import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.type.SmppChannelException;
import java.io.IOException;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws SmppChannelException, IOException {
        SmppServerConfiguration configuration = new SmppServerConfiguration();
        configuration.setPort(2776);
        configuration.setMaxConnectionSize(10);
        configuration.setNonBlockingSocketsEnabled(true);
        configuration.setDefaultRequestExpiryTimeout(30000);
        configuration.setDefaultWindowMonitorInterval(15000);
        configuration.setDefaultWindowSize(5);
        configuration.setDefaultWindowWaitTimeout(configuration.getDefaultRequestExpiryTimeout());
        configuration.setDefaultSessionCountersEnabled(true);
        configuration.setJmxEnabled(true);

        // create a server, start it up
        DefaultSmppServer smppServer = new DefaultSmppServer(configuration, new DefaultSmppServerHandler(Executors.newScheduledThreadPool(10)));

        logger.info("Starting SMPP server...");
        smppServer.start();
        logger.info("SMPP server started");

        System.out.println("Press any key to stop server");
        System.in.read();

        logger.info("Stopping SMPP server...");
        smppServer.stop();
        logger.info("SMPP server stopped");

        logger.info("Server counters: {}", smppServer.getCounters());
    }
}
