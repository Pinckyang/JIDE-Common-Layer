/*
 * @(#)IntegerEnumConverter.java 4/1/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.converter;

/**
 * A typical way to define a constant is to use int as the value type. For example, in SwingConstants, the following values
 * are defined.
 * <code><pre>
 * public static final int CENTER  = 0;
 * public static final int TOP     = 1;
 * public static final int LEFT    = 2;
 * public static final int BOTTOM  = 3;
 * public static final int RIGHT   = 4;
 * </pre></code>
 * Before JDK1.5, there is no enum type, so this is one way to define enumeration.
 * When you use it, you just need to define a int field say _locaton and the
 * valid value for _location is one of the values above. If you want to display
 * it in UI and allow user to specify the value of _location, problem comes.
 * You don't want to use 0, 1, 2, 3, 4 as the value doesn't mean anything from user point of view.
 * You want user to be able to use meanful names such as "Center", "Top", "Left", "Bottom", "Right".
 * Obviously you need a converter here to convert from integer in an enum to string, such as converting from 0 to "Center" and vice versa.
 * That's what <tt>EnumConverter</tt> for.
 * <p/>
 * Combining with EnumCellConverter, EnumCellEditor,
 * you can easily use combobox to choose value for _location like the example above using
 * meanful strings.
 */
public class EnumConverter implements ObjectConverter {
    private String _name;
    private Object _default;
    private Class _type;
    private Object[] _objects;
    private String[] _strings;

    public EnumConverter(String name, Object[] values, String[] strings) {
        this(name, values[0].getClass(), values, strings);
    }

    public EnumConverter(String name, Class type, Object[] values, String[] strings) {
        this(name, type, values, strings, null);
    }

    /**
     * Creates an EnumConverter.
     *
     * @param name     the name of the converter. The name is used to create ConverterContext and later on the EditorContext.
     * @param type     the type of the element in <code>objects</code> array.
     * @param objects  the <code>objects</code> array. All elements in the <code>objects</code> array should have the same type.
     * @param strings  the <code>strings</code> array. It contains the meanful names for the elements in <code>objects</code> array.
     *                 They should one to one match with each other. The length of <code>strings</code>
     *                 array should be the same as that of <code>objects</code> array. Otherwise IllegalArgumentExceptio will be thrown.
     * @param aDefault
     */
    public EnumConverter(String name, Class type, Object[] objects, String[] strings, Object aDefault) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The \"name\" parameter cannot be null or empty. Please use a unique string to represent the name of the converter.");
        }
        _name = name;
        if (objects == null) {
            throw new IllegalArgumentException("The \"objects\" parameter cannot be null.");
        }
        if (strings == null) {
            throw new IllegalArgumentException("The \"strings\" parameter cannot be null.");
        }
        if (strings.length != objects.length) {
            throw new IllegalArgumentException("The \"objects\" and \"strings\" parameters should have the same length.");
        }
        _type = type;
        _objects = objects;
        _strings = strings;
        _default = aDefault;
    }

    transient private ConverterContext _conext;

    /**
     * Gets the converter context of this converter. The name of the context
     * is the name of the converter where you pass in to EnumConverter's constructor.
     *
     * @return the converter context of this converter.
     */
    public ConverterContext getContext() {
        if (_conext == null) {
            _conext = new ConverterContext(_name);
        }
        return _conext;
    }

    /**
     * Converts the object to string. It will find the object from the <code>objects</code> array and find the matching string
     * from <code>strings</code> array.
     *
     * @param object
     * @param context
     * @return the string for the object.
     */
    public String toString(Object object, ConverterContext context) {
        for (int i = 0; i < _objects.length; i++) {
            if ((_objects[i] == null && object == null) || (_objects[i] != null && _objects[i].equals(object))) {
                if (i < _strings.length) {
                    return _strings[i];
                }
            }
        }
        return null;
    }

    public boolean supportToString(Object object, ConverterContext context) {
        return true;
    }

    /**
     * Converts the string to the object. It will find the string from the <code>strings</code> array and find the matching
     * object from <code>objects</code> array.
     *
     * @param string
     * @param context
     * @return the object of the string.
     */
    public Object fromString(String string, ConverterContext context) {
        for (int i = 0; i < _strings.length; i++) {
            if (_strings[i].equals(string)) {
                if (i < _objects.length) {
                    return _objects[i];
                }
            }
        }
        return _default;
    }

    public boolean supportFromString(String string, ConverterContext context) {
        return true;
    }

    /**
     * Gets the name of the converter.
     *
     * @return the name of the converter.
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the type of the converter.
     *
     * @return the type of the converter.
     */
    public Class getType() {
        return _type;
    }

    /**
     * Gets the default value of the converter if it failed to find the matching object for a particular string.
     *
     * @return the default value.
     */
    public Object getDefault() {
        return _default;
    }

    /**
     * Gets the <code>objects</code> array.
     *
     * @return the <code>objects</code> array.
     */
    public Object[] getObjects() {
        return _objects;
    }

    /**
     * Gets the <code>strings</code> array.
     *
     * @return the <code>strings</code> array.
     */
    public String[] getStrings() {
        return _strings;
    }
}