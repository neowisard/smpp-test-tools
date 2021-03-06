package com.a1systems.smpp.multiplexer.server;


import java.lang.ref.WeakReference;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CleanupTask implements Runnable {

    protected static final Logger logger  = LoggerFactory.getLogger(CleanupTask.class);

    protected String sessionName;
    
    protected WeakReference<ConcurrentHashMap<String, MsgRoute>> map;

    public CleanupTask(ConcurrentHashMap<String, MsgRoute> msgMap, String sessionName) {
        this.map = new WeakReference<ConcurrentHashMap<String, MsgRoute>>(msgMap);
        this.sessionName = sessionName;
    }

    @Override
    public void run() {
        logger.info("Started cleanup task for:{}", sessionName);

        if (map.get() != null) {
            ConcurrentHashMap<String, MsgRoute> msgMap = map.get();

            int dropped=0;
            
            for (Entry<String, MsgRoute> entry:msgMap.entrySet()) {
                MsgRoute route = entry.getValue();

                if (route.getCreateDate().plusMinutes(3).isBeforeNow()) {
                    logger.debug("Remove entry:{} for:{}", entry.getKey(), sessionName);

                    msgMap.remove(entry.getKey());
                    
                    dropped++;
                }
            }
            
            logger.info("Removed {} items for:{}", dropped, sessionName);
        } else {
            logger.info("Nothing to clean up for:{}", sessionName);
        }
        
        logger.info("Clean up task completed for:{}", sessionName);
    }


}
