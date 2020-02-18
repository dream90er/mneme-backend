package net.ddns.la90zy.mnemo.syncservice.control;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
@DependsOn("DatabaseBootstrapBean")
public class BatchSyncScheduler {

    @Resource
    private TimerService timerService;

    @Inject
    private BatchSyncService batchSyncService;

    @PostConstruct
    private void init() {
        long interval = TimeUnit.HOURS.toMillis(12L);
        timerService.createTimer(interval, "Playlists synchronization timer with 12 hours interval");
    }

    @Timeout
    public void syncPlaylists(Timer timer) {
        batchSyncService.sync();
    }
}
