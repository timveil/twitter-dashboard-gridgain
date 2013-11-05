package dashboard.web;

import dashboard.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String get(ModelMap model) {
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String post(HttpServletRequest request, ModelMap model) {

        final String text = request.getParameter("text");
        final String screenName = request.getParameter("screenName");

        model.put("tweets", twitterService.findTweets(text, screenName));

        return "search";
    }


}