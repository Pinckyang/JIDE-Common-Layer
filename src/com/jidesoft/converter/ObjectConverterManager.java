/*
 * @(#) ObjectConverterManager.java
 *
 * Copyright 2002 - 2003 JIDE Software. All rights reserved.
 */
package com.jidesoft.converter;

import java.awt.*;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * A global object that can register converter with a type and a ConverterContext.
 */
public class ObjectConverterManager {

    private static CacheMap _cache = new CacheMap(ConverterContext.DEFAULT_CONTEXT);

    private static ObjectConverter _defaultConverter = new DefaultObjectConverter();

    /**
     * Registers a converter with the type specified as class and a converter context specified as context.
     *
     * @param clazz     type
     * @param converter converter to be registered
     * @param context
     */
    public static void registerConverter(Class clazz, ObjectConverter converter, ConverterContext context) {
        if (clazz == null) {
            throw new IllegalArgumentException("Parameter class cannot be null");
        }

        if (context == null) {
            context = ConverterContext.DEFAULT_CONTEXT;
        }
        _cache.register(clazz, converter, context);
    }

    /**
     * Registers a converter with type specified as clazz.
     *
     * @param clazz     type
     * @param converter converter to be registered
     */
    public static void registerConverter(Class clazz, ObjectConverter converter) {
        registerConverter(clazz, converter, ConverterContext.DEFAULT_CONTEXT);
    }

    /**
     * Unregisters converter associated with clazz and context.
     *
     * @param clazz
     * @param context
     */
    public static void unregisterConverter(Class clazz, ConverterContext context) {
        if (context == null) {
            context = ConverterContext.DEFAULT_CONTEXT;
        }
        _cache.unregister(clazz, context);
    }

    /**
     * Unregisters converter associated with clazz.
     *
     * @param clazz
     */
    public static void unregisterConverter(Class clazz) {
        unregisterConverter(clazz, ConverterContext.DEFAULT_CONTEXT);
    }

    /**
     * Unregisters all the converters which registered before.
     */
    public static void unregisterAllConverters() {
        _cache.clear();
    }

    /**
     * Gets the registered converter associated with class and context.
     *
     * @param clazz
     * @param context
     * @return the registered converter.
     */
    public static ObjectConverter getConverter(Class clazz, ConverterContext context) {
        if (isAutoInit()) {
            initDefaultConverter();
        }

        if (context == null) {
            context = ConverterContext.DEFAULT_CONTEXT;
        }
        Object object = _cache.getRegisteredObject(clazz, context);
        if (object != null && object instanceof ObjectConverter) {
            return (ObjectConverter) object;
        }
        else {
            return _defaultConverter;
        }
    }

    /**
     * Gets the converter associated with the type.
     *
     * @param clazz type
     * @return the converter
     */
    public static ObjectConverter getConverter(Class clazz) {
        return getConverter(clazz, ConverterContext.DEFAULT_CONTEXT);
    }

    /**
     * Converts an object to string using default converter context.
     *
     * @param object object to be converted.
     * @return the string
     */
    public static String toString(Object object) {
        if (object != null)
            return toString(object, object.getClass(), ConverterContext.DEFAULT_CONTEXT);
        else
            return "";
    }

    /**
     * Converts an object to string using default converter context.
     *
     * @param object object to be converted.
     * @param clazz  type of the object
     * @return the string
     */
    public static String toString(Object object, Class clazz) {
        return toString(object, clazz, ConverterContext.DEFAULT_CONTEXT);
    }

    /**
     * Converts an object to string using converter context sepcified.
     *
     * @param object  object to be converted.
     * @param clazz   type of the object
     * @param context converter context
     * @return the string converted from object
     */
    public static String toString(Object object, Class clazz, ConverterContext context) {
        ObjectConverter converter = getConverter(clazz, context);
        if (converter != null && converter.supportToString(object, context)) {
            return converter.toString(object, context);
        }
        else if (object == null) {
            return "";
        }
        else {
            return object.toString();
        }
    }

    /**
     * Converts from a string to an object with type class.
     *
     * @param string the string to be converted
     * @param clazz  the type to be converted to
     * @return the object of type class which is converted from string
     */
    public static Object fromString(String string, Class clazz) {
        return fromString(string, clazz, ConverterContext.DEFAULT_CONTEXT);
    }

    /**
     * Converts from a string to an object with type class using the converter context.
     *
     * @param string  the string to be converted
     * @param clazz   the type to be converted to
     * @param context converter context to be used
     * @return the object of type class which is converted from string
     */
    public static Object fromString(String string, Class clazz, ConverterContext context) {
        ObjectConverter converter = getConverter(clazz, context);
        if (converter != null && converter.supportFromString(string, context)) {
            return converter.fromString(string, context);
        }
        else {
            return null;
        }
    }

