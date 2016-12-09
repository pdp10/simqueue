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

    /** lambda parameter */
    private double lambda = 0.0d;
	
    /** 
     * Construct an exponential stochastic variable with parameter expLambda.
     * expLambda parameter must be  > 0.
     * 
     * @param expLambda the lambda parameter
     * @throws ExponentialException if expLambda < 0. 
     */    
	public ExponentialVariable(double expLambda)  throws ExponentialException  {
        if( expLambda > 0 ) {
            lambda = expLambda;
            setTheoreticalMean(1 / lambda);
            setTheoreticalVar(1 / ( lambda * lambda ));
            setTheoreticalSD(Math.sqrt( 1 / ( lambda * lambda ) )); 
        } else {
            throw new ExponentialException();
        }
	}
	
	/**
	 * Return the lambda parameter for the exponential variable.
	 * @return lambda
	 */
	public double getLambda() {
		return lambda;
	}

	/** {@inheritDoc} */
	public double getNext() {
        //generate exponential stochastic variable
        return - ( Math.log( 1-rand.nextDouble() ) / lambda );
	}


}
