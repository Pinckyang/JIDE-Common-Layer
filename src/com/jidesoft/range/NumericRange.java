/*
 * @(#)NumericRange.java
 * 
 * 2002 - 2010 JIDE Software Incorporated. All rights reserved.
 * Copyright (c) 2005 - 2010 Catalysoft Limited. All rights reserved.
 */

package com.jidesoft.range;

/**
 * Specifies upper and lower bounds for a range of values
 *
 * @author Simon
 */
public class NumericRange extends AbstractNumericRange<Double> {
    protected double _min;
    protected double _max;

    /**
     * Creates a numeric range with minimum 0.0 and maximum of 1.0
     */
    public NumericRange() {
        this(0.0, 1.0);
    }

    /**
     * Create a numeric range by supplying minimum and maximum values
     *
     * @param min the minumum
     * @param max the maximum
     */
    public NumericRange(double min, double max) {
        super();
        _min = Math.min(min, max);
        _max = Math.max(min, max);
    }
    
    /**
     * Creates a copy of the supplied NumericRange
     * @param numericRange the NumericRange instance to copy
     */
    public NumericRange(NumericRange numericRange) {
        this(numericRange.minimum(), numericRange.maximum());
    }

    @Override
    public Range<Double> copy() {
        return new NumericRange(this);
    }

    /**
     * @return the minimum value
     */
    public double minimum() {
        return _min;
    }

    /**
     * @return the maximum value
     */
    public double maximum() {
        return _max;
    }

    /**
     * @return the minimum value
     */
    public double getMin() {
        return _min;
    }

    /**
     * Sets the minimum value
     *
     * @param min the new minimum value.
     */
    public void setMin(double min) {
        double old = _min;
        if (old == min) {
            return;
        }
        assert min <= _max;
        _min = min;
        firePropertyChange(PROPERTY_MIN, old, min);
    }

    /**
     * @return the maximum value
     */
    public double getMax() {
        return _max;
    }

    /**
     * Sets the maximum value
     *
     * @param max the new maximum value.
     */
    public void setMax(double max) {
        double old = _max;
        if (old == max) {
            return;
        }
        assert max >= _min;
        _max = max;
        firePropertyChange(PROPERTY_MAX, old, max);
    }

    /**
     * @return the size of the range
     */
    public double size() {
        return _max - _min;
    }

    /**
     * @return the minimum() value for the range
     */
    public Double lower() {
        return minimum();
    }

    /**
     * Sets the minimum and maximum values of the range in a single call
     */
    public void adjust(Double lower, Double upper) {
        setMin(lower);
        setMax(upper);
    }

    /**
     * @return the maximum() value for the range
     */
    public Double upper() {
        return maximum();
    }

    /**
     * Determines whether the range contains the supplied value
     */
    public boolean contains(Double x) {
        return x != null && x >= _min && x <= _max;
    }
    
    /**
     * Creates a new NumericRange by enlarging this numeric range about its mid-point.
     * For example to make it 10% bigger, use a stretch factor of 1.1.
     * Note that this method can also be used to shrink a NumericRange.
     * @param stretchFactor the multiplication factor for the enlargement
     * @return a new NumericRange
     */
    public NumericRange stretch(double stretchFactor) {
        double mid = (_max + _min)/2.0;
        double halfSize = size()/2.0;
        return new NumericRange(mid - halfSize * stretchFactor, mid + halfSize*stretchFactor);
    }

    /**
     * Test for equality based on the values of min and max
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof NumericRange) {
            NumericRange otherRange = (NumericRange) other;
            return _min == otherRange._min && _max == otherRange._max;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("#<NumericRange min=%f max=%f>", _min, _max);
    }
}
