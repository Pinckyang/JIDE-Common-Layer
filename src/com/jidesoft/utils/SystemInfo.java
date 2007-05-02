/*
 * @(#)SystemInfo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.utils;

import java.util.Locale;


/**
 * A utility class can detect OS system information.
 */
final public class SystemInfo {

    /**
     * Variable for whether or not we're on Windows.
     */
    private static boolean _isWindows = false;

    /**
     * Variable for whether or not we're on Windows NT or 2000.
     */
    private static boolean _isWindowsNTor2000 = false;

    /**
     * Variable for whether or not we're on Windows XP.
     */
    private static boolean _isWindowsXP = false;

    /**
     * Variable for whether or not we're on Windows 2003.
     */
    private static boolean _isWindows2003 = false;

    /**
     * Flag which indicates that the Win98/Win2k/WinME features
     * should be disabled.
     */
    private static boolean _isClassicWindows = false;
    /**
     * Variable for whether or not we're on Windows 95.
     */
    private static boolean _isWindows95 = false;

    /**
     * Variable for whether or not we're on Windows 98.
     */
    private static boolean _isWindows98 = false;

    /**
     * Variable for whether or not the operating system allows the
     * application to be reduced to the system tray.
     */
    private static boolean _supportsTray = false;

    /**
     * Variable for whether or not we're on Mac 9.1 or below.
     */
    private static boolean _isMacClassic = false;

    /**
     * Variable for whether or not we're on MacOSX.
     */
    private static boolean _isMacOSX = false;

    /**
     * Variable for whether or not we're on Linux.
     */
    private static boolean _isLinux = false;

    /**
     * Variable for whether or not we're on Solaris.
     */
    private static boolean _isSolaris = false;

    /**
     * Make sure the constructor can never be called.
     */
    private SystemInfo() {
    }

    /**
     * Initialize the settings statically.
     */
    static {
        // get the operating system
        String os = SecurityUtils.getProperty("os.name", "Windows XP");

        // set the operating system variables
        _isWindows = os.indexOf("Windows") != -1;
        try {
            String osVersion = SecurityUtils.getProperty("os.version", "5.0");
            Float version = Float.valueOf(osVersion);
            if (version.floatValue() <= 4.0) {
                _isClassicWindows = true;
            }
            else {
                _isClassicWindows = false;
            }
        }
        catch (NumberFormatException ex) {
            _isClassicWindows = false;
        }
        if (os.indexOf("Windows XP") != -1 || os.indexOf("Windows NT") != -1 || os.indexOf("Windows 2000") != -1) {
            _isWindowsNTor2000 = true;
        }
        if (os.indexOf("Windows XP") != -1) {
            _isWindowsXP = true;
        }
        if (os.indexOf("Windows 2003") != -1) {
            _isWindows2003 = true;
            _isWindowsXP = true;
        }
        if (os.indexOf("Windows 95") != -1) {
            _isWindows95 = true;
        }
        if (os.indexOf("Windows 98") != -1) {
            _isWindows98 = true;
        }
        if (_isWindows) _supportsTray = true;
        _isSolaris = (os.indexOf("Solaris") != -1) || (os.indexOf("SunOS") != -1);
        _isLinux = os.indexOf("Linux") != -1;
        if (os.startsWith("Mac OS")) {
            if (os.endsWith("X")) {
                _isMacOSX = true;
            }
            else {
                _isMacClassic = true;
            }
        }
    }

    /**
     * Returns the version of java we're using.
     */
    public static String getJavaVersion() {
        return SecurityUtils.getProperty("java.version", "1.4.2");
    }

    /**
     * Returns the operating system.
     */
    public static String getOS() {
        return SecurityUtils.getProperty("os.name", "Windows XP");
    }

    /**
     * Returns the operating system version.
     */
    public static String getOSVersion() {
        return SecurityUtils.getProperty("os.version", "5.0");
    }

    /**
     * Returns the user's current working directory.
     */
    public static String getCurrentDirectory() {
        return SecurityUtils.getProperty("user.dir", "");
    }

    /**
     * Returns true if this is Windows NT or Windows 2000 and
     * hence can support a system tray feature.
     */
    public static boolean supportsTray() {
        return _supportsTray;
    }

    /**
     * Set supportTray to false in case dll is missing.
     */
    public static void setSupportsTray(boolean support) {
        _supportsTray = support;
    }

    /**
     * Returns whether or not the os is some version of Windows.
     *
     * @return <tt>true</tt> if the application is running on some Windows
     *         version, <tt>false</tt> otherwise.
     */
    public static boolean isWindows() {
        return _isWindows;
    }

    /**
     * Gets the state of the flag which indicates if the old Windows
     * look and feel should be rendered. This flag is used by the
     * component UI delegates as a hint to determine which style the component
     * should be rendered.
     *
     * @return true if Windows 95 and Windows NT 4 look and feel should
     *         be rendered.
     */
    public static boolean isClassicWindows() {
        return _isClassicWindows;
    }

    /**
     * Returns whether or not the os is some version of Windows NT.
     *
     * @return <tt>true</tt> if the application is running on Windows NT
     *         or 2000, <tt>false</tt> otherwise.
     */
    public static boolean isWindowsNTor2000() {
        return _isWindowsNTor2000;
    }

