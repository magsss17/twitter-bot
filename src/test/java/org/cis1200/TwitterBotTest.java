package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Tests for TwitterBot class */
public class TwitterBotTest {

    /*
     * This tests whether your TwitterBot class itself is written correctly
     *
     * This test operates very similarly to our MarkovChain tests in its use of
     * `fixDistribution`, so make sure you know how to test MarkovChain before
     * testing this!
     */
    @Test
    public void simpleTwitterBotTest() {
        List<String> desiredTweet = new ArrayList<>(
                Arrays.asList(
                        "this", "comes", "from", "data", "with", "no", "duplicate", "words", ".",
                        "the", "end", "should", "come", "."
                )
        );
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);
        t.fixDistribution(desiredTweet);

        String expected = "this comes from data with no duplicate words. the end should come.";
        String actual = TweetParser.replacePunctuation(t.generateTweet(12));
        assertEquals(expected, actual);
    }

    /*
     * This is the empty file test case.
     *
     * When your CSV file is empty, your program should create an empty MarkovChain.
     * An empty tweet should be generated by your bot.
     * No exceptions should be thrown and your program should not go into an
     * infinite loop!
     */
    @Test
    public void emptyFileCreatesEmptyTweet() {
        // Checks that your program does not go into an infinite loop
        assertTimeoutPreemptively(
                Duration.ofSeconds(10), () -> {
                    // No exceptions are thrown if file is empty
                    TwitterBot tb = new TwitterBot(
                            FileLineIterator.fileToReader("./files/empty.csv"), 2
                    );
                    // Checks that the bot creates an empty tweet
                    assertEquals(0, tb.generateTweet(10).length());
                }
        );
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */
    @Test
    public void testTwitterBotNull() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);

        assertThrows(IllegalArgumentException.class, () -> t.fixDistribution(null));
        String actual = TweetParser.replacePunctuation(t.generateTweet(10));
        assertEquals("", actual);
    }

    @Test
    public void testTwitterBotEmpty() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);

        assertThrows(IllegalArgumentException.class, () -> t.fixDistribution(new ArrayList<>()));

        String actual = TweetParser.replacePunctuation(t.generateTweet(10));
        assertEquals("", actual);
    }

    @Test
    public void testTwitterBotInvalid() {
        List<String> tweet = new ArrayList<>();
        StringReader sr = new StringReader("" + "!" + "a@#234" + "" + " .");
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);

        assertThrows(IllegalArgumentException.class, () -> t.fixDistribution(tweet));

        String actual = TweetParser.replacePunctuation(t.generateTweet(5));
        assertEquals("", actual);
    }
}
