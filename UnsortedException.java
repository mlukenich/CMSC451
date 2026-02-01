/**
 * File: UnsortedException.java
 * Author: Matthew Lukenich
 * Project: CSMC451 Project 1
 * 
 * UnsortedException class that is thrown when the array is not sorted
 * correctly.
 */
public class UnsortedException extends Exception {
    /*
     * Constructor for UnsortedException.
     * 
     * @param message The message to display when the exception is thrown.
     */
    public UnsortedException(String message) {
        super(message);
    }
}
