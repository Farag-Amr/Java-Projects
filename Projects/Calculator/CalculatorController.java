package Projects.Calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * The Controller in the MVC pattern. It handles user input from the View
 * and tells the Model to update its state. It then tells the View to refresh.
 */
public class CalculatorController implements ActionListener {
    private final CalculatorModel model;
    private final CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this); // Link this controller to the view's buttons
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the button text (what the user pressed)
        String command = ((JButton) e.getSource()).getText();

        // Tell the model to handle the input
        model.handleComplexInput(command);

        // Get the updated state from the model and tell the view to display it
        view.updateDisplay(model.getDisplayValue());
    }
}
