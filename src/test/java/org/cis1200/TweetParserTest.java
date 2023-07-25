package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/** Tests for TweetParser */
public class TweetParserTest {

    // A helper function to create a singleton list from a word
    private static List<String> singleton(String word) {
        List<String> l = new LinkedList<String>();
        l.add(word);
        return l;
    }

    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<String>();
        for (String s : words) {
            l.add(s);
        }
        return l;
    }

    // Cleaning and filtering tests -------------------------------------------
    @Test
    public void removeURLsTest() {
        assertEquals("abc . def.", TweetParser.removeURLs("abc http://www.cis.upenn.edu. def."));
        assertEquals("abc", TweetParser.removeURLs("abc"));
        assertEquals("abc ", TweetParser.removeURLs("abc http://www.cis.upenn.edu"));
        assertEquals("abc .", TweetParser.removeURLs("abc http://www.cis.upenn.edu."));
        assertEquals(" abc ", TweetParser.removeURLs("http:// abc http:ala34?#?"));
        assertEquals(" abc  def", TweetParser.removeURLs("http:// abc http:ala34?#? def"));
        assertEquals(" abc  def", TweetParser.removeURLs("https:// abc https``\":ala34?#? def"));
        assertEquals("abchttp", TweetParser.removeURLs("abchttp"));
    }

    @Test
    public void testCleanWord() {
        assertEquals("abc", TweetParser.cleanWord("abc"));
        assertEquals("abc", TweetParser.cleanWord("ABC"));
        assertNull(TweetParser.cleanWord("@abc"));
        assertEquals("ab'c", TweetParser.cleanWord("ab'c"));
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    /* **** ****** ***** **** EXTRACT COLUMN TESTS **** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testExtractColumnGetsCorrectColumn() {
        assertEquals(
                " This is a tweet.",
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 3
                )
        );
    }

    @Test
    public void testExtractInvalidColumn() {
        assertEquals(
                null,
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 4
                )
        );
    }

    @Test
    public void testExtractNullCSV() {
        assertEquals(
                null,
                TweetParser.extractColumn(
                        null, 0
                )
        );
    }

    /* **** ****** ***** ***** CSV DATA TO TWEETS ***** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTweetsSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<String>();
        expected.add(" The end should come here.");
        expected.add(" This comes from data with no duplicate words!");
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTweetsNullCSV() {
        StringReader sr = new StringReader(
                ""
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTweetsInvalidColumns() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 2);
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, tweets);
    }

    /* **** ****** ***** ** PARSE AND CLEAN SENTENCE ** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void parseAndCleanSentenceNonEmptyFiltered() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
        List<String> expected = new LinkedList<String>();
        expected.add("abc");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanSentenceEmpty() {
        List<String> sentence = TweetParser.parseAndCleanSentence("");
        List<String> expected = new LinkedList<>();
        assertEquals(expected, sentence);
    }

    @Test
    public void testParseAndCleanTweetOnlyBad() {
        List<String> sentence = TweetParser.parseAndCleanSentence("### #@#F @@@ ***");
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, sentence);
    }

    @Test
    public void testParseAndCleanTweetAllGood() {
        List<String> sentence = TweetParser.parseAndCleanSentence("hi hola nihao");
        List<String> expected = new LinkedList<String>();
        expected.add("hi");
        expected.add("hola");
        expected.add("nihao");
        assertEquals(expected, sentence);
    }

    /* **** ****** ***** **** PARSE AND CLEAN TWEET *** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testParseAndCleanTweetRemovesURLS1() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc http://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetRemovesURLSEmpty() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("");
        List<List<String>> expected = new LinkedList<List<String>>();
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetRemovesAllURLS() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet(
                        "http://www.cis.upenn.edu " +
                                "https://www.cis.upenn.edu/~cis1600/current/ " +
                                "https://www.cis.upenn.edu/~cis120/current/"
                );
        List<List<String>> expected = new LinkedList<List<String>>();
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetRemovesNoURLS() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("hello world");
        List<List<String>> expected = new LinkedList<List<String>>();
        String[] words = { "hello", "world" };
        expected.add(listOfArray(words));
        assertEquals(expected, sentences);
    }

    /* **** ****** ***** ** CSV DATA TO TRAINING DATA ** ***** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTrainingDataSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataEmptyCSV() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataSomeBadWords() {
        StringReader sr = new StringReader(
                "0, #$@\n" +
                        "1, @!$\n" +
                        "2, hi\n" +
                        "3, @$# hello"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("hi"));
        expected.add(singleton("hello"));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataWithURLS() {
        StringReader sr = new StringReader(
                "0, The end should come here. http://www.cis.upenn.edu \n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataAllBad() {
        StringReader sr = new StringReader(
                "0, #$@\n" +
                        "1, @!$\n" +
                        "2, http://www.cis.upenn.edu\n" +
                        "3, @$#"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataWithInvalidColumns() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 2);
        List<List<String>> expected = new LinkedList<List<String>>();
        assertEquals(expected, tweets);
    }

}
