package org.simqueue;

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

import java.io.*;
import java.util.Calendar;

import org.simqueue.exception.ExponentialException;
import org.simqueue.exception.SimQueueException;
import org.simqueue.exception.TriangularException;
import org.simqueue.sim.SimQueue;
import org.simqueue.utils.ElapsedTime;

/**
 * Main class used for testing SimQueue.
 */
public class MainTest {

	/**
	 * The programs automatically sets a queue size and the parameters for the
	 * two used stochastic variables: 1) triangular variable; 2) exponential
	 * variable. Then, the stochastic history is generated and shown.
	 */
	public static void main(String[] args) {

		String fileout = "simqueue.csv";
		if (args.length > 0) {
			fileout = args[0];
		}
		
		// Used for computing the start and end times.
		Calendar start = null;
		Calendar end = null;

		SimQueue Q = null;

		// Queue Capacity
		int num = 50;

		// average number of customers per minute
		double expVar_b = 1.6d;

		// length of the most common (the mode) service time [min]
		double triVar_m = 5d;

		// length of the shortest service time [min] (no service)
		double triVar_a = 0.0d;

		// length of the maximum service time [min]
		double triVar_b = 60d;

		try {
			Q = new SimQueue(num, expVar_b, triVar_a, triVar_m, triVar_b);
			
			start = Calendar.getInstance();
			Q.run();
			end = Calendar.getInstance();

			System.out.println(Q.getHistoryString());
			System.out.println();
			System.out.println(Q.getTheoreticalStatisticsString());
			System.out.println();
			System.out.println(Q.getSimulatedStatisticsString());
			System.out.println();
			System.out.println(Q.getErrorStatisticsString());
			System.out.println();
			System.out.println(ElapsedTime.compute(start, end));

			// get the queue of events (arrival, service, and leave times)
			double[][] queue = Q.getQueue();

			// write the queue to file
			try (PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(fileout, false)))) {
				// out.println("Client\tArrivalTime\tServiceTime\tLeavingTime");
				// for( int j = 0; j < queue[0].length; j++ ) {
				// out.println(j + "\t" + queue[0][j] + "\t" + queue[1][j] +
				// "\t" + queue[2][j]);
				// }
				out.println("Time\tStartServiceTime\tEndServiceTime");
				for (int j = 0; j < queue[0].length; j++) {
					out.println(queue[0][j] + "\t" + queue[1][j] + "\t"
							+ queue[2][j]);
				}

			} catch (IOException e) {
				System.err.println(e);
			}

		} catch (SimQueueException e) {
			e.getMessage();
			e.printStackTrace();
		} catch (TriangularException e) {
			e.getMessage();
			e.printStackTrace();
		} catch (ExponentialException e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
}
