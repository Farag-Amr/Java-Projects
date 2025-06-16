package Projects.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
                    if (text.equals("Clr")) {
                        display.setText("");
                    } else if (text.equals("Del")) {
                        String current = display.getText();
                        if (!current.isEmpty()) {
                            display.setText(current.substring(0, current.length() - 1));
                        }
                    } else if (!text.equals("()") && !text.equals("=")) {
                        display.setText(display.getText() + text);
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
