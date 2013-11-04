package dashboard.web;

import dashboard.streaming.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class IngestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/ingest", method = RequestMethod.GET)
    public String get(ModelMap model) {
        return "ingest";
    }

    @RequestMapping(value = "/ingest", method = RequestMethod.POST)
    public String post(HttpServletRequest request, ModelMap model) {

        final String minutes = request.getParameter("duration");
        int duration = 10000;

        if (StringUtils.hasText(minutes)) {
            duration = Integer.parseInt(minutes) * 60 * 1000;
        }

        request.getSession().setAttribute("startTime", new Date());

        twitterService.ingest(duration);

        return "redirect:/dashboard";
    }



}