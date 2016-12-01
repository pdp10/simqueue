
/* File: SimulatedQueue.java
 *
 * Author Piero Dalle Pezze
 * License: MIT
 * Year: 2005
 */

import java.io.*;
import java.util.*;


/** This program is a simulation based on casual temporary cadences.
 *  Here, I want to simulate a real queue, like a queue of a post office,
 *  where users arrive in a casual order.  When a client arrives, he is
 *  the last one who will be served. (FIFO = First In First Out). In particular
 *  there are two stochastic facts:
 *  1) the time of arrive of a client,
 *  2) the time of a client service.
 *  During the time some clients arrive and some others go away.
 *  A simulation of this reality is to show when a person arrives, starts to be
 *  served and then leaves the server.    
 *  For any reference, you can consult 'Ricerca Operativa' by Paolo Malesani
 *  chapter 6 that is the original idea of this program. 
*/
public class SimulatedQueue implements Runnable {

    private double[][] queue; // the casual history 
    private int
	n = 1,               // the number of person to server 
	n1 = -1,             // the name of the last entered. no client
	n2 = -1;             // the name of the last served. no client
    private double
	t = 0.0,             // time: it is the actual time
	t1 = 0.0,            // time: it shows when the next client will arrive 
	t2 = 999999999.9,    // time: it shows when has finished his activity
	limit = 999999999.9, //infinite
	c1,                  // exponential casual variable
	c2,                  // triangular casual variable
	bC1 = 0.0,           // parameter b of c1
	aC2 = 0.0,           // parameter a of c2
	mC2 = 0.0,           // parameter m of c2 the mode
	bC2 = 0.0,           // parameter b of c2
	// statistics after the simulation
	averangeArrive = 0.0,
	varianceArrive = 0.0,
	sdArrive = 0.0,
	maximumServiceTime = 0.0,
	averangeServiceTime = 0.0,
	varianceServiceTime = 0.0,
	sdServiceTime = 0.0;
    private Random rand;     // the uniform variable
    private Calendar 
	start = null, 
	end = null;  //to misure the running time of the simulation

    //SET STHOCASTIC VARIABLES
    /** It returns a triangular casual variable. */
    protected double generateTriangular() {
	double y = rand.nextDouble();
 	if( y < ((mC2 - aC2) / (bC2 - aC2)) )
	    c2 = aC2 + Math.sqrt( (bC2-aC2)*(mC2-aC2)*y );
	else
	    c2 = bC2 - Math.sqrt( (bC2-aC2)*(bC2-mC2)*(1-y) );
	return c2;
    }

    /** It returns an exponential casual variable. */
    protected double generateExponential() {
	//generate exponential casual variable
	c1 = - ( Math.log( 1-rand.nextDouble() ) / bC1 );
	return c1;
    }

    //SET THE STATISTICS
    /** It sets the simulated averange time of a new user arrive . */
    protected double setAverangeArrive() {
	double ave = 0.0;              // averange
	for( int i = 1; i < n; i++ )
	    ave += queue[0][i] - queue[0][i-1];
	ave /= n;
	return ave;
    }

    /** It sets the simulated variance of the time of a new user arrive . */
    protected double setVarianceArrive() { 
	double b = 1 / setAverangeArrive();
	return 1 / (b * b);
    }

    /** It sets the simulated standard deviation of 
     *  the time of a new user arrive . */
    protected double setSDArrive() { 
	return Math.sqrt( setVarianceArrive() ); 
    }

    /** It returns the simulated averange service time. */
    protected double setAverangeServiceTime() {
	double ave = 0.0;              // averange
	for( int i = 0; i < n; i++ )
	    ave += queue[2][i] - queue[1][i];
	ave /= n;
	return ave;
    }

    /** It sets the maximum simulated service time. */
    protected double setMaximumServiceTime() {
	double max = 0.0, temp = 0.0;
	for( int i = 0; i < n; i++ ) {
	    temp = queue[2][i] - queue[1][i];
	    if( temp > max ) 
		max = temp;
	}
	return max;
    }

