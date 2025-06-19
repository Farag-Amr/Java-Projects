package Projects.Calculator;

import javax.swing.*;
import java.awt.*;

// This is a simple calculator application in Java using Swing.
// It provides basic arithmetic operations like addition, subtraction, multiplication, and division.
public class Calculator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main window
            JFrame frame = new JFrame("Calculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 600);
            frame.setLayout(new BorderLayout());

            // --- Top display area for showing input/output ---
            JTextField display = new JTextField();
            display.setFont(new Font("Arial", Font.BOLD, 32));
            display.setEditable(false); // User CANNOT type directly
            display.setHorizontalAlignment(JTextField.RIGHT); // Text is right-aligned
            display.setBackground(Color.WHITE);
            display.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setPreferredSize(new Dimension(400, 150));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(display, BorderLayout.CENTER);
            frame.add(topPanel, BorderLayout.NORTH);

            // --- Button panel for calculator buttons (4x5 grid) ---
            JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
            String[] buttons = {
                    "Clr", "()", "^", "÷",
                    "7", "8", "9", "x",
                    "4", "5", "6", "-",
                    "1", "2", "3", "+",
                    "0", ".", "Del", "="
            };
            // Adds each button to the panel
            for (String text : buttons) {
                JButton btn = new JButton(text);
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                btn.addActionListener(e -> {
                    String current = display.getText();
                    String operators = "+-x÷^";
                    if (text.equals("Clr")) {
                        // Clears all text from the display
                        display.setText("");
                    } else if (text.equals("Del")) {
                        // Removes the last character from the display
                        if (!current.isEmpty()) {
                            display.setText(current.substring(0, current.length() - 1));
                        }
                    } else if (text.equals("()")) {
                        // Smart handling for parentheses
                        long openCount = current.chars().filter(ch -> ch == '(').count();
                        long closeCount = current.chars().filter(ch -> ch == ')').count();
                        if (openCount == 0 && closeCount == 0) {
                            // No parentheses yet, add (
                            display.setText(current + "(");
                        } else if (openCount > closeCount) {
                            // More ( than ), decide what to add
                            int lastOpen = current.lastIndexOf('(');
                            boolean hasNumber = false;
                            for (int i = lastOpen + 1; i < current.length(); i++) {
                                if (Character.isDigit(current.charAt(i))) {
                                    hasNumber = true;
                                    break;
                                }
                            }
                            if (hasNumber) {
                                // If last char is operator, add (, else add )
                                int lastIdx = current.length() - 1;
                                while (lastIdx >= 0
                                        && (current.charAt(lastIdx) == '(' || current.charAt(lastIdx) == ')')) {
                                    lastIdx--;
                                }
                                if (lastIdx >= 0 && operators.indexOf(current.charAt(lastIdx)) != -1) {
                                    display.setText(current + "(");
                                } else {
                                    display.setText(current + ")");
                                }
                            } else {
                                display.setText(current + "(");
                            }
                        } else {
                            // Default to adding (
                            display.setText(current + "(");
                        }
                    } else if (text.equals("=")) {
                        // --- Parsing section for '=' ---
                        String expr = display.getText();
                        try {
                            double result = parse(expr);
                            // Only show .0 if not a whole number
                            if (result == (long) result) {
                                display.setText(Long.toString((long) result));
                            } else {
                                display.setText(Double.toString(result));
                            }
                        } catch (ArithmeticException ex) {
                            if ("DIV0".equals(ex.getMessage())) {
                                display.setText("ERROR DIVISION BY 0");
                            } else {
                                display.setText("Error");
                            }
                        } catch (Exception ex) {
                            display.setText("Error");
                        }
                    } else {
                        // --- Standalone 0 replacement logic with all rules ---
                        // Finds the start of the current number segment
                        int lastNonDigit = -1;
                        for (int i = current.length() - 1; i >= 0; i--) {
                            char ch = current.charAt(i);
                            if (!Character.isDigit(ch) && ch != '.') {
                                lastNonDigit = i;
                                break;
                            }
                        }
                        int numStart = lastNonDigit + 1;
                        boolean replacedZero = false;
                        boolean replacingZeroWithMinus = false;
                        // Check for standalone 0 in the current number segment
                        if (numStart < current.length() && current.charAt(numStart) == '0') {
                            boolean isStandaloneZero = true;
                            if (numStart + 1 < current.length() && current.charAt(numStart + 1) == '.') {
                                isStandaloneZero = false;
                            }
                            if (numStart + 1 < current.length() && Character.isDigit(current.charAt(numStart + 1))) {
                                isStandaloneZero = false;
                            }
                            // Only replace if not adding a decimal
                            if (isStandaloneZero && !text.equals(".")) {
                                // Allow '-' to replace a leading 0 at the start
                                if (numStart == 0 && text.equals("-")) {
                                    display.setText("-");
                                    return;
                                } else {
                                    String newText = current.substring(0, numStart) + text;
                                    current = newText;
                                    replacedZero = true;
                                }
                            }
                        }
                        // Prevent operator as first char except '-'
                        if (!replacingZeroWithMinus && (replacedZero ? current.length() == 1 : current.isEmpty())
                                && operators.contains(text) && !text.equals("-")) {
                            return;
                        }
                        // Prevent two operators in a row, except for special '-' rule
                        if (operators.contains(text)) {
                            int i = current.length() - 1;
                            while (i >= 0 && (current.charAt(i) == '(' || current.charAt(i) == ')')) {
                                i--;
                            }
                            if (i >= 0) {
                                char last = current.charAt(i);
                                if (operators.indexOf(last) != -1) {
                                    if (text.equals("-")) {
                                        if (current.length() > i + 1 && current.charAt(i + 1) == '(') {
                                            // allow
                                        } else {
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }
                        if (replacedZero) {
                            display.setText(current);
                        } else {
                            display.setText(current + text);
                        }
                    }
                });
                buttonPanel.add(btn);
            }
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            frame.add(buttonPanel, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }

    private static double parse(String expr) throws ArithmeticException {
        // Remove spaces
        expr = expr.replace(" ", "");

        // Handles negative numbers at the start or after '('
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '-' && (i == 0 || expr.charAt(i - 1) == '(')) {
                sb.append("~"); // Uses ~ as a marker for negative numbers
            } else {
                sb.append(c);
            }
        }
        String replaced = sb.toString();

        // --- Parentheses parsing ---
        while (replaced.contains("(")) {
            int close = replaced.indexOf(')');
            if (close == -1)
                break; // Unmatched parenthesis, stop
            int open = replaced.lastIndexOf('(', close);
            if (open == -1)
                break; // Unmatched parenthesis, stop
            String inside = replaced.substring(open + 1, close);
            double insideVal = parseNoParen(inside);
            // Replace the parenthesis with the result
            replaced = replaced.substring(0, open) + insideVal + replaced.substring(close + 1);
        }

        // Evaluate the remaining expression (no parentheses left)
        return parseNoParen(replaced);
    }

    // Helper to parse expressions with +, -, x, ÷ and negative numbers, but no
    // parentheses
    private static double parseNoParen(String expr) throws ArithmeticException {
        // Split by '+'
        String[] plusParts = expr.split("\\+");
        double sum = 0;
        for (String part : plusParts) {
            // Now split by '-' (but not if it's a negative marker)
            double subtotal = 0;
            String[] minusParts = part.split("(?<=\\d)-");
            for (int i = 0; i < minusParts.length; i++) {
                String p = minusParts[i];
                if (p.isEmpty())
                    continue;
                double val;
                // Handles multiplication and division inside each minus part
                String[] multDivParts = p.split("x");
                double multResult = 1;
                for (int j = 0; j < multDivParts.length; j++) {
                    String m = multDivParts[j];
                    // Handles division within each multiplication segment
                    String[] divParts = m.split("÷");
                    double divResult = 0;
                    for (int k = 0; k < divParts.length; k++) {
                        String d = divParts[k];
                        if (d.isEmpty())
                            continue;
                        double dv;
                        if (d.contains("~")) {
                            dv = -Double.parseDouble(d.replace("~", ""));
                        } else {
                            dv = Double.parseDouble(d);
                        }
                        if (k == 0) {
                            divResult = dv;
                        } else {
                            if (dv == 0) {
                                throw new ArithmeticException("DIV0");
                            }
                            divResult /= dv;
                        }
                    }
                    multResult *= divResult;
                }
                val = multResult;
                if (i == 0) {
                    subtotal = val;
                } else {
                    subtotal -= val;
                }
            }
            sum += subtotal;
        }
        return sum;
    }
}
