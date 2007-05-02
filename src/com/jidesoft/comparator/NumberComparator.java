package com.jidesoft.comparator;

import java.util.Comparator;

/**
 * Comparator for Number type. This is a singleton class. Call getInstance() to
 * get the comparator.
 */
public class NumberComparator implements Comparator {
    private static NumberComparator singleton = null;

    /**
     * Constructor.
     * <p/>
     * Has protected access to prevent other clients creating instances of the
     * class ... it is stateless so we need only one instance.
     */
    protected NumberComparator() {
    }

    /**
     * Returns <tt>NumberComparator</tt> singleton.
     *
     * @return an instance of NumberComparator.
     */
    public static NumberComparator getInstance() {
        if (singleton == null)
            singleton = new NumberComparator();
        return singleton;
    }

    /**
     * Compares two <tt>Number</tt>s.
     *
     * @param o1 the first object to be compared
     * @param o2 the second object to be compared
     * @return 0 if a and b are equal, -1 if a is less than b, 1 if a is more than b.
     */
    public int compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        else if (o1 == null) {
            return -1;
        }
        else if (o2 == null) {
            return 1;
        }

        if (o1 instanceof Number) {
            if (o2 instanceof Number) {
                final double d1 = ((Number) o1).doubleValue();
                final double d2 = ((Number) o2).doubleValue();

                if (d1 < d2)
                    return -1;
                else if (d1 > d2)
                    return 1;
                else
                    return 0;
            }
            else {
// o2 wasn't comparable
                throw new ClassCastException(
                        "The first argument of this method was not a Number but " +
                                o2.getClass().getName()
                );
            }
        }
        else if (o2 instanceof Number) {
// o1 wasn't comparable
            throw new ClassCastException(
                    "The second argument of this method was not a Number but " +
                            o1.getClass().getName()
            );
        }
        else {
// neither were comparable
            throw new ClassCastException(
                    "Both arguments of this method were not Numbers. They are " +
                            o1.getClass().getName() + " and " + o2.getClass().getName()
            );
        }
    }
}