    /**
     * Returns whether or not the os is some version of Windows XP.
     *
     * @return <tt>true</tt> if the application is running on Windows XP,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isWindowsXP() {
        return _isWindowsXP;
    }

    /**
     * Returns whether or not the os is some version of Windows 2003.
     *
     * @return <tt>true</tt> if the application is running on Windows 2003,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isWindows2003() {
        return _isWindows2003;
    }


    /**
     * Returns whether or not the os is Mac 9.1 or earlier.
     *
     * @return <tt>true</tt> if the application is running on a Mac version
     *         prior to OSX, <tt>false</tt> otherwise.
     */
    public static boolean isMacClassic() {
        return _isMacClassic;
    }

    /**
     * Returns whether or not the os is Mac OSX.
     *
     * @return <tt>true</tt> if the application is running on Mac OSX,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isMacOSX() {
        return _isMacOSX;
    }

    /**
     * Returns whether or not the os is any Mac os.
     *
     * @return <tt>true</tt> if the application is running on Mac OSX
     *         or any previous mac version, <tt>false</tt> otherwise.
     */
    public static boolean isAnyMac() {
        return _isMacClassic || _isMacOSX;
    }

    /**
     * Returns whether or not the os is Solaris.
     *
     * @return <tt>true</tt> if the application is running on Solaris,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isSolaris() {
        return _isSolaris;
    }

    /**
     * Returns whether or not the os is Linux.
     *
     * @return <tt>true</tt> if the application is running on Linux,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isLinux() {
        return _isLinux;
    }

    /**
     * Returns whether or not the os is some version of
     * Unix, defined here as only Solaris or Linux.
     *
     * @return <tt>true</tt> if the application is running on a type of UNIX such as Linux or Solaris,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isUnix() {
        return _isLinux || _isSolaris;
    }

    /**
     * Returns whether or no the JDK version is 1.3 and above.
     *
     * @return <tt>true</tt> if the application is running on JDK 1.3 and above,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isJdk13Above() {
        String s = getJavaVersion();
        String sub = s.substring(0, 3);
        try {
            double version = Double.parseDouble(sub);
            return version >= 1.3;
        }
        catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * Returns whether or no the JDK version is 1.4.2 and above.
     *
     * @return <tt>true</tt> if the application is running on JDK 1.4.2 and above,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isJdk142Above() {
        String s = getJavaVersion();
        String sub = s.substring(0, 5);

        if (sub.compareTo("1.4.2") >= 0) {
            return true;
        }

        return false;
    }

    /**
     * Returns whether or no the JDK version is 1.4 and above.
     *
     * @return <tt>true</tt> if the application is running on JDK 1.4 and above,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isJdk14Above() {
        String s = getJavaVersion();
        String sub = s.substring(0, 3);
        try {
            double version = Double.parseDouble(sub);
            return version >= 1.4;
        }
        catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * Returns whether or no the JDK version is 1.5 and above.
     *
     * @return <tt>true</tt> if the application is running on JDK 1.5 and above,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isJdk15Above() {
        String s = getJavaVersion();
        String sub = s.substring(0, 3);
        try {
            double version = Double.parseDouble(sub);
            return version >= 1.5;
        }
        catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * Returns whether or no the JDK version is 6 and above.
     *
     * @return <tt>true</tt> if the application is running on JDK 6 and above,
     *         <tt>false</tt> otherwise.
     * @deprecated use {@link #isJdk6Above()}. Nothing is wrong with this method. It's just
     *             Sun starts to call JDK1.6 as JDK6 now.
     */
    public static boolean isJdk16Above() {
        return isJdk6Above();
    }

    /**
     * Returns whether or no the JDK version is 6 and above.
     *
     * @return <tt>true</tt> if the application is running on JDK 6 and above,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isJdk6Above() {
        String s = getJavaVersion();
        String sub = s.substring(0, 3);
        try {
            double version = Double.parseDouble(sub);
            return version >= 1.6;
        }
        catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * Returns whether or no the JDK version is 1.7 and above.
     *
     * @return <tt>true</tt> if the application is running on JDK 1.7 and above,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isJdk7Above() {
        String s = getJavaVersion();
        String sub = s.substring(0, 3);
        try {
            double version = Double.parseDouble(sub);
            return version >= 1.7;
        }
        catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * Returns whether the default locale is one of the three language - Chinese, Japanese or Korean - also known as CJK.
     *
     * @return true if the default locale is in CJK.
     */
    public static boolean isCJKLocale() {
        return isCJKLocale(Locale.getDefault());
    }

    /**
     * Returns whether the locale is one of the three language - Chinese, Japanese or Korean - also known as CJK.
     *
     * @param locale the locale to be checked.
     * @return true if the default locale is in CJK.
     */
    public static boolean isCJKLocale(Locale locale) {
        return locale.equals(Locale.CHINA)
                || locale.equals(Locale.CHINESE)
                || locale.equals(new Locale("zh", "HK"))
                || locale.equals(Locale.TAIWAN)
                || locale.equals(Locale.JAPAN)
                || locale.equals(Locale.JAPANESE)
                || locale.equals(Locale.KOREA)
                || locale.equals(Locale.KOREAN);
    }

}