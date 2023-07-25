package org.cis1200;

import org.junit.jupiter.api.Test;

//import java.io.IOException;
import java.io.StringReader;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for FileLineIterator */
public class FileLineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    public void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to test out our
        // FileLineIterator if we do not want to. We can just create a
        // StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */
    // constructor tests
    @Test
    public void testFileLineIteratorNullReader() {
        assertThrows(
                IllegalArgumentException.class, () ->
                        new FileLineIterator((BufferedReader) null)
        );
    }

    @Test
    // fileToReader tests
    public void testFileToReaderNullFilePath() {
        assertThrows(IllegalArgumentException.class, () -> new FileLineIterator(""));
    }

    // next and hasNext tests
    @Test
    public void testEmptyString() {
        String words = "";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertFalse(li.hasNext());
    }

    // what should we do with null string
    // @Test
    // public void testNullString() {
    // String words = null;
    // StringReader sr = new StringReader(words);
    // BufferedReader br = new BufferedReader(sr);
    // FileLineIterator li = new FileLineIterator(br);
    // assertFalse(li.hasNext());
    // }

    @Test
    public void testSingleLine() {
        String words = "hello world";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);

        assertTrue(li.hasNext());
        assertEquals("hello world", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testWithEmptyNewLine() {
        String words = "hello\n\nworld";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);

        assertTrue(li.hasNext());
        assertEquals("hello", li.next());
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertTrue(li.hasNext());
        assertEquals("world", li.next());
        assertFalse(li.hasNext());
    }

    @Test
    public void testEmptyNewLine() {
        String words = "Hello World\n";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);

        assertTrue(li.hasNext());
        assertEquals("Hello World", li.next());
        assertFalse(li.hasNext());
    }

    // test empty lines in the beginning
    @Test
    public void testEmptyLinesAtStart() {
        String words = "\n\n\nHello World";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);

        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertTrue(li.hasNext());
        assertEquals("", li.next());
        assertTrue(li.hasNext());
        assertEquals("Hello World", li.next());
        assertFalse(li.hasNext());
    }

}
