/*
 * @(#)Calculator.java 7/11/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

package com.jidesoft.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

/**
 * <tt>Calculator</tt> is a component that can do simple arithmetic calculation. Since it
 * extends JPanel, you can use it at any place in your application.
 * <p/>
 * To make it more flexible, the <tt>Calculator</tt> has no text field to display
 * the result. You can create your own JTextField or JLabel to display the result.
 * Here is a simple example to create a text field and associate it with Calculator.
 * <pre><code>
 * final JTextField textField = new JTextField();
 * textField.setColumns(20);
 * textField.setHorizontalAlignment(JTextField.TRAILING);
 * Calculator calculator = new Calculator();
 * calculator.registerKeyboardActions(textField, JComponent.WHEN_FOCUSED);
 * calculator.addPropertyChangeListener(Calculator.PROPERTY_DISPLAY_TEXT, new PropertyChangeListener() {
 *     public void propertyChange(PropertyChangeEvent evt) {
 *         textField.setText("" + evt.getNewValue());
 *     }
 * });
 * calculator.clear();
 * </code></pre>
 * With the code above, user can type in directly into text field and do the calculation.
 * If you just want to display the result and don't mind if the text field accepts keyboard input,
 * you don't need to call registerKeyboardActions method.
 * <p/>
 * All numeric and operator keys work as expected. Here are a few special keys that worth mentioning
 * <ul>
 * <li> 'C', 'c' or ESC to clear current result
 * <li> '!' to make current displayed number from positive to negative (or from negative to positive)
 * <li> ENTER is equalivent to '='..
 * </ul>
 * <p/>
 * Another interesting way to use Calculator is to use it without using GUI.
 * <pre><code>
 * Calculator calculator = new Calculator();
 * calculator.input('1');
 * calculator.input('0');
 * calculator.input('*');
 * calculator.input('2');
 * calculator.input('4');
 * calculator.input('=');
 * System.out.println("10 * 24 = " + calculator.getDisplayText());
 * </code></pre>
 * The print out will be "10 * 24 = 240".
 * <p/>
 * There are seveal methods you can use to get internal state of the Calculator.
 * <ul>
 * <li> {@link #getDisplayText()}: to get the result that should be displayed. Please note, this method return a string.
 * <li> {@link #getResult()}: to get the last calculated result. This method returns a double value.
 * <li> {@link #getOperator()}: to get the current operator
 * <li> {@link #isOverflow()}: to check if there is an overflow. Usually if you try to divide by zero, you will get an overflow.
 * </ul>
 */
public class Calculator extends JPanel implements ActionListener {

    private double _result;
    private StringBuffer _op1;
    private StringBuffer _op2;
    private int _operator = OPERATOR_NONE;
    private String _displayText;
    private boolean _overflow = false;
    private boolean _negationOp1 = true;
    private boolean _backspaceOp1 = false;
    private boolean _backspaceOp2 = false;
    private boolean _clearOperatorPending = false;

    public final static int OPERATOR_NONE = -1;
    public final static int OPERATOR_ADD = 0;
    public final static int OPERATOR_MINUS = 1;
    public final static int OPERATOR_MULTIPLY = 2;
    public final static int OPERATOR_DIVIDE = 3;

    private AbstractButton _addButton;
    private AbstractButton _minusButton;
    private AbstractButton _multiplyButton;
    private AbstractButton _divideButton;
    private AbstractButton _pointButton;
    private AbstractButton _equalButton;
    private AbstractButton _backspaceButton;
    private AbstractButton _clearButton;
    private AbstractButton _negativeButton;
    private AbstractButton[] _numberButtons;

    private NumberFormat _displayFormat;

    public static final char CHAR_CLEAR = 'c';
    public static final char CHAR_POINT = '.';
    public static final char CHAR_ADD = '+';
    public static final char CHAR_MINUS = '-';
    public static final char CHAR_MULTIPLY = '*';
    public static final char CHAR_DIVIDE = '/';
    public static final char CHAR_EQUAL = '=';
    public static final char CHAR_NEGATIVE = '!';
    public static final char CHAR_BACKSPACE = '<';
    public static final char CHAR_0 = '0';
    public static final char CHAR_1 = '1';
    public static final char CHAR_2 = '2';
    public static final char CHAR_3 = '3';
    public static final char CHAR_4 = '4';
    public static final char CHAR_5 = '5';
    public static final char CHAR_6 = '6';
    public static final char CHAR_7 = '7';
    public static final char CHAR_8 = '8';
    public static final char CHAR_9 = '9';

