package sim_queue;
/*
 * MIT License
 * 
 * Copyright (c) 2005 Piero Dalle Pezze
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/

import java.util.*;


/** 
 * This is a queue simulator based on stochastic time events.
 * The aim is to simulate a real queue, like a queue at the post office,
 * where users arrive in a random order. When a client arrives, s/he is
 * the last one who will be served. (FIFO = First In First Out). In particular
 * there are two stochastic factors:
 * 1) the time of arrive,
 * 2) the time of service.
 * Meanwhile, clients arrive and others leave.
 * This simulation shows when a person arrives, is ready to be served, and finally leaves.    
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
	expVar,                  // exponential casual variable
	triVar,                  // triangular casual variable
	expVar_b = 0.0,           // parameter b of expVar
	triVar_a = 0.0,           // parameter a of triVar
	triVar_m = 0.0,           // parameter m of triVar the mode
	triVar_b = 0.0,           // parameter b of triVar
	// statistics after the simulation
	averageArrive = 0.0,
	varianceArrive = 0.0,
	sdArrive = 0.0,
	maximumServiceTime = 0.0,
	averageServiceTime = 0.0,
	varianceServiceTime = 0.0,
	sdServiceTime = 0.0;
    private Random rand;     // the uniform variable
    private Calendar 
	start = null, 
	end = null;  //to misure the running time of the simulation

    //SET STHOCASTIC VARIABLES
    /** Return a triangular casual variable. */
    public double generateTriangular() {
        double y = rand.nextDouble();
        if( y < ((triVar_m - triVar_a) / (triVar_b - triVar_a)) )
            triVar = triVar_a + Math.sqrt( (triVar_b-triVar_a)*(triVar_m-triVar_a)*y );
        else
            triVar = triVar_b - Math.sqrt( (triVar_b-triVar_a)*(triVar_b-triVar_m)*(1-y) );
        return triVar;
    }

    /** Return an exponential casual variable. */
    public double generateExponential() {
        //generate exponential casual variable
        expVar = - ( Math.log( 1-rand.nextDouble() ) / expVar_b );
        return expVar;
    }

    //SET THE STATISTICS
    /** Set the simulated average time of a new user arrive . */
    public double setAverageArrive() {
        double ave = 0.0;              // average
        for( int i = 1; i < n; i++ )
            ave += queue[0][i] - queue[0][i-1];
        ave /= n;
        return ave;
    }

    /** Set the simulated variance of the time of a new user arrive . */
    public double setVarianceArrive() { 
        double b = 1 / setAverageArrive();
        return 1 / (b * b);
    }

    /** Set the simulated standard deviation of 
     *  the time of a new user arrive . */
    public double setSDArrive() { 
        return Math.sqrt( setVarianceArrive() ); 
    }

    /** Return the simulated average service time. */
    public double setAverageServiceTime() {
        double ave = 0.0;              // average
        for( int i = 0; i < n; i++ )
            ave += queue[2][i] - queue[1][i];
        ave /= n;
        return ave;
    }

    /** Set the maximum simulated service time. */
    public double setMaximumServiceTime() {
        double max = 0.0, temp = 0.0;
        for( int i = 0; i < n; i++ ) {
            temp = queue[2][i] - queue[1][i];
            if( temp > max ) 
                max = temp;
        }
        return max;
    }

    /** Set the minimum simulated service time. */
    public double setMinimumServiceTime() {
        double min = queue[2][0] - queue[1][0], temp = 0.0;
        for( int i = 1; i < n; i++ ) {
            temp = queue[2][i] - queue[1][i];
            if( temp < min ) 
                min = temp;
        }
        return min;
    }

    /** Set the simulated variance service time. */
    public double setVarianceServiceTime() {
        double 
            ave = setAverageServiceTime(),
            min = setMinimumServiceTime(),
            max = setMaximumServiceTime(),
            mode = (3 * ave) - min - max;
        return ((max - min)*(max - min) - (mode - min)*(max - mode)) / 18;
    }

    /** Set the simulated standard deviation of the service time. */
    public double setSDServiceTime() { 
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
    /** Return the queue of simulated events */
    public double[][] getQueue() { 
        return queue; 
    }
    
    /** Return the capacity of the queue. */
    public int getN() { 
        return n; 
    }

    /** Return the limit used as infinite. Limit value is 999999999.9 . */
    public double getLimit() { 
        return limit; 
    }

    /** Set the parameter b of the exponential casual variable. 
     *  b parameter must be  > 0. 
     *	@throws ExponentialException if b < 0. */
    public void setExponential( double b ) throws ExponentialException {
        if( b > 0 )
            expVar_b = b;
        else
            throw new ExponentialException();
    }

    /** Set the parameters a, m, b of the triangular casual variable. 
     *  a <= m <= b and a < b. 
     *  @throws TriangularException if not a <= m <= b or not a < b. */
    public void setTriangular( double a, double m, double b ) throws TriangularException { 
        if( a <= m && m <= b && a < b ) {
            triVar_a = a;
            triVar_m = m;
            triVar_b = b;
        } else
            throw new TriangularException();
    }
    

    //RETURN THE STATISTICS
    /** Return the simulated average time of a new user arrive . */
    public double averageArrive() { return averageArrive; }

    /** Return the simulated variance of the time of a new user arrive . */
    public double varianceArrive() { return varianceArrive; }

    /** Return the simulated standard deviation of the time of a new user arrive . */
    public double sdArrive() { return sdArrive; }

    /** Return the simulated average service time. */
    public double averageServiceTime() { return averageServiceTime; }

    /** Return the maximum simulated service time. */
    public double maximumServiceTime() { return maximumServiceTime; }

    /** Return the simulated variance service time. */
    public double varianceServiceTime() { return varianceServiceTime; }

    /** Return the simulated standard deviation of the service time. */
    public double sdServiceTime() { return sdServiceTime; }



    //ERRORS
    /** Return the error between the simulated and the 
     *  real average time of a new user arrive. */
    public double averageArriveError() { 
        return Math.abs( (1 / expVar_b) - averageArrive ); 
    }

    /** Return the error between the simulated and the 
     *  real variance of the time of a new user arrive. */
    public double varianceArriveError() { 
        return Math.abs( ( 1 / ( expVar_b * expVar_b ) ) - varianceArrive ); 
    }

    /** Return the error between the simulated and the 
     *  real standard deviation of the time of a new user arrive. */
    public double sdArriveError() { 
        return Math.abs( Math.sqrt( 1 / ( expVar_b * expVar_b ) ) - sdArrive ); 
    }

    /** Return the error between the simulated and the
     *  real maximum service time. */
    public double maximumServiceTimeError() { 
        return Math.abs( triVar_b - maximumServiceTime );  
    }

    /** Return the error between the simulated and the
     *  real average service time. */
    public double averageServiceTimeError() { 
        return Math.abs( ( (0 + triVar_m + triVar_b) / 3 ) - averageServiceTime ); 
    }

    /** Return the error between the simulated and the 
     *  real variance service time. */
    public double varianceServiceTimeError() {
        return Math.abs( ( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ) - 
                varianceServiceTime ); 
    }

    /** Return the error between the simulated and the 
     *  real standard deviation of the service time. */
    public double sdServiceTimeError() { 
        return Math.abs( Math.sqrt( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ) - 
                sdServiceTime ); 
    }



    //PERCENTUAL OF ERROR (%)
    /** Return the percentual of error between the simulated 
     *  and the real average time of a new user arrive. */
    public double averageArrivePercError() { 
        return ( averageArriveError() * 100 ) / (1 / expVar_b);  
    }

    /** Return the percentual of error between the simulated 
     *  and the real variance of the time of a new user arrive. */
    public double varianceArrivePercError() { 
        return ( varianceArriveError() * 100 ) / ( 1 / ( expVar_b * expVar_b ) ); 
    }

    /** Return the percentual of error between the simulated 
     *  and the real standard deviation of the time of a new user arrive. */
    public double sdArrivePercError() { 
        return ( sdArriveError() * 100 ) / Math.sqrt( 1 / ( expVar_b * expVar_b ) ); 
    }

    /** Return the percentual of error between the simulated 
     *  and the real maximum service time. */
    public double maximumServiceTimePercError() { 
        return ( maximumServiceTimeError() * 100 ) / triVar_b;  
    }

    /** Return the percentual of error between the simulated 
     *  and the real average service time. */
    public double averageServiceTimePercError() { 
        return ( averageServiceTimeError() * 100 ) / ( (0 + triVar_m + triVar_b) / 3 ); 
    }

    /** Return the percentual of error between the simulated 
     *  and the real variance service time. */
    public double varianceServiceTimePercError() {
        return ( varianceServiceTimeError() * 100 ) / 
            ( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ); 
    }

    /** Return the percentual of error between the simulated 
     *  and the real standard deviation of the service time. */
    public double sdServiceTimePercError() { 
        return ( sdServiceTimeError() * 100 ) / 
            Math.sqrt( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ); 
    }





    /** It runs a simulated queue. 
     *  This method creates an stochastic history of a simulated queue. */
    public void run() {
        start = Calendar.getInstance();
        boolean stop = false;
        while( !stop ) {
            if( t1 < t2 ) {                // a new client arrives
                // put this application in the sleeping state. 
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
        averageArrive      = setAverageArrive();
        varianceArrive      = setVarianceArrive();
        sdArrive            = setSDArrive();
        maximumServiceTime  = setMaximumServiceTime();
        averageServiceTime = setAverageServiceTime();
        varianceServiceTime = setVarianceServiceTime();
        sdServiceTime       = setSDServiceTime();
    }

    /** It prints the statistics of the queue using the real parameters of the stochastic variables. */
    public void realStatistics() {
	    System.out.println("[REAL VALUES]" +
			       "\n 1- Average arrive time: \t" + ( 1 / expVar_b ) + " minutes" +
			       "\n 2- Variance arrive time: \t" + ( 1 / ( expVar_b * expVar_b ) ) + " minutes exp(2)" +
			       "\n 3- Std dev arrive time:  \t" + Math.sqrt( 1 / ( expVar_b * expVar_b ) ) + " minutes" +
			       "\n 4- Maximum service time: \t" + triVar_b + " minutes" +
			       "\n 5- Average service time:\t" + ( (0 + triVar_m + triVar_b) / 3 ) + " minutes" +
			       "\n 6- Variance service time:\t" + ( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ) + 
			       " minutes exp(2)" +
			       "\n 7- Std dev service time: \t" + 
			       Math.sqrt( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ) + " minutes");
    }

    /** It prints the statistics of the queue using the simulated parameters of the stochastic variables. */
    public void simulatedStatistics() {   // + percentuale
        System.out.println("[SIMULATED VALUES]" +
                "\n 1- Average arrive time: \t" + averageArrive + " minutes " +
                "\n 2- Variance arrive time: \t" + varianceArrive + " minutes exp(2) " +
                "\n 3- Std dev arrive time:  \t" + sdArrive  + " minutes " +
                "\n 4- Maximum service time: \t" + maximumServiceTime + " minutes " + 
                "\n 5- Average service time:\t" + averageServiceTime + " minutes " + 
                "\n 6- Variance service time:\t" + varianceServiceTime + " minutes exp(2) " +
                "\n 7- Std dev service time: \t" + sdServiceTime + " minutes ");
    }

    /** It prints the relative errors and the percentuals of error between simulated and real statistics. */
    public void errorStatistics() {   // + percentuale
        System.out.println("[RELATIVE ERRORS]" +
                "\n 1- Average arrive time error:  \t" + averageArriveError() + 
                " minutes       \t[" + averageArrivePercError() + " %]" +
                "\n 2- Variance arrive time error:  \t" + varianceArriveError() +
                " minutes exp(2)\t[" + varianceArrivePercError() + " %]" +
                "\n 3- Std dev arrive time error:   \t" + sdArriveError() +
                " minutes       \t[" + sdArrivePercError() + " %]" +
                "\n 4- Maximum service time error:  \t" + maximumServiceTimeError() +
                " minutes       \t[" + maximumServiceTimePercError() + " %]" +
                "\n 5- Average service time error: \t" + averageServiceTimeError() +
                " minutes       \t[" + averageServiceTimePercError() + " %]" +
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

    /** Return the string of the stochastic history of the queue. */
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
            int m  = end.get( Calendar.MINUTE ) - start.get( Calendar.MINUTE );
            int s  = end.get( Calendar.SECOND ) - start.get( Calendar.SECOND );
            int ms = end.get( Calendar.MILLISECOND ) - start.get( Calendar.MILLISECOND );
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

} // end class SimulatedQueue
