
/* File: TriangularException.java
 *
 * Author Piero Dalle Pezze
 * License: MIT
 * Year: 2005
 */


/** This generates an exception when it is impossible initialize a triangular casual variable. */
class TriangularException extends Exception {
    /* Default constructor. */
    public TriangularException() { this("TriangularException"); }
    /* Constructor. */
    public TriangularException(String s) { super(s); }
    /* Return the message of this exception. */
    public String getMessage() {
	return "Paramethers must be a <= m <= b and a < b .";
    } 
}
