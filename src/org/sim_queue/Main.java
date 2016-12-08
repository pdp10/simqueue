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
 * Main class for SimQueue.
 */
public class Main {

    /** The programs requests the queue size and the parameters 
      * for the two used stochastic variables: 
      * 1) triangular variable;
      * 2) exponential variable. 
      * Then, the stochastic history is generated and shown. */
    public static void main( String[] args ) {
    	
    	String fileout = "SimQueue.csv";
    	if(args.length > 0) {
    		fileout = args[0];
    	}
    		
    	
        SimQueue Q = null;
        Integer num = null;
        Double expVar_b = null, triVar_a = new Double(0.0), triVar_m = null, triVar_b = null;
        BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );

        System.out.print( "\n\t\t*** SIMULATED QUEUE ***\n\n" );

        try {
            System.out.print( "Enter the queue size: " );
            num = new Integer( in.readLine() );
            Q = new SimQueue( num.intValue() );

            System.out.print( "Mean number of clients per hour: " );
            expVar_b = new Double( in.readLine() );
            expVar_b = new Double( expVar_b.doubleValue() / 60 );
            Q.setExponential( expVar_b.doubleValue() );

            System.out.print( "Most common service time [min] (the mode): " );
            triVar_m = new Double( in.readLine() );	    

            System.out.print( "Longest service time [min]: " );
            triVar_b = new Double( in.readLine() );

            Q.setTriangular( triVar_a.doubleValue(), triVar_m.doubleValue(),  triVar_b.doubleValue() );
            Q.run();

            System.out.println( "\nStochastic generation of Arrival/Service/Leaving times for this simulated queue (FIFO):\n" );
            Q.printHistory();
            System.out.println();
            Q.printRealStatistics();
            System.out.println();
            Q.printSimulatedStatistics();
            System.out.println();
            Q.printErrorStatistics();
            System.out.println();
            Q.printTime();
            
            
            // get the queue of events (arrival, service, and leave times)
            double[][] queue = Q.getQueue();

            // write the queue to file
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileout, false)))) {
                out.println("Time\tArrivalTime\tServiceTime\tLeavingTime");
                for( int j = 0; j < queue[0].length; j++ ) {
                    out.println(j + "\t" + queue[0][j] + "\t" + queue[1][j] + "\t" + queue[2][j]);
                }
            } catch (IOException e) {
                System.err.println(e);
            }
            
        } 
        catch( IOException e ) { System.out.println("Error: input not read correctly"); }
        catch( NumberFormatException e ) { System.out.println("Error: input must be an integer"); }
        catch( SimQueueException e ) { e.getMessage(); e.printStackTrace();}
        catch( TriangularException e ) { e.getMessage(); e.printStackTrace();}
        catch( ExponentialException e ) { e.getMessage(); e.printStackTrace(); }
    }
}
