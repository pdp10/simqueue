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

import java.io.*;


/** 
 * Main class
 */
public class MainTest {

    public static void main( String[] args ) {
        SimulatedQueue Q = null;
        
        // Queue Capacity
        Integer num = new Integer(100);
        
        // average number of customers per minute?
        Double expVar_b = new Double(0.3);
        
        // length of the most common (the mode) service time [min]
        Double triVar_m = new Double(2);
        
        // length of the shortest service time [min] (no service)
        Double triVar_a = new Double(0.0);
        
        // length of the maximum service time [min]
        Double triVar_b = new Double(20);

        try {
            Q = new SimulatedQueue( num.intValue() );
            Q.setExponential( expVar_b.doubleValue() );

            Q.setTriangular( triVar_a.doubleValue(), triVar_m.doubleValue(),  triVar_b.doubleValue() );
            Q.run();

            Q.printHistory();
            System.out.println();
            Q.realStatistics();
            System.out.println();
            Q.simulatedStatistics();
            System.out.println();
            Q.errorStatistics();
            System.out.println();
            Q.printTime();
            
            
            // get the queue of events (arrival, service, and leave times)
            double[][] queue = Q.getQueue();

            // write the queue to file
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("SimulatedQueue.csv", false)))) {
//                out.println("Client\tArrivalTime\tServiceTime\tLeavingTime");
//                for( int j = 0; j < queue[0].length; j++ ) {
//                    out.println(j + "\t" + queue[0][j] + "\t" + queue[1][j] + "\t" + queue[2][j]);
//                }
                out.println("Time\tStartServiceTime\tEndServiceTime");
                for( int j = 0; j < queue[0].length; j++ ) {
                    out.println(queue[0][j] + "\t" + queue[1][j] + "\t" + queue[2][j]);
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
