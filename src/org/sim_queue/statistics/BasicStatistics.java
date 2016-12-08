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

/** 
 * A class for computing basic statistics from the queue simulation.
 * Errors between the theoretical and practical statistics are also computed.
 */
public class BasicStatistics {

	private double meanArrivalTime = 0d;
	private double varArrivalTime = 0d;
	private double sdArrivalTime = 0d;
	private double minServiceTime = 0d;	
	private double maxServiceTime = 0d;
	private double meanServiceTime = 0d;
	private double varServiceTime = 0d;
	private double sdServiceTime = 0d;
	
	private double meanArrivalTimeError = 0d;
	private double varArrivalTimeError = 0d;
	private double sdArrivalTimeError = 0d;
	private double minServiceTimeError = 0d;	
	private double maxServiceTimeError = 0d;
	private double meanServiceTimeError = 0d;
	private double varServiceTimeError = 0d;
	private double sdServiceTimeError = 0d;	

	/** Default constructor */
	public BasicStatistics() {

	}

	/** Reset the statistics */
	public void reset() {
		meanArrivalTime = 0d;
		varArrivalTime = 0d;
		sdArrivalTime = 0d;
		maxServiceTime = 0d;
		meanServiceTime = 0d;
		varServiceTime = 0d;
		sdServiceTime = 0d;
		
		meanArrivalTimeError = 0d;
		varArrivalTimeError = 0d;
		sdArrivalTimeError = 0d;
		maxServiceTimeError = 0d;
		meanServiceTimeError = 0d;
		varServiceTimeError = 0d;
		sdServiceTimeError = 0d;
	}

	/** Return the queue length */
	private int getQueueLength(double[][] queue) {
		if (queue != null && queue.length > 0) {
			return queue[0].length;
		}
		return 0;
	}

	// setter methods
	
	/** Set the simulated mean time of a new user arrive . */
	public void setMeanArrivalTime(double[][] queue) {
        if(meanArrivalTime != 0d) { 
        	return;
        }
        int n = getQueueLength(queue);
        if(n == 0) { 
        	return; 
        }
        for( int i = 1; i < n; i++ )
            meanArrivalTime += queue[0][i] - queue[0][i-1];
	    meanArrivalTime /= n;
    }

	/** Set the simulated variance of the time of a new user arrive . */
	public void setVarArrivalTime(double[][] queue) {
        if(varArrivalTime != 0d) { 
        	return;
        }
		int n = getQueueLength(queue);
		if (n == 0) {
			return;
		}
		setMeanArrivalTime(queue);
		if(meanArrivalTime == 0d) {
			varArrivalTime = 0d;
		} else {
			double b = 1 / meanArrivalTime;
			varArrivalTime = 1 / (b * b);
		}
	}

	/**
	 * Set the simulated standard deviation of the time of a new user arrive .
	 */
	public void setSDArrivalTime(double[][] queue) {
        if(sdArrivalTime != 0d) { 
        	return;
        }
        setVarArrivalTime(queue);
		sdArrivalTime = Math.sqrt(varArrivalTime);
	}

	/** Set the maximum simulated service time. */
	public void setMaxServiceTime(double[][] queue) {
        if(maxServiceTime != 0d) { 
        	return;
        }		
		double temp = 0.0;
		int n = getQueueLength(queue);
		if (n == 0) {
			return;
		}
		for (int i = 0; i < n; i++) {
			temp = queue[2][i] - queue[1][i];
			if (temp > maxServiceTime)
				maxServiceTime = temp;
		}
	}

	/** Set the minimum simulated service time. */
	public void setMinServiceTime(double[][] queue) {
        if(minServiceTime != 0d) { 
        	return;
        }	
		int n = getQueueLength(queue);
		if (n == 0) {
			return;
		}
		minServiceTime = queue[2][0] - queue[1][0];
		double temp = 0.0;
		for (int i = 1; i < n; i++) {
			temp = queue[2][i] - queue[1][i];
			if (temp < minServiceTime)
				minServiceTime = temp;
		}
	}

	/** Return the simulated mean service time. */
	public void setMeanServiceTime(double[][] queue) {
        if(meanServiceTime != 0d) { 
        	return;
        }		
		int n = getQueueLength(queue);
		if (n == 0) {
			return;
		}
		for (int i = 0; i < n; i++)
			meanServiceTime += queue[2][i] - queue[1][i];
		meanServiceTime /= n;
	}	
	
	/** Set the simulated variance service time. */
	public void setVarServiceTime(double[][] queue) {
        if(varServiceTime != 0d) { 
        	return;
        }
        setMeanServiceTime(queue);
        setMinServiceTime(queue);
        setMaxServiceTime(queue);
		double ave = meanServiceTime, 
			   min = minServiceTime, 
			   max = maxServiceTime, 
			   mode = (3 * ave) - min - max;
		varServiceTime = ((max - min) * (max - min) - (mode - min) * (max - mode)) / 18;
	}

