package Projects.Calculator;

import javax.swing.*;
import java.awt.*;

/**
 * The View in the MVC pattern. It is responsible for displaying the UI.
 * It is "dumb" and only forwards user actions to the Controller.
 */
public class CalculatorView extends JFrame {
    private final JTextField display;
    private final JPanel buttonPanel;

    public CalculatorView(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLayout(new BorderLayout());

        // --- Top display area for showing input/output ---
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(400, 150));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(display, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // --- Button panel for calculator buttons ---
        buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
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
            // The controller will be added later
            buttonPanel.add(btn);
        }
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buttonPanel, BorderLayout.CENTER);
    }

    // Lets the controller listen to all button presses
    public void setController(CalculatorController controller) {
        for (Component comp : buttonPanel.getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).addActionListener(controller);
            }
        }
    }

    // Updates the calculator display
    public void updateDisplay(String text) {
        display.setText(text);
    }
}
