package dashboard.web;

import dashboard.streaming.StreamerWindow;
import dashboard.streaming.TwitterService;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Controller
public class DashboardController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterService twitterService;



    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String get(ModelMap model) {
        return "dashboard";
    }


    @RequestMapping(value = "/counts/lastFive")
    @ResponseBody
    public void fiveMinuteCount(AtmosphereResource atmosphereResource) {
        broadcastCounts(atmosphereResource, StreamerWindow.FIVE_MIN);
    }

    @RequestMapping(value = "/counts/lastFifteen")
    @ResponseBody
    public void fifteenMinuteCount(AtmosphereResource atmosphereResource) {
        broadcastCounts(atmosphereResource, StreamerWindow.FIFTEEN_MIN);
    }

    @RequestMapping(value = "/counts/lastSixty")
    @ResponseBody
    public void sixtyMinuteCount(AtmosphereResource atmosphereResource) {
        broadcastCounts(atmosphereResource, StreamerWindow.SIXTY_MIN);
    }

    private void broadcastCounts(AtmosphereResource atmosphereResource, final StreamerWindow window) {
        final ObjectMapper mapper = new ObjectMapper();

        this.suspend(atmosphereResource);

        final Broadcaster bc = atmosphereResource.getBroadcaster();

        bc.scheduleFixedBroadcast(new Callable<String>() {

            //@Override
            public String call() throws Exception {

                return mapper.writeValueAsString(twitterService.getHashtagSummary(window));
            }

        }, 10, TimeUnit.SECONDS);
    }

    private void suspend(final AtmosphereResource resource) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
            @Override
            public void onSuspend(AtmosphereResourceEvent event) {
                countDownLatch.countDown();
                resource.removeEventListener(this);
            }
        });
        resource.suspend();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