    public final static String PROPERTY_DISPLAY_TEXT = "displayText";
    public final static String PROPERTY_OPERATOR = "operator";

    private int _buttonWidth = 24;
    private int _buttonHeight = 24;
    private int _buttonGap = 2;

    /**
     * Creates a <code>Calculator</code>.
     */
    public Calculator() {
        _op1 = new StringBuffer();
        _op2 = new StringBuffer();
        initComponents();
        _displayFormat = NumberFormat.getNumberInstance();
        configureNumberFormat();
        registerKeyboardActions(this, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Configures the number format for displaying purpose.
     */
    protected void configureNumberFormat() {
        _displayFormat.setMaximumFractionDigits(20);
        _displayFormat.setMinimumFractionDigits(0);
        _displayFormat.setGroupingUsed(false);
    }

    /**
     * Checks if the key event a valid key event that can be accepted by the Calculator.
     *
     * @param keyEvent the key event.
     * @return true if it is a valid key event for the Calculator.
     */
    public static boolean isValidKeyEvent(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        return (keyEvent.getModifiers() & ~KeyEvent.SHIFT_MASK) != 0 // if it has any modify, ignore it
                || Character.isDigit(c) || isOperator(keyEvent)
                || isEnter(keyEvent)
                || c == KeyEvent.VK_PERIOD || c == CHAR_CLEAR || Character.toLowerCase(c) == CHAR_CLEAR
                || c == KeyEvent.VK_ESCAPE
                || c == KeyEvent.VK_BACK_SPACE;
    }

    /**
     * Checks if the key event a key event for operators. In the other words, if it is {@link #CHAR_ADD},
     * {@link #CHAR_MINUS}, {@link #CHAR_MULTIPLY} or {@link #CHAR_DIVIDE}, this method will return true.
     *
     * @param keyEvent the key event.
     * @return true if it is a valid key event is an operator.
     */
    public static boolean isOperator(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        return c == CHAR_ADD || c == CHAR_MINUS || c == CHAR_MULTIPLY || c == CHAR_DIVIDE;
    }

    /**
     * Checks if the key event a key event for enter. In the other words, if it is {@link KeyEvent#VK_ENTER}, this method will return true.
     *
     * @param keyEvent the key event.
     * @return true if it is a valid key event is an enter key.
     */
    public static boolean isEnter(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        return c == KeyEvent.VK_ENTER;
    }

    /**
     * Registers necessary keyboard actions onto the component. Usually the component
     * is a <code>JTextField</code>.
     *
     * @param component the component where the key input will be taken and passed to the <code>Calculator</code>.
     * @param condition the condition as defined in
     *                  {@link JComponent#registerKeyboardAction(java.awt.event.ActionListener,javax.swing.KeyStroke,int)}.
     */
    public void registerKeyboardActions(JComponent component, int condition) {
        boolean isCellEditor = isCellEditor();
        component.registerKeyboardAction(this, "" + CHAR_ADD, KeyStroke.getKeyStroke(CHAR_ADD), condition);
        component.registerKeyboardAction(this, "" + CHAR_MINUS, KeyStroke.getKeyStroke(CHAR_MINUS), condition);
        component.registerKeyboardAction(this, "" + CHAR_MULTIPLY, KeyStroke.getKeyStroke(CHAR_MULTIPLY), condition);
        component.registerKeyboardAction(this, "" + CHAR_DIVIDE, KeyStroke.getKeyStroke(CHAR_DIVIDE), condition);
        component.registerKeyboardAction(this, "" + CHAR_EQUAL, KeyStroke.getKeyStroke(CHAR_EQUAL), condition);
        if (!isCellEditor)
            component.registerKeyboardAction(this, "" + CHAR_EQUAL, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), condition);
        component.registerKeyboardAction(this, "" + CHAR_0, KeyStroke.getKeyStroke(CHAR_0), condition);
        component.registerKeyboardAction(this, "" + CHAR_1, KeyStroke.getKeyStroke(CHAR_1), condition);
        component.registerKeyboardAction(this, "" + CHAR_2, KeyStroke.getKeyStroke(CHAR_2), condition);
        component.registerKeyboardAction(this, "" + CHAR_3, KeyStroke.getKeyStroke(CHAR_3), condition);
        component.registerKeyboardAction(this, "" + CHAR_4, KeyStroke.getKeyStroke(CHAR_4), condition);
        component.registerKeyboardAction(this, "" + CHAR_5, KeyStroke.getKeyStroke(CHAR_5), condition);
        component.registerKeyboardAction(this, "" + CHAR_6, KeyStroke.getKeyStroke(CHAR_6), condition);
        component.registerKeyboardAction(this, "" + CHAR_7, KeyStroke.getKeyStroke(CHAR_7), condition);
        component.registerKeyboardAction(this, "" + CHAR_8, KeyStroke.getKeyStroke(CHAR_8), condition);
        component.registerKeyboardAction(this, "" + CHAR_9, KeyStroke.getKeyStroke(CHAR_9), condition);
        component.registerKeyboardAction(this, "" + CHAR_POINT, KeyStroke.getKeyStroke(CHAR_POINT), condition);
        component.registerKeyboardAction(this, "" + CHAR_BACKSPACE, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), condition);
        if (!isCellEditor)
            component.registerKeyboardAction(this, "" + CHAR_CLEAR, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), condition);
        if (!isCellEditor)
            component.registerKeyboardAction(this, "" + CHAR_CLEAR, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        component.registerKeyboardAction(this, "" + CHAR_CLEAR, KeyStroke.getKeyStroke(Character.toUpperCase(CHAR_CLEAR)), condition);
        component.registerKeyboardAction(this, "" + CHAR_CLEAR, KeyStroke.getKeyStroke(Character.toLowerCase(CHAR_CLEAR)), condition);
    }

