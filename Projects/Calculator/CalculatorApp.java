package Projects.Calculator;

import javax.swing.SwingUtilities;

public class CalculatorApp {
    public static void main(String[] args) {
        // Start the calculator UI on the Swing event thread
        SwingUtilities.invokeLater(() -> {
            // Create the model (handles calculator logic and state)
            CalculatorModel model = new CalculatorModel();

            // Create the view (the window and buttons)
            CalculatorView view = new CalculatorView("Calculator");

            // Create the controller (connects the view and model)
            new CalculatorController(model, view);

            // Show the calculator window
            view.setVisible(true);
        });
    }
}