    private static boolean _inited = false;
    private static boolean _autoInit = true;

    /**
     * Checks the value of autoInit.
     *
     * @return true or false.
     * @see #setAutoInit(boolean)
     */
    public static boolean isAutoInit() {
        return _autoInit;
    }

    /**
     * Sets autoInit to true or false. If autoInit is true, whenever someone tries to call methods like
     * as toString or fromString, {@link #initDefaultConverter()} will be called if it has never be called.
     * By default, autoInit is true.
     * <p/>
     * This might affect the behavior if users provide their own converters and want to overwrite
     * default converters. In this case, instead of depending on autoInit to initialize default converters,
     * you should call {@link #initDefaultConverter()} first, then call registerConverter to add your own converters.
     *
     * @param autoInit
     */
    public static void setAutoInit(boolean autoInit) {
        _autoInit = autoInit;
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the manager occurs.
     *
     * @param l the RegistrationListener
     */
    public static void addRegistrationListener(RegistrationListener l) {
        _cache.addRegistrationListener(l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the manager occurs.
     *
     * @param l the RegistrationListener
     */
    public static void removeRegistrationListener(RegistrationListener l) {
        _cache.removeRegistrationListener(l);
    }

    /**
     * Returns an array of all the registration listeners
     * registered on this manager.
     *
     * @return all of this registration's <code>RegistrationListener</code>s
     *         or an empty array if no registration listeners are currently registered
     * @see #addRegistrationListener
     * @see #removeRegistrationListener
     */
    public static RegistrationListener[] getRegistrationListeners() {
        return _cache.getRegistrationListeners();
    }

    /**
     * Initialize default converters. Please make sure you call this method before you use any
     * converter related classes. By default we register following converters.
     * <ul>
     * <li> registerConverter(String.class, new DefaultObjectConverter());
     * <li> registerConverter(Integer.class, new IntegerConverter());
     * <li> registerConverter(int.class, new IntegerConverter());
     * <li> registerConverter(Integer.class, new NaturalNumberConverter(), NaturalNumberConverter.CONTEXT);
     * <li> registerConverter(int.class, new NaturalNumberConverter(), NaturalNumberConverter.CONTEXT);
     * <li> registerConverter(Long.class, new LongConverter());
     * <li> registerConverter(long.class, new LongConverter());
     * <li> registerConverter(Double.class, new DoubleConverter());
     * <li> registerConverter(double.class, new DoubleConverter());
     * <li> registerConverter(Float.class, new FloatConverter());
     * <li> registerConverter(float.class, new FloatConverter());
     * <li> registerConverter(Short.class, new ShortConverter());
     * <li> registerConverter(short.class, new ShortConverter());
     * <li> registerConverter(Rectangle.class, new RectangleConverter());
     * <li> registerConverter(Point.class, new PointConverter());
     * <li> registerConverter(Insets.class, new InsetsConverter());
     * <li> registerConverter(Dimension.class, new DimensionConverter());
     * <li> registerConverter(Boolean.class, new BooleanConverter());
     * <li> registerConverter(boolean.class, new BooleanConverter());
     * <li> registerConverter(File.class, new FileConverter());
     * <li> registerConverter(String.class, new FontNameConverter(), FontNameConverter.CONTEXT);
     * <li> registerConverter(Date.class, new DateConverter());
     * <li> registerConverter(Calendar.class, new CalendarConverter());
     * <li> registerConverter(Calendar.class, new MonthConverter(), MonthConverter.CONTEXT_MONTH);
     * <li> registerConverter(Color.class, new RgbColorConverter());
     * <li> registerConverter(Color.class, new HexColorConverter(), ColorConverter.CONTEXT_HEX);
     * <li> registerConverter(String[].class, new StringArrayConverter());
     * </ul>
     */
    public static void initDefaultConverter() {
        if (_inited) {
            return;
        }

        registerConverter(String.class, new DefaultObjectConverter());
        registerConverter(char[].class, new PasswordConverter(), PasswordConverter.CONTEXT);

        IntegerConverter integerConverter = new IntegerConverter();
        registerConverter(Integer.class, integerConverter);
        registerConverter(int.class, integerConverter);

        NaturalNumberConverter naturalNumberConverter = new NaturalNumberConverter();
        registerConverter(Integer.class, naturalNumberConverter, NaturalNumberConverter.CONTEXT);
        registerConverter(int.class, naturalNumberConverter, NaturalNumberConverter.CONTEXT);

        LongConverter longConverter = new LongConverter();
        registerConverter(Long.class, longConverter);
        registerConverter(long.class, longConverter);

        DoubleConverter doubleConverter = new DoubleConverter();
        registerConverter(Double.class, doubleConverter);
        registerConverter(double.class, doubleConverter);

        FloatConverter floatConverter = new FloatConverter();
        registerConverter(Float.class, floatConverter);
        registerConverter(float.class, floatConverter);

        ShortConverter shortConverter = new ShortConverter();
        registerConverter(Short.class, shortConverter);
        registerConverter(short.class, shortConverter);

        ByteConverter byteConverter = new ByteConverter();
        registerConverter(Byte.class, byteConverter);
        registerConverter(byte.class, byteConverter);

        registerConverter(Rectangle.class, new RectangleConverter());
        registerConverter(Point.class, new PointConverter());
        registerConverter(Insets.class, new InsetsConverter());
        registerConverter(Dimension.class, new DimensionConverter());

        BooleanConverter booleanConverter = new BooleanConverter();
        registerConverter(Boolean.class, booleanConverter);
        registerConverter(boolean.class, booleanConverter);

        registerConverter(File.class, new FileConverter());
        registerConverter(String.class, new FontNameConverter(), FontNameConverter.CONTEXT);

        DateConverter dateConverter = new DateConverter();
        registerConverter(Date.class, dateConverter);
        registerConverter(Date.class, dateConverter, DateConverter.DATETIME_CONTEXT);
        registerConverter(Date.class, dateConverter, DateConverter.TIME_CONTEXT);

        CalendarConverter calendarConverter = new CalendarConverter();
        registerConverter(Calendar.class, calendarConverter);
        registerConverter(Calendar.class, calendarConverter, DateConverter.DATETIME_CONTEXT);
        registerConverter(Calendar.class, calendarConverter, DateConverter.TIME_CONTEXT);

        registerConverter(Calendar.class, new MonthConverter(), MonthConverter.CONTEXT_MONTH);
        registerConverter(Color.class, new RgbColorConverter());
        registerConverter(Color.class, new HexColorConverter(), ColorConverter.CONTEXT_HEX);

        registerConverter(String[].class, new StringArrayConverter());

        QuarterNameConverter quarterNameConverter = new QuarterNameConverter();
        registerConverter(int.class, quarterNameConverter, QuarterNameConverter.CONTEXT);
        registerConverter(Integer.class, quarterNameConverter, QuarterNameConverter.CONTEXT);

        registerConverter(Font.class, new FontConverter());

        CurrencyConverter currencyConverter = new CurrencyConverter();
        registerConverter(Float.class, currencyConverter, CurrencyConverter.CONTEXT);
        registerConverter(float.class, currencyConverter, CurrencyConverter.CONTEXT);
        registerConverter(Double.class, currencyConverter, CurrencyConverter.CONTEXT);
        registerConverter(double.class, currencyConverter, CurrencyConverter.CONTEXT);

        PercentConverter percentConverter = new PercentConverter();
        registerConverter(Float.class, percentConverter, PercentConverter.CONTEXT);
        registerConverter(float.class, percentConverter, PercentConverter.CONTEXT);
        registerConverter(Double.class, percentConverter, PercentConverter.CONTEXT);
        registerConverter(double.class, percentConverter, PercentConverter.CONTEXT);

        MonthNameConverter monthNameConverter = new MonthNameConverter();
        registerConverter(Integer.class, monthNameConverter, MonthNameConverter.CONTEXT);
        registerConverter(int.class, monthNameConverter, MonthNameConverter.CONTEXT);

        YearNameConverter yearNameConverter = new YearNameConverter();
        registerConverter(Integer.class, yearNameConverter, YearNameConverter.CONTEXT);
        registerConverter(int.class, yearNameConverter, YearNameConverter.CONTEXT);

        ObjectConverterManager.registerConverter(int[].class, new DefaultArrayConverter("; ", int.class));
        registerConverter(Object[].class, new DefaultArrayConverter("; ", Object.class));
        registerConverter(String[].class, new DefaultArrayConverter("; ", String.class));

        _inited = true;
    }

    /**
     * If {@link #initDefaultConverter()} is called once, calling it again will have no effect because an internal flag is set.
     * This method will reset the internal flag so that you can call  {@link #initDefaultConverter()} in case you unresgister all
     * converters using {@link #unregisterAllConverters()}.
     */
    public void resetInit() {
        _inited = false;
    }
}