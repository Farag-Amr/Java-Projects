package Projects.Calculator;

import javax.swing.*;
import java.awt.*;

// This is a simple calculator application in Java using Swing.
// It provides basic arithmetic operations like addition, subtraction, multiplication, and division.
public class Calculator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Calculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 600);
            frame.setLayout(new BorderLayout());

            // Top panel with a text field for input/output
            JTextField display = new JTextField();
            display.setFont(new Font("Arial", Font.BOLD, 32));
            display.setEditable(false);
            display.setHorizontalAlignment(JTextField.RIGHT);
            display.setBackground(Color.WHITE);
            display.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setPreferredSize(new Dimension(400, 150));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(display, BorderLayout.CENTER);
            frame.add(topPanel, BorderLayout.NORTH);

            // Button panel (4x5)
            JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
            String[] buttons = {
                    "Clr", "()", "^", "÷",
                    "7", "8", "9", "x",
                    "4", "5", "6", "-",
                    "1", "2", "3", "+",
                    "0", ".", "Del", "="
            };
            for (String text : buttons) {
                JButton btn = new JButton(text);
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                btn.addActionListener(e -> {
                    String current = display.getText();
                    String operators = "+-x÷^";
                    if (text.equals("Clr")) {
                        display.setText("");
                    } else if (text.equals("Del")) {
                        if (!current.isEmpty()) {
                            display.setText(current.substring(0, current.length() - 1));
                        }
                    } else if (text.equals("()")) {
                        long openCount = current.chars().filter(ch -> ch == '(').count();
                        long closeCount = current.chars().filter(ch -> ch == ')').count();
                        if (openCount == 0 && closeCount == 0) {
                            display.setText(current + "(");
                        } else if (openCount > closeCount) {
                            int lastOpen = current.lastIndexOf('(');
                            boolean hasNumber = false;
                            for (int i = lastOpen + 1; i < current.length(); i++) {
                                if (Character.isDigit(current.charAt(i))) {
                                    hasNumber = true;
                                    break;
                                }
                            }
                            if (hasNumber) {
                                // Check if the most recent char is an operator
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
                            display.setText(current + "(");
                        }
                    } else if (!text.equals("=")) {
                        if (current.isEmpty() && operators.contains(text) && !text.equals("-")) {
                            return;
                        }
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
                        display.setText(current + text);
                    }
                });
                buttonPanel.add(btn);
            }
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            frame.add(buttonPanel, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