    /** It sets the minimum simulated service time. */
    protected double setMinimumServiceTime() {
	double min = queue[2][0] - queue[1][0], temp = 0.0;
	for( int i = 1; i < n; i++ ) {
	    temp = queue[2][i] - queue[1][i];
	    if( temp < min ) 
		min = temp;
	}
	return min;
    }

    /* It sets the simulated variance service time. */
    protected double setVarianceServiceTime() {
	double 
	    ave = setAverangeServiceTime(),
	    min = setMinimumServiceTime(),
	    max = setMaximumServiceTime(),
	    mode = (3 * ave) - min - max;
	return ((max - min)*(max - min) - (mode - min)*(max - mode)) / 18;
    }

    /* It sets the simulated standard deviation of the service time. */
    protected double setSDServiceTime() { 
	return Math.sqrt( setVarianceServiceTime() ); 
    }


    /** Constructor. It builts a queue of capacity = _n. 
     * @throws QueueSimulationException if _n < 1. */
    public SimulatedQueue( int _n ) throws SimulatedQueueException {
	if( _n > 0 ) {
	    n = _n;
	    queue = new double[3][n];
	    rand = new Random();
	} else
	    throw new SimulatedQueueException();
    }

    //PUBLIC METHODS
    /** It returns the capacity of the queue. */
    public int getN() { return n; }

    /** It returns the limit used as infinite. Limit value is 999999999.9 . */
    public double getLimit() { return limit; }

    /** It sets the parameter b of the exponential casual variable. 
     *  b parameter must be  > 0. 
     *	@throws ExponentialException if b < 0. */
    public void setExponential( double b ) throws ExponentialException {
	if( b > 0 )
	    bC1 = b;
	else
	    throw new ExponentialException();
    }

    /** It sets the parameters a, m, b of the triangular casual variable. 
     *  a <= m <= b and a < b. 
     *  @throws TriangularException if not a <= m <= b or not a < b. */
    public void setTriangular( double a, double m, double b ) throws TriangularException { 
	if( a <= m && m <= b && a < b ) {
	    aC2 = a;
	    mC2 = m;
	    bC2 = b;
	} else
	    throw new TriangularException();
    }



    //RETURN THE STATISTICS
    /** It returns the simulated averange time of a new user arrive . */
    public double averangeArrive() { return averangeArrive; }

    /** It returns the simulated variance of the time of a new user arrive . */
    public double varianceArrive() { return varianceArrive; }

    /** It returns the simulated standard deviation of the time of a new user arrive . */
    public double sdArrive() { return sdArrive; }

    /** It returns the simulated averange service time. */
    public double averangeServiceTime() { return averangeServiceTime; }

    /** It returns the maximum simulated service time. */
    public double maximumServiceTime() { return maximumServiceTime; }

    /** It returns the simulated variance service time. */
    public double varianceServiceTime() { return varianceServiceTime; }

    /** It returns the simulated standard deviation of the service time. */
    public double sdServiceTime() { return sdServiceTime; }



    //ERRORS
    /** It returns the error between the simulated and the 
     *  real averange time of a new user arrive. */
    public double averangeArriveError() { 
	return Math.abs( (1 / bC1) - averangeArrive ); 
    }

    /** It returns the error between the simulated and the 
     *  real variance of the time of a new user arrive. */
    public double varianceArriveError() { 
	return Math.abs( ( 1 / ( bC1 * bC1 ) ) - varianceArrive ); 
    }

    /** It returns the error between the simulated and the 
     *  real standard deviation of the time of a new user arrive. */
    public double sdArriveError() { 
	return Math.abs( Math.sqrt( 1 / ( bC1 * bC1 ) ) - sdArrive ); 
    }

    /** It returns the error between the simulated and the
     *  real maximum service time. */
    public double maximumServiceTimeError() { 
	return Math.abs( bC2 - maximumServiceTime );  
    }

