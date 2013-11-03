package dashboard.web;

import dashboard.model.HashtagAggregate;
import dashboard.streaming.TwitterService;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.Broadcaster;
import org.codehaus.jackson.map.ObjectMapper;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFactory;
import org.gridgain.grid.lang.GridClosure;
import org.gridgain.grid.lang.GridReducer0;
import org.gridgain.grid.streamer.GridStreamer;
import org.gridgain.grid.streamer.GridStreamerContext;
import org.gridgain.grid.streamer.index.GridStreamerIndex;
import org.gridgain.grid.streamer.index.GridStreamerIndexEntry;
import org.gridgain.grid.typedef.F;
import org.gridgain.grid.typedef.X;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public String start(WebRequest request, ModelMap model) {

        final String minutes = request.getParameter("duration");
        int duration = 10000;

        if (StringUtils.hasText(minutes)) {
            duration = Integer.parseInt(minutes) * 60 * 1000;
        }

        twitterService.ingest(duration);

        return "redirect:/index";
    }

    @RequestMapping(value = "/twitter/concurrency")
    @ResponseBody
    public void twitterAsync(AtmosphereResource atmosphereResource) {
        final ObjectMapper mapper = new ObjectMapper();

        this.suspend(atmosphereResource);

        final Broadcaster bc = atmosphereResource.getBroadcaster();

        log.info("Atmo Resource Size: " + bc.getAtmosphereResources().size());

        bc.scheduleFixedBroadcast(new Callable<String>() {

            //@Override
            public String call() throws Exception {

                return mapper.writeValueAsString(twitterService.getHashtagAgregate());
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