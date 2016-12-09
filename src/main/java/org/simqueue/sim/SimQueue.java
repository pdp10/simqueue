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
    
    /** Exponential stochastic variable simulating the client arrival time. */
    protected ExponentialVariable expVar = null;

    /** Triangular stochastic variable simulating the client service time. */
    protected TriangularVariable triVar = null;      
    
    /** Statistics for this queue */
    protected BasicStatistics stats = new BasicStatistics();
    
    
    /** 
     * Constructor. Build a queue of size queueLength.
     * 
     * @param queueLength the length of the queue
     * @throws QueueSimulationException if queueLength < 1. 
     */
    public SimQueue(int queueLength) throws SimQueueException{
        if( queueLength > 0 ) {
            queue = new double[3][queueLength];
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
     * Constructor. Build a queue of size queueLength. 
     * 
     * @param queueLength the length of the queue
     * @param lambda the lambda parameter
     * @param a the a parameter (min)
     * @param b the m parameter (mode)
     * @param c the b parameter (max)
     * @throws QueueSimulationException if queueLength < 1. 
     * @throws ExponentialException if expLambda < 0. 
     * @throws TriangularException if not a <= m <= b or not a < b.
     */
    public SimQueue(int queueLength, double lambda, double a, double m, double b) 
    		throws SimQueueException, ExponentialException, TriangularException {
        if( queueLength > 0 ) {
            queue = new double[3][queueLength];
            expVar = new ExponentialVariable(lambda);
            triVar = new TriangularVariable(a, m, b);
        } else
            throw new SimQueueException();
    }
    
    /** 
     * Set the exponential stochastic variable simulating the client arrival time. 
     * 
     * @param lambda the lambda parameter
     * @throws ExponentialException if lambda < 0. 
     */
    public void setExponentialVariable(double lambda) throws ExponentialException {
    	expVar = new ExponentialVariable(lambda);
    }
    
    /** 
     * Set the triangular stochastic variable simulating the client service time. 
     * 
     * @param a the a parameter (min)
     * @param b the m parameter (mode)
     * @param c the b parameter (max)
     * @throws TriangularException if not a <= m <= b or not a < b.
     */
    public void setTriangularVariable(double a, double m, double b) throws TriangularException {
    	triVar = new TriangularVariable(a, m, b);
    }
    
    /** Return the queue of simulated events */
    public double[][] getQueue() { 
        return queue; 
    }
    
    /** 
     * Return the clients arrival times
     * 
     * @return the arrival times
     */
    public double[] getArrivalTimes() {
    	return queue[0];
    }
    
    /** 
     * Return the clients initial service times
     * 
     * @return the service times
     */
    public double[] getServingTimes() {
    	return queue[1];
    }
    
    /** 
     * Return the clients leaving times
     * 
     * @return the leaving times
     */
    public double[] getLeavingTimes() {
    	return queue[2];
    }    
    
    /**
     * Return the distribution samples for the arrival times
     * 
     * @return the distribution samples.
     */
    public double[] getArrivalTimesSamples() {
    	double[] samples = new double[queue[0].length];
		samples[0] = queue[0][0];
    	for(int i = 1; i < samples.length; i++) {
    		samples[i] = queue[0][i] - queue[0][i-1];
    	}
    	return samples;
    }
    
    /**
     * Return the distribution samples for the service times. 
     * These are the lengths of service times.
     * 
     * @return the distribution samples.
     */
    public double[] getServiceTimesSamples() {
    	double[] samples = new double[queue[0].length];
    	for(int i = 0; i < samples.length; i++) {
    		samples[i] = queue[2][i] - queue[1][i];
    	}
    	return samples;
    }
    
    /** Return the capacity of the queue. */
    public int getN() { 
        return queue[0].length; 
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
   	
    	// Populate the clients arrival times. 
    	// This history is memoryless and independent of the service time.
    	// The simulation starts when the first client arrives. This is time 0.
    	queue[0][0] = 0;
    	for(int i=1; i < queue[0].length; i++) {
    		queue[0][i] = queue[0][i-1] + clientArrives();
    	}
    	
    	// Clients are now served in a FIFO policy.
    	// This history is memoryless, but also depends on client arrival time
    	// The first client will be served immediately
    	queue[1][0] = 0;
    	queue[2][0] = queue[1][0] + clientIsBeingServed();    	
    	for(int i=1; i < queue[0].length; i++) {
    		if(queue[0][i] < queue[2][i-1]) {
    			// A client is waiting
    			// the client is served immediately (gap times are discarded)
            	queue[1][i] = queue[2][i-1];
    		} else {
    			// No client is waiting
    			// the server must wait until the next client arrives
    			queue[1][i] = queue[0][i];
    		}
    		// the client is served
        	queue[2][i] = queue[1][i] + clientIsBeingServed();
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
			       "\n 4- Maximum service time: \t" + triVar.getB() + " min" +
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
                "\n 4- Maximum service time error:  \t" + stats.maxServiceTimeError(triVar.getB()) + " min " +
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
        for( int j = 0; j < queue[0].length; j++ ) {
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
