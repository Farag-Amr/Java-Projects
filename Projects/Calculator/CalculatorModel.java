package Projects.Calculator;

import java.util.ArrayList;
import java.util.List;

import Projects.Calculator.Parser.*;

/**
 * The Model in the MVC pattern. It holds the calculator's state and business
 * logic.
 * It is completely independent of the UI.
 */
public class CalculatorModel {
    private final List<Token> tokens; // Stores the current input as tokens
    private boolean isErrorState = false; // Tracks if an error occurred

    public CalculatorModel() {
        this.tokens = new ArrayList<>();
    }

    // --- State Modification Methods ---

    // Adds input (like a button press) to the current expression
    public void addInput(String input) {
        if (isErrorState) {
            clear();
        }
        // Prevent two operators in a row
        if (!tokens.isEmpty()) {
            Token lastToken = tokens.get(tokens.size() - 1);
            boolean lastIsOp = lastToken.getType() == TokenType.OPERATOR;
            boolean inputIsOp = "+-x÷^".contains(input);
            if (lastIsOp && inputIsOp) {
                // Ignores the input if it's an operator after another operator
                return;
            }
        }
        // Re-tokenize the entire expression after each input
        String currentExpression = getExpressionString() + input;
        try {
            Lexer.tokenize(currentExpression); // Validate input
            this.tokens.clear();
            this.tokens.addAll(Lexer.tokenize(currentExpression));
        } catch (IllegalArgumentException e) {
            // Ignore invalid input (like two operators in a row)
            System.err.println("Invalid input ignored: " + e.getMessage());
        }
    }

    // Handles special buttons and complex input (parentheses, delete, clear,
    // equals)
    public void handleComplexInput(String text) {
        if (isErrorState) {
            clear();
        }

        switch (text) {
            case "()":
                // This logic can be made much smarter, but for now, it's a placeholder.
                // A better approach would be to analyze the token list.
                addInput("("); // Simplified
                break;
            case "Del":
                deleteLast();
                break;
            case "Clr":
                clear();
                break;
            case "=":
                evaluate();
                break;
            default:
                addInput(text);
                break;
        }
    }

    // Removes the last token (like pressing backspace)
    private void deleteLast() {
        if (!tokens.isEmpty()) {
            tokens.remove(tokens.size() - 1);
        }
        isErrorState = false;
    }

    // Clears the calculator state
    public void clear() {
        tokens.clear();
        isErrorState = false;
    }

    // Evaluates the current expression and updates the state with the result
    public void evaluate() {
        if (tokens.isEmpty()) {
            return;
        }

        try {
            // 1. Convert infix token list to postfix (RPN) queue
            ShuntingYardParser parser = new ShuntingYardParser(tokens);
            List<Token> postfix = parser.parse();

            // 2. Evaluate the postfix queue
            double result = ReversePolishNotationEvaluator.evaluate(postfix);

            // 3. Update state with the result
            tokens.clear();
            isErrorState = false;

            // Format result as integer if possible
            if (result == (long) result) {
                tokens.addAll(Lexer.tokenize(String.valueOf((long) result)));
            } else {
                tokens.addAll(Lexer.tokenize(String.valueOf(result)));
            }

        } catch (Exception e) {
            tokens.clear();
            isErrorState = true;
            System.err.println("Evaluation Error: " + e.getMessage());
        }
    }

    // --- State Accessor Methods ---

    // Returns the string to display on the calculator
    public String getDisplayValue() {
        if (isErrorState) {
            return "Error";
        }
        if (tokens.isEmpty()) {
            return "";
        }
        return getExpressionString();
    }

    // Builds the current expression as a string
    private String getExpressionString() {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.getValue());
        }
        return sb.toString();
    }
}