	/** Set the simulated standard deviation of the service time. */
	public void setSDServiceTime(double[][] queue) {
        if(sdServiceTime != 0d) { 
        	return;
        }
        setVarServiceTime(queue);
		sdServiceTime = Math.sqrt(varServiceTime);
	}

	// Getter Methods
	/** Return the simulated mean time of a new user arrive . */
	public double getMeanArrivalTime() {
		return meanArrivalTime;
	}

	/** Return the simulated variance of the time of a new user arrive . */
	public double getVarArrivalTime() {
		return varArrivalTime;
	}

	/**
	 * Return the simulated standard deviation of the time of a new user arrive.
	 */
	public double getSDArrivalTime() {
		return sdArrivalTime;
	}

	/** Return the simulated mean service time. */
	public double getMeanServiceTime() {
		return meanServiceTime;
	}

	/** Return the maximum simulated service time. */
	public double getMaxServiceTime() {
		return maxServiceTime;
	}

	/** Return the simulated variance service time. */
	public double getVarServiceTime() {
		return varServiceTime;
	}

	/** Return the simulated standard deviation of the service time. */
	public double getSDServiceTime() {
		return sdServiceTime;
	}

	// ERRORS
	/**
	 * Return the error between the simulated and the real mean time of a new
	 * user arrive.
	 */
	public double meanArrivalTimeError(double exactMeanArrivalTime) {
		meanArrivalTimeError = Math.abs(exactMeanArrivalTime - meanArrivalTime);
		return meanArrivalTimeError;
	}

	/**
	 * Return the error between the simulated and the real variance of the time
	 * of a new user arrive.
	 */
	public double varArrivalTimeError(double exactVarArrivalTime) {
		varArrivalTimeError = Math.abs(exactVarArrivalTime - varArrivalTime);
		return varArrivalTimeError;
	}

	/**
	 * Return the error between the simulated and the real standard deviation of
	 * the time of a new user arrive.
	 */
	public double sdArrivalTimeError(double exactSDArrivalTime) {
		sdArrivalTimeError = Math.abs(exactSDArrivalTime - sdArrivalTime);
		return sdArrivalTimeError;
	}

	/**
	 * Return the error between the simulated and the real maximum service time.
	 */
	public double maxServiceTimeError(double exactMaxServiceTime) {
		maxServiceTimeError = Math.abs(exactMaxServiceTime - maxServiceTime);
		return maxServiceTimeError;
	}

	/**
	 * Return the error between the simulated and the real mean service time.
	 */
	public double meanServiceTimeError(double exactMeanServiceTime) {
		meanServiceTimeError = Math.abs(exactMeanServiceTime - meanServiceTime);
		return meanServiceTimeError;
	}

	/**
	 * Return the error between the simulated and the real variance service
	 * time.
	 */
	public double varServiceTimeError(double exactVarServiceTime) {
		varServiceTimeError = Math.abs(exactVarServiceTime	- varServiceTime);
		return varServiceTimeError;
	}

	/**
	 * Return the error between the simulated and the real standard deviation of
	 * the service time.
	 */
	public double sdServiceTimeError(double exactSDServiceTime) {
		sdServiceTimeError = Math.abs(exactSDServiceTime - sdServiceTime);
		return sdServiceTimeError;
	}

	// percent OF ERROR (%)
	/**
	 * Return the percent of error of the mean arrival time.
	 */
	public double meanArrivalTimePercError() {
		return (meanArrivalTimeError * 100) / meanArrivalTime;
	}

	/**
	 * Return the percent of error of the var arrival time.
	 */
	public double varArrivalTimePercError() {
		return (varArrivalTimeError * 100) / varArrivalTime;
	}

	/**
	 * Return the percent of error of the sd arrival time.
	 */
	public double sdArrivalTimePercError() {
		return (sdArrivalTimeError * 100) / sdArrivalTime;
	}

	/**
	 * Return the percent of error of the maximum service time.
	 */
	public double maxServiceTimePercError() {
		return (maxServiceTimeError * 100) / maxServiceTime;
	}

	/**
	 * Return the percent of error of the mean service time.
	 */
	public double meanServiceTimePercError() {
		return (meanServiceTimeError * 100)
				/ meanServiceTime;
	}

	/**
	 * Return the percent of error of the var service time.
	 */
	public double varServiceTimePercError() {
		return (varServiceTimeError * 100)
				/ varServiceTime;
	}

	/**
	 * Return the percent of error of the sd service time.
	 */
	public double sdServiceTimePercError() {
		return (sdServiceTimeError * 100)
				/ sdServiceTime;
	}

}