    /** It returns the error between the simulated and the
     *  real averange service time. */
    public double averangeServiceTimeError() { 
	return Math.abs( ( (0 + mC2 + bC2) / 3 ) - averangeServiceTime ); 
    }

    /** It returns the error between the simulated and the 
     *  real variance service time. */
    public double varianceServiceTimeError() {
	return Math.abs( ( ((bC2-aC2)*(bC2-aC2) - (mC2-aC2)*(bC2-mC2)) / 18 ) - 
			 varianceServiceTime ); 
    }

    /** It returns the error between the simulated and the 
     *  real standard deviation of the service time. */
    public double sdServiceTimeError() { 
	return Math.abs( Math.sqrt( ((bC2-aC2)*(bC2-aC2) - (mC2-aC2)*(bC2-mC2)) / 18 ) - 
			 sdServiceTime ); 
    }



    //PERCENTUAL OF ERROR (%)
    /** It returns the percentual of error between the simulated 
     *  and the real averange time of a new user arrive. */
    public double averangeArrivePercError() { 
	return ( averangeArriveError() * 100 ) / (1 / bC1);  
    }

    /** It returns the percentual of error between the simulated 
     *  and the real variance of the time of a new user arrive. */
    public double varianceArrivePercError() { 
	return ( varianceArriveError() * 100 ) / ( 1 / ( bC1 * bC1 ) ); 
    }

    /** It returns the percentual of error between the simulated 
     *  and the real standard deviation of the time of a new user arrive. */
    public double sdArrivePercError() { 
	return ( sdArriveError() * 100 ) / Math.sqrt( 1 / ( bC1 * bC1 ) ); 
    }

    /** It returns the percentual of error between the simulated 
     *  and the real maximum service time. */
    public double maximumServiceTimePercError() { 
	return ( maximumServiceTimeError() * 100 ) / bC2;  
    }

    /** It returns the percentual of error between the simulated 
     *  and the real averange service time. */
    public double averangeServiceTimePercError() { 
	return ( averangeServiceTimeError() * 100 ) / ( (0 + mC2 + bC2) / 3 ); }

    /** It returns the percentual of error between the simulated 
     *  and the real variance service time. */
    public double varianceServiceTimePercError() {
	return ( varianceServiceTimeError() * 100 ) / 
	    ( ((bC2-aC2)*(bC2-aC2) - (mC2-aC2)*(bC2-mC2)) / 18 ); 
    }

    /** It returns the percentual of error between the simulated 
     *  and the real standard deviation of the service time. */
    public double sdServiceTimePercError() { 
	return ( sdServiceTimeError() * 100 ) / 
	    Math.sqrt( ((bC2-aC2)*(bC2-aC2) - (mC2-aC2)*(bC2-mC2)) / 18 ); 
    }





    /** It runs a simulated queue. 
     *  This method creates an stochastic history of a simulated queue. */
    public void run() {
	start = Calendar.getInstance();
	boolean stop = false;
	while( !stop ) {
	    if( t1 < t2 ) {                // a new client arrives
		// This provides to put this application in the sleeping state. 
		// In this way, I forbid to block other applications that are running in the O.S.
		try { Thread.sleep(5); }
		catch(InterruptedException e) { System.out.println("Thread interrupted early."); }
		t = t1;
		n1++;
		queue[0][n1] = t;
		if( n1 == n - 1 )           // limit for the queue capacity
		    t1 = getLimit();
		else
		    t1 = t + generateExponential();  // sets the time for the next client
		if( t2 == getLimit() ) {             // at start only the first client can be served immediately
		    n2++;
		    t2 = t + generateTriangular();
		    queue[1][n2] = t;
		}

	    } else {                        // an old client leaves or is served
		if( t2 == getLimit() )           // exit condition
		    stop = true;
		else {                       // a served client goes away
		    t = t2;
		    queue[2][n2] = t;
		}
		if( n1 > n2 ) {              // a waiting client is served
		    n2++;
		    t2 = t + generateTriangular();
		    queue[1][n2] = t;
		}
		else
		    t2 = getLimit();
      	    }
	}
	end = Calendar.getInstance();
	// save the satistics of the simulation in variables
	averangeArrive      = setAverangeArrive();
	varianceArrive      = setVarianceArrive();
	sdArrive            = setSDArrive();
	maximumServiceTime  = setMaximumServiceTime();
	averangeServiceTime = setAverangeServiceTime();
	varianceServiceTime = setVarianceServiceTime();
	sdServiceTime       = setSDServiceTime();
    }

