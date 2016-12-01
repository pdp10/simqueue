
/* File: ExponentialException.java
 *
 * Author Piero Dalle Pezze
 * License: MIT
 * Year: 2005 
 */



/** This generates an exception when it is impossible initialize a exponential casual variable. */
public class ExponentialException extends Exception {
    /* Default constructor. */
    public ExponentialException() { this("ExponentialException"); }
    /* Constructor. */
    public ExponentialException(String s) { super(s); }
    /* Return the message of this exception. */
    public String getMessage() {
	return "Paramether b mnust be > 0 .";
    } 
}
