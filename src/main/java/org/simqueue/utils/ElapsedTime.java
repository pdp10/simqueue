package org.simqueue.utils;
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

import java.util.Calendar;


/**
 * An utility to compute the elapsed time in minutes, seconds, and milliseconds
 */
public class ElapsedTime {
	
	/**
	 * Print the running time of the simulation.
	 * 
	 * @param start
	 * @param end
	 * @return the human readable string
	 */
    public static String compute(Calendar start, Calendar end) {
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
            return "Running time of the simulation: " + m + " min " + s + " s " + ms + " ms";
        }
        return "";
    }	

}
