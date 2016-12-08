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

import java.util.Random;


/** A generic random variable */
public abstract class RandomVariable {

	/** The uniform variable. */
    protected Random rand = new Random();
	
    // the theoretical statistics 
    private double theoMean = 0.0d;
    private double theoVar = 0.0d;
    private double theoSD = 0.0d;
	
	/** Return the next value for this random variable. */
	public abstract double getNext();
	
	/**
	 * Get the theoretical mean
	 * @return the theoMean
	 */
	public double getTheoreticalMean() {
		return theoMean;
	}

	/**
	 * Set the theoretical mean
	 * @param theoMean the theoMean to set
	 */
	public void setTheoreticalMean(double theoMean) {
		this.theoMean = theoMean;
	}

	/**
	 * Get the theoretical variance
	 * @return the theoVar
	 */
	public double getTheoreticalVar() {
		return theoVar;
	}

	/**
	 * Set the theoretical variance
	 * @param theoVar the theoVar to set
	 */
	public void setTheoreticalVar(double theoVar) {
		this.theoVar = theoVar;
	}

	/**
	 * Get the theoretical standard deviation
	 * @return the theoSD
	 */
	public double getTheoreticalSD() {
		return theoSD;
	}

	/**
	 * Set the theoretical standard deviation
	 * @param theoSD the theoSD to set
	 */
	public void setTheoreticalSD(double theoSD) {
		this.theoSD = theoSD;
	}


}
