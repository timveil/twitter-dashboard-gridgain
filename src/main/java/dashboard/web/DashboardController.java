package dashboard.web;

import dashboard.service.TwitterService;
import dashboard.utils.StreamerWindow;
import org.atmosphere.cpr.*;
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

        final ObjectMapper mapper = new ObjectMapper();

        broadcast(atmosphereResource,
                20,
                "/counts/lastFive",
                new Callable<String>() {

                    public String call() throws Exception {
                        return mapper.writeValueAsString(twitterService.getHashTagSummary(StreamerWindow.FIVE_MIN));
                    }

                });
    }

    @RequestMapping(value = "/counts/lastFifteen")
    @ResponseBody
    public void fifteenMinuteCount(AtmosphereResource atmosphereResource) {
        final ObjectMapper mapper = new ObjectMapper();

        broadcast(atmosphereResource,
                20,
                "/counts/lastFifteen",
                new Callable<String>() {

                    public String call() throws Exception {
                        return mapper.writeValueAsString(twitterService.getHashTagSummary(StreamerWindow.FIFTEEN_MIN));
                    }

                });
    }

    @RequestMapping(value = "/counts/lastSixty")
    @ResponseBody
    public void sixtyMinuteCount(AtmosphereResource atmosphereResource) {
        final ObjectMapper mapper = new ObjectMapper();

        broadcast(atmosphereResource,
                20,
                "/counts/lastSixty",
                new Callable<String>() {

                    public String call() throws Exception {
                        return mapper.writeValueAsString(twitterService.getHashTagSummary(StreamerWindow.SIXTY_MIN));
                    }

                });
    }

    @RequestMapping(value = "/counts/topTweets")
    @ResponseBody
    public void topTweets(AtmosphereResource atmosphereResource) {

        final ObjectMapper mapper = new ObjectMapper();

        broadcast(atmosphereResource,
                20,
                "/counts/topTweets",
                new Callable<String>() {

                    public String call() throws Exception {
                        return mapper.writeValueAsString(twitterService.getTopTweeters());
                    }

                }
        );
    }


    @RequestMapping(value = "/counts/tweetsWithHashTag")
    @ResponseBody
    public void tweetsWithHashTags(AtmosphereResource atmosphereResource) {

        final ObjectMapper mapper = new ObjectMapper();

        broadcast(atmosphereResource,
                10,
                "/counts/tweetsWithHashTag",
                new Callable<String>() {

                    public String call() throws Exception {
                        return mapper.writeValueAsString(twitterService.getTotalTweetsWithHashTag());
                    }

                }
        );
    }

    @RequestMapping(value = "/counts/totalTweets")
    @ResponseBody
    public void totalTweets(AtmosphereResource atmosphereResource) {

        final ObjectMapper mapper = new ObjectMapper();

        broadcast(atmosphereResource,
                10,
                "/counts/totalTweets",
                new Callable<String>() {

                    public String call() throws Exception {
                        return mapper.writeValueAsString(twitterService.getTotalTweets());
                    }

                }
        );
    }


    private void broadcast(AtmosphereResource atmosphereResource, int broadcastFrequencySeconds, String url, Callable<String> callable) {


        this.suspend(atmosphereResource);

        final Broadcaster bc = BroadcasterFactory.getDefault().lookup(url, true);

        bc.addAtmosphereResource(atmosphereResource);

        if (log.isDebugEnabled()) {
            log.debug("broadcasting for url [" + url + "], id [" + bc.getID() + "], scope [" + bc.getScope() + "], atmosphere uuid [" + atmosphereResource.uuid() + "]");
        }

        bc.scheduleFixedBroadcast(callable, 5, broadcastFrequencySeconds, TimeUnit.SECONDS);
    }

    private void suspend(final AtmosphereResource resource) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        resource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
            @Override
            public void onSuspend(AtmosphereResourceEvent event) {
                if (log.isDebugEnabled()) {
                    log.debug("Suspending Client..." + resource.uuid());
                }

                countDownLatch.countDown();
                resource.removeEventListener(this);
            }

            @Override
            public void onDisconnect(AtmosphereResourceEvent event) {
                if (log.isDebugEnabled()) {
                    log.debug("Disconnecting Client..." + resource.uuid());
                }
                super.onDisconnect(event);
            }

            @Override
            public void onBroadcast(AtmosphereResourceEvent event) {
                if (log.isDebugEnabled()) {
                    log.debug("Client is broadcasting..." + resource.uuid());
                }
                super.onBroadcast(event);
            }
        });

        if (AtmosphereResource.TRANSPORT.LONG_POLLING.equals(resource.transport())) {
            resource.resumeOnBroadcast(true).suspend(-1);
        } else {
            resource.suspend(-1);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("suspend issue...", e);
        }
    }

}
