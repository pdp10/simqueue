package org.simqueue.sim;
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

import org.simqueue.exception.ExponentialException;
import org.simqueue.exception.SimQueueException;
import org.simqueue.exception.TriangularException;
import org.simqueue.random.ExponentialVariable;
import org.simqueue.random.TriangularVariable;
import org.simqueue.statistics.BasicStatistics;


/** 
 * This is a queue simulator based on stochastic time events.
 * The aim is to simulate a real queue, like a queue at the post office,
 * where users arrive in a random order. When a client arrives, s/he is
 * the last one who will be served. Therefore, this is a FIFO (First In First Out) queue.
 * For each client there are two stochastic events: arrival and service times. 
 * In this simulation, the arrival time is sampled from an exponential distribution, 
 * whereas the service time from a triangular distribution. 
 * This simulation shows when a person arrives, is ready to be served, and finally leaves.    
 */
public class SimQueue {

	/** The history of stochastic queue events. */ 
    private double[][] queue;
    
    /** The number of clients to serve. */
    private int totalClientsToServe = 1;
    
    /** The number of arrived clients. At start no client. 
     * Note: 0 is the first client. */
    private int numClientsArrived = -1;
    
    /** The number of served clients. At start no client. 
     * Note: 0 is the first client. */
    private int numClientsServed = -1;
    
    /** The current simulation time. */
    private double currentTime = 0.0d;
    
    /** The time the next client is about to arrive. */ 
    private double timeNextClientArrives = 0.0d;
    
    /** The time the client being served will leave. */
    private double timeServedClientLeaves = Double.MAX_VALUE;   
    
    /** The maximum time for this simulation. */
    public static final double MAX_SIM_TIME = Double.MAX_VALUE; 
    
    /** Exponential stochastic variable simulating the client arrival time. */
    protected ExponentialVariable expVar = null;

    /** Triangular stochastic variable simulating the client service time. */
    protected TriangularVariable triVar = null;      
    
    /** Statistics for this queue */
    protected BasicStatistics stats = new BasicStatistics();

    
    /** 
     * Constructor. Build a queue of size _n.
     * 
     * @throws QueueSimulationException if _n < 1. 
     */
    public SimQueue(int _n) throws SimQueueException{
        if( _n > 0 ) {
            totalClientsToServe = _n;
            queue = new double[3][totalClientsToServe];
            try {
            	expVar = new ExponentialVariable(1);
            	triVar = new TriangularVariable(0, 1, 2);
            } catch(ExponentialException e) { 
            	// we won't ever reach this
            } catch(TriangularException e) { 
            	// we won't ever reach this
            }
        } else
            throw new SimQueueException();
    }    

    /** 
     * Constructor. Build a queue of size _n. 
     * 
     * @throws QueueSimulationException if _n < 1. 
     * @throws ExponentialException if expB < 0. 
     * @throws TriangularException if not a <= m <= b or not a < b.
     */
    public SimQueue(int _n, double expB, double triA, double triM, double triB) 
    		throws SimQueueException, ExponentialException, TriangularException {
        if( _n > 0 ) {
            totalClientsToServe = _n;
            queue = new double[3][totalClientsToServe];
            expVar = new ExponentialVariable(expB);
            triVar = new TriangularVariable(triA, triM, triB);
        } else
            throw new SimQueueException();
    }
    
    /** 
     * Set the exponential stochastic variable simulating the client arrival time. 
     * 
     * @throws ExponentialException if expB < 0. 
     */
    public void setExponentialVariable(double expB) throws ExponentialException {
    	expVar = new ExponentialVariable(expB);
    }
    
    /** 
     * Set the triangular stochastic variable simulating the client service time. 
     * 
     * @throws TriangularException if not a <= m <= b or not a < b.
     */
    public void setTriangularVariable(double triA, double triM, double triB) throws TriangularException {
    	triVar = new TriangularVariable(triA, triM, triB);
    }
    
    /** Return the queue of simulated events */
    public double[][] getQueue() { 
        return queue; 
    }
    
    /** Return the capacity of the queue. */
    public int getN() { 
        return totalClientsToServe; 
    }
    
    /**
     * Return the statistics for this simulation
     * @return statistics
     */
    public BasicStatistics getStatistics() {
    	return stats;
    }
    
    /** 
     * Create a stochastic queue simulation.
     */
    public void run() {
        boolean stop = false;
        while( !stop ) {
            if( timeNextClientArrives < timeServedClientLeaves ) {                // a new client arrives
                // put this application in the sleeping state. 
                try { Thread.sleep(5); }
                catch(InterruptedException e) { System.err.println("Thread interrupted early."); }
                currentTime = timeNextClientArrives;
                numClientsArrived++;
                queue[0][numClientsArrived] = currentTime;
                if( numClientsArrived == totalClientsToServe - 1 )           // limit for the queue capacity
                    timeNextClientArrives = MAX_SIM_TIME;
                else
                    timeNextClientArrives = currentTime + clientArrives();  // sets the time for the next client
                if( timeServedClientLeaves == MAX_SIM_TIME ) {             // at start only the first client can be served immediately
                    numClientsServed++;
                    timeServedClientLeaves = currentTime + clientIsBeingServed();
                    queue[1][numClientsServed] = currentTime;
                }

            } else {                        // an old client leaves or is served
                if( timeServedClientLeaves == MAX_SIM_TIME )    // exit condition
                    stop = true;
                else {                       // a served client goes away
                    currentTime = timeServedClientLeaves;
                    queue[2][numClientsServed] = currentTime;
                }
                if( numClientsArrived > numClientsServed ) {              // a waiting client is served
                    numClientsServed++;
                    timeServedClientLeaves = currentTime + clientIsBeingServed();
                    queue[1][numClientsServed] = currentTime;
                }
                else
                    timeServedClientLeaves = MAX_SIM_TIME;
            }
        }
        
        // calculate the statistics
        computeStatistics();
    }

