package dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfiguration {

    private static final String CONSUMER_KEY = "UDr4MGoK5XxHaUVkurRmpw";
    private static final String CONSUMER_SECRET = "yryIt8Js0HFeCjqBZMGWKSWO1KWyXpBOHP2fCNofpk";
    private static final String ACCESS_TOKEN = "371408397-CGUgW70jVb3a2YPMjSQoqTrI88JtVub9uDozDhEN";
    private static final String ACCESS_TOKEN_SECRET = "hS5m951GIngZ7STLh02y91eP8sSJsnTQzKg9DMrPg";


    @Bean
    public Twitter twitter() {
        return new TwitterTemplate(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
    }

}
