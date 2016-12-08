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
public class SimQueue implements Runnable {

    private double[][] queue; // the stochastic history 
    private int
	n = 1,               // the number of person to server 
	n1 = -1,             // the name of the last entered. no client
	n2 = -1;             // the name of the last served. no client
    private double
	t = 0.0,             // time: it is the actual time
	t1 = 0.0,            // time: it shows when the next client will arrive 
	t2 = 999999999.9,    // time: it shows when has finished his activity
	limit = 999999999.9, //infinite
	expVar,                  // exponential stochastic variable
	triVar,                  // triangular stochastic variable
	expVar_b = 0.0,           // parameter b of expVar
	triVar_a = 0.0,           // parameter a of triVar
	triVar_m = 0.0,           // parameter m of triVar the mode
	triVar_b = 0.0;           // parameter b of triVar

    private Random rand;     // the uniform variable
    private Calendar 
	start = null, 
	end = null;  // measure the running time of the simulation
    
    BasicStatistics stats = new BasicStatistics();

    //SET STHOCASTIC VARIABLES
    /** Return a triangular stochastic variable. */
    public double generateTriangular() {
        double y = rand.nextDouble();
        if( y < ((triVar_m - triVar_a) / (triVar_b - triVar_a)) )
            triVar = triVar_a + Math.sqrt( (triVar_b-triVar_a)*(triVar_m-triVar_a)*y );
        else
            triVar = triVar_b - Math.sqrt( (triVar_b-triVar_a)*(triVar_b-triVar_m)*(1-y) );
        return triVar;
    }

    /** Return an exponential stochastic variable. */
    public double generateExponential() {
        //generate exponential stochastic variable
        expVar = - ( Math.log( 1-rand.nextDouble() ) / expVar_b );
        return expVar;
    }

    /** Constructor. Build a queue of size _n. 
     * @throws QueueSimulationException if _n < 1. */
    public SimQueue( int _n ) throws SimQueueException {
        if( _n > 0 ) {
            n = _n;
            queue = new double[3][n];
            rand = new Random();
        } else
            throw new SimQueueException();
    }

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

    /** Set the parameter b of the exponential stochastic variable. 
     *  b parameter must be  > 0. 
     *	@throws ExponentialException if b < 0. */
    public void setExponential( double b ) throws ExponentialException {
        if( b > 0 )
            expVar_b = b;
        else
            throw new ExponentialException();
    }

    /** Set the parameters a, m, b of the triangular stochastic variable. 
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


    /** Run a simulated queue. 
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
        
        // calculate the statistics
        stats.setMeanArrivalTime(queue);
        stats.setVarArrivalTime(queue);
        stats.setSDArrivalTime(queue);
        stats.setMaxServiceTime(queue);
        stats.setMinServiceTime(queue);
        stats.setMeanServiceTime(queue);
        stats.setVarServiceTime(queue);
        stats.setSDServiceTime(queue);
    }

    /** Print the statistics of the queue using the real parameters of the stochastic variables. */
    public void printRealStatistics() {
	    System.out.println("[REAL VALUES]" +
			       "\n 1- Mean arrive time: \t\t" + ( 1 / expVar_b ) + " min" +
			       "\n 2- Variance arrive time: \t" + ( 1 / ( expVar_b * expVar_b ) ) + " min^2" +
			       "\n 3- Std dev arrive time:  \t" + Math.sqrt( 1 / ( expVar_b * expVar_b ) ) + " min" +
			       "\n 4- Maximum service time: \t" + triVar_b + " min" +
			       "\n 5- Mean service time:\t\t" + ( (0 + triVar_m + triVar_b) / 3 ) + " min" +
			       "\n 6- Variance service time:\t" + ( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ) + 
			       " min^2" +
			       "\n 7- Std dev service time: \t" + 
			       Math.sqrt( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ) + " min");
    }

    /** Print the statistics of the queue using the simulated parameters of the stochastic variables. */
    public void printSimulatedStatistics() {   // + percent
        System.out.println("[SIMULATED VALUES]" +
                "\n 1- Mean arrive time: \t\t" + stats.getMeanArrivalTime() + " min " +
                "\n 2- Variance arrive time: \t" + stats.getVarArrivalTime() + " min^2 " +
                "\n 3- Std dev arrive time:  \t" + stats.getSDArrivalTime() + " min " +
                "\n 4- Maximum service time: \t" + stats.getMaxServiceTime() + " min " + 
                "\n 5- Mean service time:\t\t" + stats.getMeanServiceTime() + " min " + 
                "\n 6- Variance service time:\t" + stats.getVarServiceTime() + " min^2 " +
                "\n 7- Std dev service time: \t" + stats.getSDServiceTime() + " min ");
    }

    /** Print the relative errors and the percents of error between simulated and real statistics. */
    public void printErrorStatistics() {   // + percent
        System.out.println("[RELATIVE ERRORS]" +
                "\n 1- Mean arrive time error:  \t\t" + stats.meanArrivalTimeError((1 / expVar_b)) + 
                " min (" + (float)stats.meanArrivalTimePercError() + " %)" +
                "\n 2- Variance arrive time error:  \t" + stats.varArrivalTimeError((1 / (expVar_b * expVar_b))) +
                " min^2 (" + (float)stats.varArrivalTimePercError() + " %)" +
                "\n 3- Std dev arrive time error:   \t" + stats.sdArrivalTimeError(Math.sqrt(1 / (expVar_b * expVar_b))) +
                " min (" + (float)stats.sdArrivalTimePercError() + " %)" +
                "\n 4- Maximum service time error:  \t" + stats.maxServiceTimeError(triVar_b) +
                " min (" + (float)stats.maxServiceTimePercError() + " %)" +
                "\n 5- Mean service time error: \t\t" + stats.meanServiceTimeError(((0 + triVar_m + triVar_b) / 3)) +
                " min (" + (float)stats.meanServiceTimePercError() + " %)" +
                "\n 6- Variance service time error: \t" + stats.varServiceTimeError((((triVar_b - triVar_a) * (triVar_b - triVar_a) - (triVar_m - triVar_a)
						* (triVar_b - triVar_m)) / 18)) +
                " min^2 (" + (float)stats.varServiceTimePercError() + " %)" +
                "\n 7- Std dev service time error:  \t" + stats.sdServiceTimeError(Math.sqrt(((triVar_b - triVar_a)
        				* (triVar_b - triVar_a) - (triVar_m - triVar_a)
        				* (triVar_b - triVar_m)) / 18)) +
                " min (" + (float)stats.sdServiceTimePercError() + " %)");
    }

    /** Print the stochastic history of the queue. */
    public void printHistory() {
        System.out.println( "User\tArrival Time (min)\tServing Time (min)\tLeaving Time (min)\n" +
                    "----\t-----------------\t----------------\t-----------------\n" );
        for( int j = 0; j < n; j++ ) {
            System.out.println( "[" + (j+1) + "]\t" + queue[0][j] + "\t" + queue[1][j] + "\t" + queue[2][j] );
            if( j % 5 == 0) {
                try { Thread.sleep(5); }
                catch(InterruptedException e) { System.out.println("Thread interrupted early."); }
            }
        }
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
            System.out.println( "Running time of the simulation: " + m + " min " + s + " s " + ms + " ms");
        }
    }

} // end class SimQueue
