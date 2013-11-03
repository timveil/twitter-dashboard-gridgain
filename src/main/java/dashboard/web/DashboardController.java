package dashboard.web;

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
