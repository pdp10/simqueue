
/* File: SimulatedQueueException.java
 *
 * Author Piero Dalle Pezze
 * License: MIT
 * Year: 2005
 */



/** This generates an exception when it is impossible initialize the queue. */
public class SimulatedQueueException extends Exception {
    /* Default constructor. */
    public SimulatedQueueException() { this("SimulatedQueueException"); }
    /* Constructor. */
    public SimulatedQueueException(String s) { super(s); }
    /* Return the message of this exception. */
    public String getMessage() {
	return "Paramether n (the queue size) must be > 0. ";
    } 
}
