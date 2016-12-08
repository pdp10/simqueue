package org.simqueue.random;
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



/** An exponential random variable */
public class ExponentialVariable extends RandomVariable {

    // parameter b of expVar
    private double expVar_b = 0.0d;
	
    /** Construct an exponential stochastic variable with parameter b.
     *  b parameter must be  > 0. 
     *	@throws ExponentialException if b < 0. */    
	public ExponentialVariable(double b)  throws ExponentialException  {
        if( b > 0 ) {
            expVar_b = b;
            setTheoreticalMean(1 / expVar_b);
            setTheoreticalVar(1 / ( expVar_b * expVar_b ));
            setTheoreticalSD(Math.sqrt( 1 / ( expVar_b * expVar_b ) )); 
        } else {
            throw new ExponentialException();
        }
	}
	
	/**
	 * Return the b parameter for the exponential variable.
	 * @return b
	 */
	public double getb() {
		return expVar_b;
	}

	/** Return the next value for this random variable. */
	public double getNext() {
        //generate exponential stochastic variable
        return - ( Math.log( 1-rand.nextDouble() ) / expVar_b );
	}


}