    /** 
     * Return a string containing the statistics for this queue using 
     * the parameters of the stochastic variables. 
     * 
     * @return the theoretical statistics string
     */
    public String getTheoreticalStatisticsString() {
	    return "[THEORETICAL VALUES]" +
			       "\n 1- Mean arrive time: \t\t" + expVar.getTheoreticalMean() + " min" +
			       "\n 2- Variance arrive time: \t" + expVar.getTheoreticalVar() + " min^2" +
			       "\n 3- Std dev arrive time:  \t" + expVar.getTheoreticalSD() + " min" +
			       "\n 4- Maximum service time: \t" + triVar.getb() + " min" +
			       "\n 5- Mean service time:\t\t" + triVar.getTheoreticalMean() + " min" +
			       "\n 6- Variance service time:\t" + triVar.getTheoreticalVar() + " min^2" +
			       "\n 7- Std dev service time: \t" + triVar.getTheoreticalSD() + " min";
    }

    /** 
     * Return a string containing the statistics for this queue using 
     * the simulated parameters of the stochastic variables. 
     * 
     * @return the simulated statistics string
     */
    public String getSimulatedStatisticsString() {   // + percent
        return "[SIMULATED VALUES]" +
                "\n 1- Mean arrive time: \t\t" + stats.getMeanArrivalTime() + " min " +
                "\n 2- Variance arrive time: \t" + stats.getVarArrivalTime() + " min^2 " +
                "\n 3- Std dev arrive time:  \t" + stats.getSDArrivalTime() + " min " +
                "\n 4- Maximum service time: \t" + stats.getMaxServiceTime() + " min " + 
                "\n 5- Mean service time:\t\t" + stats.getMeanServiceTime() + " min " + 
                "\n 6- Variance service time:\t" + stats.getVarServiceTime() + " min^2 " +
                "\n 7- Std dev service time: \t" + stats.getSDServiceTime() + " min ";
    }

    /** 
     * Return the relative errors and the percents of error 
     * between simulated and theoretical statistics.
     *  
     * @return the string containing the errors 
     */
    public String getErrorStatisticsString() {   // + percent
        return "[RELATIVE ERRORS]" +
                "\n 1- Mean arrive time error:  \t\t" + stats.meanArrivalTimeError(expVar.getTheoreticalMean()) + " min " +
                "\n 2- Variance arrive time error:  \t" + stats.varArrivalTimeError(expVar.getTheoreticalVar()) + " min^2 " +
                "\n 3- Std dev arrive time error:   \t" + stats.sdArrivalTimeError(expVar.getTheoreticalSD()) + " min " +
                "\n 4- Maximum service time error:  \t" + stats.maxServiceTimeError(triVar.getb()) + " min " +
                "\n 5- Mean service time error: \t\t" + stats.meanServiceTimeError(triVar.getTheoreticalMean()) + " min " +
                "\n 6- Variance service time error: \t" + stats.varServiceTimeError(triVar.getTheoreticalVar()) + " min^2 " +
                "\n 7- Std dev service time error:  \t" + stats.sdServiceTimeError(triVar.getTheoreticalSD()) + " min ";
    }

    /** 
     * Return the string containing the stochastic history for this queue. 
     * 
     * @return the history string
     */
    public String getHistoryString() {
        String s = "Client\tArrival Time (min)\tServing Time (min)\tLeaving Time (min)\n" +
                    		"------\t------------------\t------------------\t------------------\n";
        for( int j = 0; j < totalClientsToServe; j++ ) {
            s += "[" + (j+1) + "]\t" + queue[0][j] + "\t" + queue[1][j] + "\t" + queue[2][j] + "\n";
            if( j % 5 == 0) {
                try { Thread.sleep(5); }
                catch(InterruptedException e) { System.err.println("Thread interrupted early."); }
            }
        }
        return s;
    }
    
    /** 
     * Return the arrival time for the next client. 
     * This event is extracted from an exponential distribution. 
     * 
     * @return the time the next client arrives.
     */
    protected double clientArrives() {
    	return expVar.getNext();
    }

    /** 
     * Return the time the next client is being served. 
     * This event is extracted from a triangular distribution.
     * 
     * @return the time the next client is being served.
     */
    protected double clientIsBeingServed() {
    	return triVar.getNext();
    }    
    
    /** 
     * Compute the statistics for this simulation.
     */
    protected void computeStatistics() {
        stats.setMeanArrivalTime(queue);
        stats.setVarArrivalTime(queue);
        stats.setSDArrivalTime(queue);
        stats.setMaxServiceTime(queue);
        stats.setMinServiceTime(queue);
        stats.setMeanServiceTime(queue);
        stats.setVarServiceTime(queue);
        stats.setSDServiceTime(queue);
    }

} // end class SimQueue
