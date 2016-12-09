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


import org.simqueue.exception.TriangularException;


/** An triangular random variable */
public class TriangularVariable extends RandomVariable {

    /** parameter a (min) */
	private double a = 0.0d;
	/** parameter m (mode) */ 
	private double m = 0.0d;
	/** parameter b (max) */
	private double b = 0.0d;
	
    /** 
     * Construct a triangular stochastic variable with 
     * parameters a, m, and b, such that a <= m <= b and a < b. 
     * 
     * @param triA the a parameter (min)
     * @param triM the m parameter (mode)
     * @param triB the b parameter (max)
     * @throws TriangularException if not a <= m <= b or not a < b. 
     */
	public TriangularVariable( double triA, double triM, double triB ) throws TriangularException {
        if( triA <= triM && triM <= triB && triA < triB ) {
            a = triA;
            m = triM;
            b = triB;
            setTheoreticalMean( (0 + m + b) / 3 );
            setTheoreticalVar( ((b-a)*(b-a) - (m-a)*(b-m)) / 18 );
            setTheoreticalSD(Math.sqrt( ((b-a)*(b-a) - (m-a)*(b-m)) / 18 ));            
        } else {
            throw new TriangularException();
        }
	}
	
	/** {@inheritDoc} */
	public double getNext() {
        //generate triangular stochastic variable
        double u = rand.nextDouble();
        if( u <= ((m - a) / (b - a)) )
            return a + Math.sqrt( (b-a)*(m-a)*u );
        return b - Math.sqrt( (b-a)*(b-m)*(1-u) );      
	}

	/**
	 * Return the parameter a for the triangular variable.
	 * @return a
	 */
	public double getA() {
		return a;
	}

	/**
	 * Return the parameter m for the triangular variable.
	 * @return m
	 */
	public double getM() {
		return m;
	}

	/**
	 * Return the parameter b for the triangular variable.
	 * @return b
	 */
	public double getB() {
		return b;
	}

}
