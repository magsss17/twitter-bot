package org.cis1200;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.util.NoSuchElementException;
import java.io.FileReader;

/**
 * FileLineIterator provides a useful wrapper around Java's provided
 * BufferedReader and provides practice with implementing an Iterator. Your
 * solution should not read the entire file into memory at once, instead reading
 * a line whenever the next() method is called.
 * <p>
 * Note: Any IOExceptions thrown by readers should be caught and handled
 * properly. Do not use the ready() method from BufferedReader.
 */
public class FileLineIterator implements Iterator<String> {

    private final BufferedReader reader;
    // INVARIANT: isValid checks whether there is another line
    private boolean nextLine;
    // INVARIANT: If there is no subsequent line, nextLineStr is null
    // Otherwise, nextLineStr saves the next line
    private String nextLineStr = null;

    /**
     * Creates a FileLineIterator for the reader. Fill out the constructor so
     * that a user can instantiate a FileLineIterator. Feel free to create and
     * instantiate any variables that your implementation requires here. See
     * recitation and lecture notes for guidance.
     * <p>
     * If an IOException is thrown by the BufferedReader, then hasNext should
     * return false.
     * <p>
     * The only method that should be called on BufferedReader is readLine() and
     * close(). You cannot call any other methods.
     *
     * @param reader - A reader to be turned to an Iterator
     * @throws IllegalArgumentException if reader is null
     */

    public FileLineIterator(BufferedReader reader) {
        // check if the reader is null
        if (reader == null) {
            throw new IllegalArgumentException();
        }
        this.reader = reader;
        try {
            // try to read the first line
            this.nextLineStr = this.reader.readLine();
            this.nextLine = (nextLineStr != null);
        } catch (IOException e) {
            // there is no first line
            this.nextLine = false;
        }
    }

    /**
     * Creates a FileLineIterator from a provided filePath by creating a
     * FileReader and BufferedReader for the file.
     * <p>
     * DO NOT MODIFY THIS METHOD.
     * 
     * @param filePath - a string representing the file
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public FileLineIterator(String filePath) {
        this(fileToReader(filePath));
    }

    /**
     * Takes in a filename and creates a BufferedReader.
     * See Java's documentation for BufferedReader to learn how to construct one
     * given a path to a file.
     *
     * @param filePath - the path to the CSV file to be turned to a
     *                 BufferedReader
     * @return a BufferedReader of the provided file contents
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public static BufferedReader fileToReader(String filePath) {
        if (filePath == null || filePath.equals("")) {
            throw new IllegalArgumentException();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }
        return reader;
    }

    /**
     * Returns true if there are lines left to read in the file, and false
     * otherwise.
     * <p>
     * If there are no more lines left, this method should close the
     * BufferedReader.
     *
     * @return a boolean indicating whether the FileLineIterator can produce
     *         another line from the file
     */
    @Override
    public boolean hasNext() {
        // if there is no next line, attempt to close the reader
        if (!nextLine) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new NoSuchElementException();
            }
        }
        // return whether there's a next line
        return nextLine;
    }

    /**
     * Returns the next line from the file, or throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false).
     * <p>
     * This method also advances the iterator in preparation for another
     * invocation. If an IOException is thrown during a next() call, your
     * iterator should make note of this such that future calls of hasNext()
     * will return false and future calls of next() will throw a
     * NoSuchElementException
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the
     *                                          file
     */
    @Override
    public String next() {
        // save the current nextLineStr
        String curr = nextLineStr;
        // there is no next line
        if (!nextLine) {
            throw new java.util.NoSuchElementException();
        } else { // there is a next line
            try {
                // attempt to read the next line and reset nextLine boolean
                // reader is moved to the next line
                nextLineStr = reader.readLine();
                nextLine = (nextLineStr != null);
            } catch (IOException e) {
                // there is no next line
                nextLine = false;
                throw new java.util.NoSuchElementException();
            }
        }
        // return the previous line
        return curr;
    }
}