    /**
     * Unregisters the keyboard actions you registered using {@link #registerKeyboardActions(javax.swing.JComponent,int)}.
     *
     * @param component the component.
     */
    public void unregisterKeyboardActions(JComponent component) {
        boolean isCellEditor = isCellEditor();
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_ADD));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_MINUS));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_MULTIPLY));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_DIVIDE));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_EQUAL));
        if (!isCellEditor) component.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_0));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_1));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_2));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_3));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_4));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_5));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_6));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_7));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_8));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_9));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(CHAR_POINT));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
        if (!isCellEditor) component.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(Character.toUpperCase(CHAR_CLEAR)));
        component.unregisterKeyboardAction(KeyStroke.getKeyStroke(Character.toLowerCase(CHAR_CLEAR)));
    }

    protected void initComponents() {
        setLayout(new CalculatorLayoutManager());
        add(_addButton = createButton("+"));
        add(_minusButton = createButton("-"));
        add(_multiplyButton = createButton("*"));
        add(_divideButton = createButton("/"));
        _numberButtons = new AbstractButton[10];
        for (int i = 0; i <= 9; i++) {
            add(_numberButtons[i] = createButton("" + i));
        }
        add(_pointButton = createButton("."));
        add(_equalButton = createButton("="));
        add(_backspaceButton = createButton("��"));
        add(_negativeButton = createButton("�"));
        add(_clearButton = createButton("C"));
    }

    /**
     * Creates the button that is used in the Calculator. By default, it will create a JideButton. Here is the code. You can override it
     * to create your own button.
     * <pre><code>
     * AbstractButton button = new JideButton(text);
     * button.setOpaque(true);
     * button.setContentAreaFilled(true);
     * button.setRequestFocusEnabled(false);
     * button.setFocusable(false);
     * button.addActionListener(this);
     * return button;
     * </code></pre>
     *
     * @param text the text on the button.
     * @return the button.
     */
    protected AbstractButton createButton(String text) {
        AbstractButton button = new JideButton(text);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setRequestFocusEnabled(false);
        button.setFocusable(false);
        button.addActionListener(this);
        return button;
    }

    /**
     * Checks if the calculator is in overflow state.
     *
     * @return true if overflow.
     */
    public boolean isOverflow() {
        return _overflow;
    }

    /**
     * Sets the overflow flag.
     *
     * @param overflow the overflow flag.
     */
    public void setOverflow(boolean overflow) {
        _overflow = overflow;
    }

    /**
     * Inputs a char to the calculator. Please note, not all chars are acceptable. Valid chars are defined in {@link Calculator} class
     * as CHAR_XXX constants.
     *
     * @param c the char inputed char.
     */
    public void input(char c) {
        if (CHAR_CLEAR == Character.toLowerCase(c) || CHAR_CLEAR == Character.toUpperCase(c)) {
            clear();
            return;
        }

        if (_overflow) {
            beep();
            return;
        }

        if (Character.isDigit(c) || CHAR_POINT == c) {
            if (_clearOperatorPending) {
                setOperator(OPERATOR_NONE);
                _op1.setLength(0);
                _clearOperatorPending = false;
            }
            if (getOperator() == -1) {
                if (CHAR_POINT != c || _op1.indexOf("" + CHAR_POINT) == -1) {
                    _op1.append(c);
                    _backspaceOp1 = true;
                    _backspaceOp2 = false;
                    setDisplayText(_op1.toString());
                }
                else {
                    beep();
                }
            }
            else {
                if (CHAR_POINT != c || _op2.indexOf("" + CHAR_POINT) == -1) {
                    _op2.append(c);
                    _backspaceOp2 = true;
                    _backspaceOp1 = false;
                    setDisplayText(_op2.toString());
                }
                else {
                    beep();
                }
            }
        }
        else {
            switch (c) {
                case CHAR_ADD:
                    _op2.setLength(0);
                    calculateResult(false);
                    setOperator(OPERATOR_ADD);
                    _negationOp1 = false;
                    _clearOperatorPending = false;
                    break;
                case CHAR_MINUS:
                    _op2.setLength(0);
                    calculateResult(false);
                    setOperator(OPERATOR_MINUS);
                    _negationOp1 = false;
                    _clearOperatorPending = false;
                    break;
                case CHAR_MULTIPLY:
                    _op2.setLength(0);
                    calculateResult(false);
                    setOperator(OPERATOR_MULTIPLY);
                    _negationOp1 = false;
                    _clearOperatorPending = false;
                    break;
                case CHAR_DIVIDE:
                    _op2.setLength(0);
                    calculateResult(false);
                    setOperator(OPERATOR_DIVIDE);
                    _negationOp1 = false;
                    _clearOperatorPending = false;
                    break;
                case CHAR_EQUAL:
                    calculateResult(true);
                    _clearOperatorPending = true;
                    break;
                case CHAR_NEGATIVE:
                    if (_negationOp1) {
                        negativePressed(_op1);
                        setDisplayText(_op1.toString());
                    }
                    else {
                        negativePressed(_op2);
                        setDisplayText(_op2.toString());
                    }
                    break;
                case CHAR_BACKSPACE:
                    if (_backspaceOp1) {
                        backspacePressed(_op1);
                        setDisplayText(_op1.toString());
                    }
                    else if (_backspaceOp2) {
                        backspacePressed(_op2);
                        setDisplayText(_op2.toString());
                    }
                    else {
                        beep();
                    }
                    break;
            }
        }
    }

    protected void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    private void negativePressed(StringBuffer buf) {
        if (buf.length() == 0) {
            return;
        }
        if (buf.charAt(0) == CHAR_MINUS) {
            buf.deleteCharAt(0);
        }
        else {
            buf.insert(0, CHAR_MINUS);
        }
    }

    private void backspacePressed(StringBuffer buf) {
        if (buf.length() == 0) {
            return;
        }
        buf.deleteCharAt(buf.length() - 1);
    }

    private void calculateResult(boolean equalPressed) {
        if (getOperator() == -1) {
            return;
        }

        if (_op1.length() == 0) {
            beep();
            return;
        }

        if (equalPressed) {
            if (_op2.length() == 0) {
                _op2.append(_op1);
            }
        }
        else if (_op2.length() == 0) {
            return;
        }
        double op1 = Double.parseDouble(_op1.toString());
        double op2 = Double.parseDouble(_op2.toString());
        try {
            switch (getOperator()) {
                case OPERATOR_ADD:
                    _result = op1 + op2;
                    break;
                case OPERATOR_MINUS:
                    _result = op1 - op2;
                    break;
                case OPERATOR_MULTIPLY:
                    _result = op1 * op2;
                    break;
                case OPERATOR_DIVIDE:
                    if (op2 == 0) {
                        _result = Double.NaN;
                        _overflow = true;
                    }
                    else {
                        _result = op1 / op2;
                    }
                    break;
            }
        }
        catch (Exception e) {
            _overflow = true;
        }

        if (_overflow) {
            setDisplayText("E");
        }
        else {
            _op1.setLength(0);
            if (_displayFormat != null) {
                String displayText = _displayFormat.format(_result);
                setDisplayText(displayText);
            }
            else {
                setDisplayText("" + _result);
            }
            _op1.append(getDisplayText());
            _negationOp1 = true;
            _backspaceOp1 = true;
            _backspaceOp2 = false;
        }
    }

    private void clearOps() {
        setOperator(OPERATOR_NONE);
        _op1.setLength(0);
        _op2.setLength(0);
    }

    /**
     * Clears the internal state and reset the calculator.
     */
    public void clear() {
        clearOps();
        _overflow = false;
        _clearOperatorPending = false;
        setDisplayText("0");
    }

    /**
     * Gets the last calculated result.
     *
     * @return the last calculated result.
     */
    public double getResult() {
        return _result;
    }

    /**
     * Gets the display text.
     *
     * @return the display text.
     */
    public String getDisplayText() {
        return _displayText;
    }

    /**
     * Sets the display text and fire property change event on property named {@link #PROPERTY_DISPLAY_TEXT}.
     *
     * @param displayText the displayed text.
     */
    public void setDisplayText(String displayText) {
        String old = _displayText;
        _displayText = displayText;
        firePropertyChange(PROPERTY_DISPLAY_TEXT, old, _displayText);
    }

    /**
     * Gets the current operator.
     *
     * @return the current operator.
     */
    public int getOperator() {
        return _operator;
    }

    /**
     * Sets the operator and fire property change event on property named {@link #PROPERTY_OPERATOR}.
     *
     * @param operator the operator.
     */
    public void setOperator(int operator) {
        int old = _operator;
        if (old != operator) {
            _operator = operator;
            firePropertyChange(PROPERTY_OPERATOR, new Integer(old), new Integer(operator));
        }
    }

    private class CalculatorLayoutManager implements LayoutManager {
        public CalculatorLayoutManager() {
        }

        public void addLayoutComponent(String name, Component comp) {

        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            return minimumLayoutSize(parent);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(getButtonWidth() * 4 + getButtonGap() * 3, getButtonHeight() * 5 + getButtonGap() * 4);
        }

        public void layoutContainer(Container parent) {
            int x = 0;
            int y = 0;

            int w = getButtonWidth();
            int h = getButtonHeight();
            int gap = getButtonGap();

            _numberButtons[7].setBounds(x, y, w, h);
            x += w + gap;
            _numberButtons[8].setBounds(x, y, w, h);
            x += w + gap;
            _numberButtons[9].setBounds(x, y, w, h);
            x += w + gap;
            _divideButton.setBounds(x, y, w, h);

            x = 0;
            y += h + gap;

            _numberButtons[4].setBounds(x, y, w, h);
            x += w + gap;
            _numberButtons[5].setBounds(x, y, w, h);
            x += w + gap;
            _numberButtons[6].setBounds(x, y, w, h);
            x += w + gap;
            _multiplyButton.setBounds(x, y, w, h);

            x = 0;
            y += h + gap;

            _numberButtons[1].setBounds(x, y, w, h);
            x += w + gap;
            _numberButtons[2].setBounds(x, y, w, h);
            x += w + gap;
            _numberButtons[3].setBounds(x, y, w, h);
            x += w + gap;
            _minusButton.setBounds(x, y, w, h);

            x = 0;
            y += h + gap;

            _numberButtons[0].setBounds(x, y, w, h);
            x += w + gap;
            _pointButton.setBounds(x, y, w, h);
            x += w + gap;
            _negativeButton.setBounds(x, y, w, h);
            x += w + gap;
            _addButton.setBounds(x, y, w, h);

            x = 0;
            y += h + gap;

            _clearButton.setBounds(x, y, w, h);
            x += w + gap;
            _backspaceButton.setBounds(x, y, w, h);
            x += w + gap;
            _equalButton.setBounds(x, y, w * 2 + gap, h);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (_addButton == source) {
            input(CHAR_ADD);
        }
        else if (_minusButton == source) {
            input(CHAR_MINUS);
        }
        else if (_multiplyButton == source) {
            input(CHAR_MULTIPLY);
        }
        else if (_divideButton == source) {
            input(CHAR_DIVIDE);
        }
        else if (_equalButton == source) {
            input(CHAR_EQUAL);
        }
        else if (_pointButton == source) {
            input(CHAR_POINT);
        }
        else if (_negativeButton == source) {
            input(CHAR_NEGATIVE);
        }
        else if (_backspaceButton == source) {
            input(CHAR_BACKSPACE);
        }
        else if (_clearButton == source) {
            input(CHAR_CLEAR);
        }
        else {
            boolean found = false;
            for (int i = 0; i <= 9; i++) {
                if (_numberButtons[i] == source) {
                    input(("" + i).charAt(0));
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (e.getActionCommand() != null && e.getActionCommand().length() > 0) {
                    fakePressButton(e.getActionCommand().charAt(0));
                }
                else {
                    fakePressButton(CHAR_EQUAL);
                }
            }
        }
    }

    private final static int DELAY = 100;

    private void fakePressButton(char c) {
        switch (c) {
            case CHAR_CLEAR:
                _clearButton.doClick(DELAY);
                break;
            case CHAR_BACKSPACE:
                _backspaceButton.doClick(DELAY);
                break;
            case CHAR_EQUAL:
                _equalButton.doClick(DELAY);
                break;
            case CHAR_POINT:
                _pointButton.doClick(DELAY);
                break;
            case CHAR_NEGATIVE:
                _negativeButton.doClick(DELAY);
                break;
            case CHAR_ADD:
                _addButton.doClick(DELAY);
                break;
            case CHAR_MINUS:
                _minusButton.doClick(DELAY);
                break;
            case CHAR_MULTIPLY:
                _multiplyButton.doClick(DELAY);
                break;
            case CHAR_DIVIDE:
                _divideButton.doClick(DELAY);
                break;
            case CHAR_0:
                _numberButtons[0].doClick(DELAY);
                break;
            case CHAR_1:
                _numberButtons[1].doClick(DELAY);
                break;
            case CHAR_2:
                _numberButtons[2].doClick(DELAY);
                break;
            case CHAR_3:
                _numberButtons[3].doClick(DELAY);
                break;
            case CHAR_4:
                _numberButtons[4].doClick(DELAY);
                break;
            case CHAR_5:
                _numberButtons[5].doClick(DELAY);
                break;
            case CHAR_6:
                _numberButtons[6].doClick(DELAY);
                break;
            case CHAR_7:
                _numberButtons[7].doClick(DELAY);
                break;
            case CHAR_8:
                _numberButtons[8].doClick(DELAY);
                break;
            case CHAR_9:
                _numberButtons[9].doClick(DELAY);
                break;
        }
    }

    /**
     * Gets the display format for the number.
     *
     * @return the display format for the number.
     */
    public NumberFormat getDisplayFormat() {
        return _displayFormat;
    }

    /**
     * Sets the display format for the number.
     *
     * @param displayFormat the display format.
     */
    public void setDisplayFormat(NumberFormat displayFormat) {
        _displayFormat = displayFormat;
    }

    /**
     * Calculates the pending calculation. If the Calculator has both operations
     * and a valid operator, this method will do the calculation and set the display text and result.
     */
    public void commit() {
        if (!_clearOperatorPending) {
            input(CHAR_EQUAL);
        }
    }

    /**
     * Gets the button width.
     *
     * @return the button width.
     */
    public int getButtonWidth() {
        return _buttonWidth;
    }

    /**
     * Sets the button width.
     *
     * @param buttonWidth the new button width.
     */
    public void setButtonWidth(int buttonWidth) {
        _buttonWidth = buttonWidth;
    }

    /**
     * Gets the button height.
     *
     * @return the button height.
     */
    public int getButtonHeight() {
        return _buttonHeight;
    }

    /**
     * Sets the button height.
     *
     * @param buttonHeight the new button height.
     */
    public void setButtonHeight(int buttonHeight) {
        _buttonHeight = buttonHeight;
    }

    /**
     * Gets the gap betwen buttons. Default is 2.
     *
     * @return the gap betwen buttons.
     */
    public int getButtonGap() {
        return _buttonGap;
    }

    public void setButtonGap(int buttonGap) {
        _buttonGap = buttonGap;
    }

    /**
     * If this method return true, ENTER and ESCAPE key will be registered. Otherwise they will not be.
     * The reason we do so because the two keys are conflicted with keys in JTable.
     *
     * @return true or false.
     */
    protected boolean isCellEditor() {
        return false;
    }

    public void setInitialValue(String value) {
        _op1.setLength(0);
        _op1.append(value);
        _backspaceOp1 = true;
        _backspaceOp2 = false;
        setDisplayText(_op1.toString());
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.input('1');
        calculator.input('0');
        calculator.input('*');
        calculator.input('2');
        calculator.input('4');
        calculator.input('=');
        System.out.println("10 * 24 = " + calculator.getDisplayText());
    }

}