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
import java.util.*;


/** 
 * Main class
 */
public class MainTest {

    /** The main method. In the main body, the programs asks to user the capacity 
     *  of the queue and the parameters for the two stochastic variables used: 
     *  triangular and exponential casual variables. Then it prints the stochastic 
     *  history generated. */
    public static void main( String[] args ) {
        SimulatedQueue Q = null;
        
        // Queue Capacity
        Integer num = new Integer(100);
        
        // average number of customers per minute?
        Double bC1 = new Double(7.8);
        
        // length of the most common (the mode) service time [min]
        Double mC2 = new Double(35.0);
        
        // length of the maximum service time [min]
        Double bC2 = new Double(60.0);

        // length of the shortest service time [min] (no service)
        Double aC2 = new Double(0.0);

        BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

        try {
            Q = new SimulatedQueue( num.intValue() );
            Q.setExponential( bC1.doubleValue() );

            Q.setTriangular( aC2.doubleValue(), mC2.doubleValue(),  bC2.doubleValue() );
            Q.run();

            //Q.printHistory();
            //System.out.println();
            //Q.realStatistics();
            //System.out.println();
            //Q.simulatedStatistics();
            //System.out.println();
            //Q.errorStatistics();
            //System.out.println();
            //Q.printTime();
            
            
            // get the queue of events (arrival, service, and leave times)
            double[][] queue = Q.getQueue();

            // write the queue to file
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("SimulatedQueue.csv", false)))) {
                out.println("Time\tArrivalTime\tServiceTime\tLeavingTime");
                for( int j = 0; j < queue[0].length; j++ ) {
                    out.println(j + "\t" + queue[0][j] + "\t" + queue[1][j] + "\t" + queue[2][j]);
                }
            } catch (IOException e) {
                System.err.println(e);
            }
            
        } 
        catch( SimulatedQueueException e ) { e.getMessage(); e.printStackTrace();}
        catch( TriangularException e ) { e.getMessage(); e.printStackTrace();}
        catch( ExponentialException e ) { e.getMessage(); e.printStackTrace(); }
    }
}
