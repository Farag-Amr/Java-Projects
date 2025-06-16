package Projects.Calculator;

import javax.swing.*;
import java.awt.*;

// Simple calculator app using Java Swing
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
            display.setEditable(false); // User can't type directly
            display.setHorizontalAlignment(JTextField.RIGHT); // Text is right-aligned
            display.setBackground(Color.WHITE);
            display.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setPreferredSize(new Dimension(400, 150)); // Top 25% of window
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
            // Add each button to the panel
            for (String text : buttons) {
                JButton btn = new JButton(text);
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                // What happens when a button is clicked
                btn.addActionListener(e -> {
                    String current = display.getText();
                    String operators = "+-x÷^";
                    if (text.equals("Clr")) {
                        // Clear all text from the display
                        display.setText("");
                    } else if (text.equals("Del")) {
                        // Remove the last character from the display
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
                    } else if (!text.equals("=")) {
                        // Prevent operator as first char except '-'
                        if (current.isEmpty() && operators.contains(text) && !text.equals("-")) {
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
                                        // Only allow '-' after another operator if the next char is '('
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
                        // Add the button text to the display
                        display.setText(current + text);
                    }
                });
                buttonPanel.add(btn);
            }
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            frame.add(buttonPanel, BorderLayout.CENTER);

            // Show the window
            frame.setVisible(true);
        });
    }
}
