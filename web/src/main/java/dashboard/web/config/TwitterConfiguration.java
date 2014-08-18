package dashboard.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.Properties;

@Configuration
public class TwitterConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @javax.annotation.Resource(name = "properties")
    private Properties properties;

    @Bean
    public Twitter twitter() {
        final String consumerKey = properties.getProperty("twitter.consumer-key");
        final String consumerSecret = properties.getProperty("twitter.consumer-secret");
        final String accessToken = properties.getProperty("twitter.access-token");
        final String accessTokenSecret = properties.getProperty("twitter.access-token-secret");

        log.debug("twitter consumer key:  " + consumerKey);
        log.debug("twitter consumer secret:  " + consumerSecret);
        log.debug("twitter access token:  " + accessToken);
        log.debug("twitter access token secret:  " + accessTokenSecret);

        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

}
