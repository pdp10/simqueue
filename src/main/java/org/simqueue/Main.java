package org.simqueue;
/*
 * MIT License
 * Copyright (c) 2005 Piero Dalle Pezze
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

import org.simqueue.exception.ExponentialException;
import org.simqueue.exception.SimQueueException;
import org.simqueue.exception.TriangularException;
import org.simqueue.sim.SimQueue;
import org.simqueue.utils.ElapsedTime;
import org.simqueue.utils.PropertiesManager;

/**
 * Main class for SimQueue.
 */
public class Main {

  /**
   * The programs requests the queue size and the parameters for the two used
   * stochastic variables: 1) triangular variable; 2) exponential variable.
   * Then, the stochastic history is generated and shown.
   * 
   * @param args
   *        the report file name
   */
  public static void main(String[] args) {
    String fileout = "simqueue.csv";
    String filein = "parameters.txt";
    Properties prop = null;
    boolean screenprint = true;
    if (args.length > 0) {
      fileout = args[0];
      screenprint = false;
      if (args.length > 1) {
        filein = args[1];
      }
    }
    try {
      prop = PropertiesManager.load(filein);
    } catch (IOException e1) {
      System.err.println("Error: Input file not found.");
      System.err.println("simqueue syntax:\n" + "java -jar simqueue.jar [simqueue.csv] [parameters.txt]\n\n"
        + "simqueue.csv: output file\n"
        + "parameters.txt: configuration file (if this is found, it is parsed automatically)\n\n"
        + "(e.g. parameters.txt)\n" + "clients_num=50\n" + "clients_per_hour=25\n" + "most_common_service_time=3.5\n"
        + "maximum_service_time=10\n");
      System.exit(1);
    }
    // Used for computing the start and end times.
    Calendar start = null;
    Calendar end = null;
    SimQueue Q = null;
    Integer num = null;
    double expVar_lambda = 0.0d, triVar_a = 0.0d, triVar_m = 1.0d, triVar_b = 2.0d;

    if (prop.getProperty("clients_num") != null) {
      num = Integer.parseInt(prop.getProperty("clients_num"));
    } else {
      System.err.println("Error: `clients_num` not found in configuration file. Exit.");
      System.exit(1);
    }
    if (prop.getProperty("clients_per_hour") != null) {
      expVar_lambda = Double.parseDouble(prop.getProperty("clients_per_hour")) / 60.0d;
    } else {
      System.err.println("Error: `clients_per_hour` not found in configuration file. Exit.");
      System.exit(1);
    }
    if (prop.getProperty("most_common_service_time") != null) {
      triVar_m = Double.parseDouble(prop.getProperty("most_common_service_time"));
    } else {
      System.err.println("Error: `most_common_service_time` not found in configuration file. Exit.");
      System.exit(1);
    }
    if (prop.getProperty("maximum_service_time") != null) {
      triVar_b = Double.parseDouble(prop.getProperty("maximum_service_time"));
    } else {
      System.err.println("Error: `maximum_service_time` not found in configuration file. Exit.");
      System.exit(1);
    }
    
    if(screenprint) {
    	System.out.print("\nsimqueue: a FIFO queue simulator based on stochastic time events.\n\n");
    }
    try {
      Q = new SimQueue(num, expVar_lambda, triVar_a, triVar_m, triVar_b);
      start = Calendar.getInstance();
      Q.run();
      end = Calendar.getInstance();
      if(screenprint) {
	      System.out.println(Q.getHistoryString());
	      System.out.println();
	      System.out.println(Q.getTheoreticalStatisticsString());
	      System.out.println();
	      System.out.println(Q.getSimulatedStatisticsString());
	      System.out.println();
	      System.out.println(Q.getErrorStatisticsString());
	      System.out.println();
	      System.out.println(ElapsedTime.compute(start, end));
      }
      // get the queue of events (arrival, service, and leave times)
      double[][] history = Q.getHistory();
      // retrieve the arrival time samples and calculate the CDF
      Double[] arrivalTimeSamples = ArrayUtils.toObject(Q.getArrivalTimesDistrib());
      Arrays.sort(arrivalTimeSamples);
      // retrieve the service time samples
      Double[] serviceTimeSamples = ArrayUtils.toObject(Q.getServiceTimesDistrib());
      Arrays.sort(serviceTimeSamples);
      if(!screenprint) {
	      // write the queue to file
	      try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileout, false)))) {
	        out.println("Time\tArrivalTime\tServiceTime\tLeavingTime\tArrivalTimeSamples\tServiceTimeSamples");
	        for (int j = 0; j < history[0].length; j++) {
	          out.println(j + "\t" + history[0][j] + "\t" + history[1][j] + "\t" + history[2][j] + "\t"
	            + arrivalTimeSamples[j] + "\t" + serviceTimeSamples[j]);
	        }
	      } catch (IOException e) {
	        System.err.println(e);
	      }
      }
    } catch (NumberFormatException e) {
      System.err.println("Error: input must be an integer");
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
