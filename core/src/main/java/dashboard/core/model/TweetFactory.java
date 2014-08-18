package dashboard.core.model;

import java.util.UUID;

public class TweetFactory {

    public static Tweet create(org.springframework.social.twitter.api.Tweet tweet, boolean fake) {

        Tweet ggTweet = new Tweet();
        ggTweet.setGUID(UUID.randomUUID().toString());
        ggTweet.setTweetId(tweet.getId());
        ggTweet.setText(tweet.getText());
        ggTweet.setCreatedAt(tweet.getCreatedAt());
        ggTweet.setScreenName(tweet.getUser().getScreenName());
        ggTweet.setUserId(tweet.getUser().getId());
        ggTweet.setLanguageCode(tweet.getLanguageCode());
        ggTweet.setGeoEnabled(tweet.getUser().isGeoEnabled());
        ggTweet.setLocation(tweet.getUser().getLocation());
        ggTweet.setSource(tweet.getSource());
        ggTweet.setFake(fake);

        return ggTweet;

    }
}
