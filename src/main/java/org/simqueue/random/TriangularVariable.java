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

    // parameter a of triVar
	private double triVar_a = 0.0d;
	// parameter m of triVar the mode
	private double triVar_m = 0.0d;
	// parameter b of triVar
	private double triVar_b = 0.0d;
	
    /** 
     * Construct a triangular stochastic variable with 
     * parameters a, m, and b, such that a <= m <= b and a < b. 
     * 
     * @throws TriangularException if not a <= m <= b or not a < b. */
	public TriangularVariable( double a, double m, double b ) throws TriangularException {
        if( a <= m && m <= b && a < b ) {
            triVar_a = a;
            triVar_m = m;
            triVar_b = b;
            setTheoreticalMean( (0 + triVar_m + triVar_b) / 3 );
            setTheoreticalVar( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 );
            setTheoreticalSD(Math.sqrt( ((triVar_b-triVar_a)*(triVar_b-triVar_a) - (triVar_m-triVar_a)*(triVar_b-triVar_m)) / 18 ));            
        } else {
            throw new TriangularException();
        }
	}
	
	/** {@inheritDoc} */
	public double getNext() {
        //generate triangular stochastic variable
        double y = rand.nextDouble();
        if( y < ((triVar_m - triVar_a) / (triVar_b - triVar_a)) )
            return triVar_a + Math.sqrt( (triVar_b-triVar_a)*(triVar_m-triVar_a)*y );
        return triVar_b - Math.sqrt( (triVar_b-triVar_a)*(triVar_b-triVar_m)*(1-y) );      
	}

	/**
	 * Return the parameter a for the triangular variable.
	 * @return a
	 */
	public double geta() {
		return triVar_a;
	}

	/**
	 * Return the parameter m for the triangular variable.
	 * @return m
	 */
	public double getm() {
		return triVar_m;
	}

	/**
	 * Return the parameter b for the triangular variable.
	 * @return b
	 */
	public double getb() {
		return triVar_b;
	}

}