    /** It prints the statistics of the queue using the real parameters of the stochastic variables. */
    public void realStatistics() {
	    System.out.println("[REAL VALUES]" +
			       "\n 1- Averange arrive time: \t" + ( 1 / bC1 ) + " minutes" +
			       "\n 2- Variance arrive time: \t" + ( 1 / ( bC1 * bC1 ) ) + " minutes exp(2)" +
			       "\n 3- Std dev arrive time:  \t" + Math.sqrt( 1 / ( bC1 * bC1 ) ) + " minutes" +
			       "\n 4- Maximum service time: \t" + bC2 + " minutes" +
			       "\n 5- Averange service time:\t" + ( (0 + mC2 + bC2) / 3 ) + " minutes" +
			       "\n 6- Variance service time:\t" + ( ((bC2-aC2)*(bC2-aC2) - (mC2-aC2)*(bC2-mC2)) / 18 ) + 
			       " minutes exp(2)" +
			       "\n 7- Std dev service time: \t" + 
			       Math.sqrt( ((bC2-aC2)*(bC2-aC2) - (mC2-aC2)*(bC2-mC2)) / 18 ) + " minutes");
    }

    /** It prints the statistics of the queue using the simulated parameters of the stochastic variables. */
    public void simulatedStatistics() {   // + percentuale
	System.out.println("[SIMULATED VALUES]" +
			   "\n 1- Averange arrive time: \t" + averangeArrive + " minutes " +
			   "\n 2- Variance arrive time: \t" + varianceArrive + " minutes exp(2) " +
			   "\n 3- Std dev arrive time:  \t" + sdArrive  + " minutes " +
			   "\n 4- Maximum service time: \t" + maximumServiceTime + " minutes " + 
			   "\n 5- Averange service time:\t" + averangeServiceTime + " minutes " + 
			   "\n 6- Variance service time:\t" + varianceServiceTime + " minutes exp(2) " +
			   "\n 7- Std dev service time: \t" + sdServiceTime + " minutes ");
    }

    /** It prints the relative errors and the percentuals of error between simulated and real statistics. */
    public void errorStatistics() {   // + percentuale
	System.out.println("[RELATIVE ERRORS]" +
			   "\n 1- Averange arrive time error:  \t" + averangeArriveError() + 
			   " minutes       \t[" + averangeArrivePercError() + " %]" +
			   "\n 2- Variance arrive time error:  \t" + varianceArriveError() +
			   " minutes exp(2)\t[" + varianceArrivePercError() + " %]" +
			   "\n 3- Std dev arrive time error:   \t" + sdArriveError() +
			   " minutes       \t[" + sdArrivePercError() + " %]" +
			   "\n 4- Maximum service time error:  \t" + maximumServiceTimeError() +
			   " minutes       \t[" + maximumServiceTimePercError() + " %]" +
			   "\n 5- Averange service time error: \t" + averangeServiceTimeError() +
			   " minutes       \t[" + averangeServiceTimePercError() + " %]" +
			   "\n 6- Variance service time error: \t" + varianceServiceTimeError() +
			   " minutes exp(2)\t[" + varianceServiceTimePercError() + " %]" +
			   "\n 7- Std dev service time error:  \t" + sdServiceTimeError() +
			   " minutes       \t[" + sdServiceTimePercError() + " %]");
    }

