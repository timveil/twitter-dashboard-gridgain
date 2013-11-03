package dashboard.web;

import dashboard.streaming.TwitterService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

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

        return "redirect:/mvc/index";
    }

}