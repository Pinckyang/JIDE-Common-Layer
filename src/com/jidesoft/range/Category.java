/*
 * @(#)Category.java
 * 
 * 2002 - 2009 JIDE Software Incorporated. All rights reserved.
 * Copyright (c) 2005 - 2009 Catalysoft Limited. All rights reserved.
 */

package com.jidesoft.range;

/**
 * This class is really an adapter because it takes any object and allows it to be used as a Category.
 *
 * @author Simon White (swhite@catalysoft.com)
 */
public class Category<T> implements Positionable {
    private String _name;
    private T _value;
    private CategoryRange<T> _range;

    public Category(String name, T value) {
        setName(name);
        _value = value;
    }

    public Category(String name, T value, CategoryRange<T> range) {
        setName(name);
        _value = value;
        _range = range;
    }

    public Category(T value) {
        _value = value;
    }

    public Category(T value, CategoryRange<T> range) {
        _value = value;
        _range = range;
    }

    public CategoryRange<T> getRange() {
        return _range;
    }

    public void setRange(CategoryRange<T> range) {
        _range = range;
    }

    public double position() {
        if (_range == null) {
            throw new IllegalStateException("Cannot compute position for a category that does not belong to a range");
        }
        return _range.position(_value);
    }

    public T getValue() {
        return _value;
    }

    public String getName() {
        if (_name == null) {
            return _value.toString();
        }
        return _name;
    }

    private void setName(String name) {
        _name = name;
    }

    public int compareTo(Positionable o) {
        double otherPosition = o.position();
        double position = position();
        if (position < otherPosition) {
            return -1;
        }
        else if (position > otherPosition) {
            return 1;
        }
        else {
            return 0;
        }
    }
    
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_value == null) ? 0 : _value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category other = (Category) obj;
        if (_name == null) {
            if (other._name != null)
                return false;
        } else if (!_name.equals(other._name))
            return false;
        if (_value == null) {
            if (other._value != null)
                return false;
        } else if (!_value.equals(other._value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("#<Category name='%s' value='%s'>", _name, _value.toString());
    }

}