    /** It prints the stochastic history of the queue. */
    public void printHistory() {
	System.out.println( "User\tArrived (minutes)\tServed (minutes)\tGo Away (minutes)\n" +
			    "----\t-----------------\t----------------\t-----------------\n" );
	for( int j = 0; j < n; j++ ) {
	    System.out.println( "[" + (j+1) + "]\t" + queue[0][j] + "\t" + queue[1][j] + "\t" + queue[2][j] );
	    if( j % 5 == 0) {
		try { Thread.sleep(5); }
		catch(InterruptedException e) { System.out.println("Thread interrupted early."); }
	    }
	}
    }

    /** It returns the string of the stochastic history of the queue. */
    public String toString() {
	String str = "User\tArrived (minutes)\tServed (minutes)\tGo Away (minutes)\n";
	str +=       "----\t-----------------\t----------------\t-----------------\n";
	for( int j = 0; j < n; j++ ) {
	    str = str + "[" +
		String.valueOf( j + 1 )       + "]\t" +
		String.valueOf( queue[0][j] ) + "\t"  + 
		String.valueOf( queue[1][j] ) + "\t"  + 
		String.valueOf( queue[2][j] ) + "\n";
	    if( j % 5 == 0) {
		try { Thread.sleep(5); }
		catch(InterruptedException e) { System.out.println("Thread interrupted early."); }
	    }
	}
	return str;
    }

    /** Print the running time of the simulation. */
    public void printTime() {
	if(start != null && end != null) {
	    int m  = end.get( end.MINUTE ) - start.get( start.MINUTE );
	    int s  = end.get( end.SECOND ) - start.get( start.SECOND );
	    int ms = end.get( end.MILLISECOND ) - start.get( start.MILLISECOND );
	    if( m < 0 ) m = 60 + m;
	    if( s < 0 ) {
		m--;
		s = 60 + s;
	    }
	    if( ms < 0 ) {
		s--;
		ms = 1000 + ms;
	    }
	    System.out.println( "Running time of the simulation: " + m + " min " + s + " sec " + ms + " msec");
	}
    }

    /** The main method. In the main body, the programs asks to user the capacity 
     *  of the queue and the parameters for the two stochastic variables used: 
     *  triangular and exponential casual variables. Then it prints the stochastic 
     *  history generated. */
    public static void main( String[] args ) {
	SimulatedQueue Q = null;
	Integer num = null;
	Double bC1 = null, aC2 = new Double(0.0), mC2 = null, bC2 = null;
	BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

	System.out.print( "\n\t\t*** SIMULATED QUEUE ***\n\n" );

	try {
	    System.out.print( "How is the capacity of the queue that you want to simulate? " );
	    num = new Integer( in.readLine() );
	    Q = new SimulatedQueue( num.intValue() );

	    System.out.print( "Which is the averange number of users in an hour? " );
	    bC1 = new Double( in.readLine() );
	    bC1 = new Double( bC1.doubleValue() / 60 );
	    Q.setExponential( bC1.doubleValue() );

	    System.out.print( "How long is the most common (the mode) service time (minutes)? " );
	    mC2 = new Double( in.readLine() );	    

	    System.out.print( "How long is the maximum service time (minutes)? " );
	    bC2 = new Double( in.readLine() );

	    Q.setTriangular( aC2.doubleValue(), mC2.doubleValue(),  bC2.doubleValue() );
	    Q.run();

	    System.out.println( "\nCasual history of the cadence times of the simulated queue (FIFO):\n" );
	    Q.printHistory();
	    System.out.println();
	    Q.realStatistics();
	    System.out.println();
	    Q.simulatedStatistics();
	    System.out.println();
	    Q.errorStatistics();
	    System.out.println();
	    Q.printTime();
	} 
	catch( IOException e ) { System.out.println("Error: input not read correctly"); }
	catch( NumberFormatException e ) { System.out.println("Error: input must be an integer"); }
	catch( SimulatedQueueException e ) { e.getMessage(); e.printStackTrace();}
	catch( TriangularException e ) { e.getMessage(); e.printStackTrace();}
	catch( ExponentialException e ) { e.getMessage(); e.printStackTrace(); }
    }
} // end class SimulatedQueue
